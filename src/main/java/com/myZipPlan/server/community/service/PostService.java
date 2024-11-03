package com.myZipPlan.server.community.service;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.domain.PostLike;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import com.myZipPlan.server.community.dto.post.request.PostCreateRequest;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.dto.post.request.PostUpdateRequest;
import com.myZipPlan.server.common.enums.community.PostSortType;
import com.myZipPlan.server.community.repository.PostLikeRepository;
import com.myZipPlan.server.community.repository.PostRepository;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PostLikeRepository postLikeRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;
    private final CommentService commentService;

    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest, String oauthProviderId) throws IOException {
        // 1. 유효성 검증
        postCreateRequest.validate();

        // 2. 유저 확인
        User user = getUserByOauthProviderId(oauthProviderId);

        // 3. 보고서
        LoanAdviceResult loanAdviceResult = getLoanAdviceResult(postCreateRequest.getLoanAdviceResultId());
        LoanAdviceSummaryResponse loanAdviceSummaryReport = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);

        // 4. 파일업로드
        String imageUrl = uploadImage(postCreateRequest.getImageFile());
        Post post = postCreateRequest.toEntity(user, imageUrl, loanAdviceResult);
        postRepository.save(post);

        List<CommentResponse> comments = commentService.getComments(oauthProviderId, post.getId());
        return PostResponse.fromEntity(post, comments, loanAdviceSummaryReport, false, "N");
    }

    @Transactional
    public PostResponse updatePost(String oauthProviderId, Long postId, PostUpdateRequest postUpdateRequest) throws IOException {
        User user = getUserByOauthProviderId(oauthProviderId);
        Post post = getPostById(postId);

        String authority = determineAuthority(user, post);
        validateUserAuthority(user, post, authority);

        LoanAdviceResult loanAdviceResult = getLoanAdviceResult(postUpdateRequest.getLoanAdviceResultId());
        LoanAdviceSummaryResponse loanAdviceSummaryReport = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);

        String imageUrl = handleImageUpdate(postUpdateRequest, post);
        postUpdateRequest.updatePost(post, loanAdviceResult, imageUrl);
        postRepository.save(post);

        boolean like = postLikeRepository.findByPostAndUser(post, user).isPresent();
        List<CommentResponse> comments = commentService.getComments(oauthProviderId, post.getId());

        return PostResponse.fromEntity(post, comments, loanAdviceSummaryReport, like, authority);
    }

    @Transactional
    public void deletePost(String oauthProviderId, Long postId) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Post post = getPostById(postId);

        String authority = determineAuthority(user, post);
        validateUserAuthority(user, post, authority);

        deleteImageIfExists(post);
        postRepository.delete(post);
    }

    @Transactional
    public List<PostResponse> getAllPosts(String oauthProviderId, int page, int size) {
        Optional<User> optionalUser = getOptionalUserByOauthProviderId(oauthProviderId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.findAllWithComments(pageable);

        return postsPage.stream()
                .map(post -> {
                    List<CommentResponse> comments = commentService.getComments(oauthProviderId, post.getId());
                    LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.fromEntity(post.getLoanAdviceResult());

                    boolean like = false;
                    String authority = "N";
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        like = postLikeRepository.findByPostAndUser(post, user).isPresent();
                        authority = determineAuthority(user, post);
                    }

                    return PostResponse.fromEntity(post, comments, loanAdviceSummaryResponse, like, authority);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostResponse> getPostsBySortType(String oauthProviderId, PostSortType sortType, int page, int size) {
        Optional<User> optionalUser = getOptionalUserByOauthProviderId(oauthProviderId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = getPostsSortedByType(sortType, pageable);

        return postsPage.stream()
                .map(post -> {
                    List<CommentResponse> comments = commentService.getComments(oauthProviderId, post.getId());
                    LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.fromEntity(post.getLoanAdviceResult());

                    boolean like = false;
                    String authority = "N";
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        like = postLikeRepository.findByPostAndUser(post, user).isPresent();
                        authority = determineAuthority(user, post);

                    }

                    return PostResponse.fromEntity(post, comments, loanAdviceSummaryResponse, like, authority);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse getPostById(String oauthProviderId, Long id) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Post post = getPostById(id);
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.fromEntity(post.getLoanAdviceResult());

        boolean like = postLikeRepository.findByPostAndUser(post, user).isPresent();
        List<CommentResponse> comments = commentService.getComments(oauthProviderId, post.getId());
        String authority = determineAuthority(user, post);

        return PostResponse.fromEntity(user, post, comments, loanAdviceSummaryResponse, like, authority);
    }



    @Transactional
    public void likePost(String oauthProviderId, Long postId) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Post post = getPostById(postId);

        if (postLikeRepository.findByPostAndUser(post, user).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        postLikeRepository.save(new PostLike(post, user));
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(String oauthProviderId, Long postId) {
        User user = getUserByOauthProviderId(oauthProviderId);
        Post post = getPostById(postId);

        PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 좋아요를 누르지 않았습니다."));

        postLikeRepository.delete(postLike);
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
    }

    private User getUserByOauthProviderId(String oauthProviderId) {
        return userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
    }

    private Post getPostById(Long postId) {
        return postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private Optional<User> getOptionalUserByOauthProviderId(String oauthProviderId) {
        return (oauthProviderId != null) ? userRepository.findByOauthProviderId(oauthProviderId) : Optional.empty();
    }

    private LoanAdviceResult getLoanAdviceResult(Long loanAdviceResultId) {
        return (loanAdviceResultId != null) ? loanAdviceResultRepository.findById(loanAdviceResultId)
                .orElseThrow(() -> new IllegalArgumentException("LoanAdviceResult를 찾을 수 없습니다.")) : null;
    }

    private Page<Post> getPostsSortedByType(PostSortType sortType, Pageable pageable) {
        if (sortType == PostSortType.LATEST) {
            return postRepository.findAllByOrderByCreatedDateDesc(pageable);
        } else if (sortType == PostSortType.POPULAR) {
            return postRepository.findAllByOrderByLikesDesc(pageable);
        } else {
            return Page.empty();
        }
    }

    private boolean isPostOwner(User user, Post post) {
        return post.getUser().getOauthProviderId().equals(user.getOauthProviderId());
    }

    private String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            return s3Service.uploadFile(imageFile);
        }
        return null;
    }

    private String handleImageUpdate(PostUpdateRequest postUpdateRequest, Post post) throws IOException {
        String imageUrl = post.getImageUrl();
        if (hasNewImage(postUpdateRequest)) {
            imageUrl = handleNewImage(postUpdateRequest.getImageFile(), imageUrl);
        } else if (shouldDeleteExistingImage(postUpdateRequest, imageUrl)) {
            imageUrl = deleteExistingImage(imageUrl);
        }
        return imageUrl;
    }

    private void deleteImageIfExists(Post post) {
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            s3Service.deleteFileByImageUri(post.getImageUrl());
        }
    }

    private boolean hasNewImage(PostUpdateRequest postUpdateRequest) {
        return postUpdateRequest.getImageFile() != null && !postUpdateRequest.getImageFile().isEmpty();
    }

    private boolean shouldDeleteExistingImage(PostUpdateRequest postUpdateRequest, String imageUrl) {
        return (postUpdateRequest.getExistingImageUrl() == null || postUpdateRequest.getExistingImageUrl().isEmpty())
                && imageUrl != null && !imageUrl.isEmpty();
    }

    private String handleNewImage(MultipartFile newImageFile, String existingImageUrl) throws IOException {
        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            s3Service.deleteFileByImageUri(existingImageUrl); // 기존 이미지 삭제
        }
        return s3Service.uploadFile(newImageFile); // 새로운 이미지 업로드 후 URL 반환
    }

    private String deleteExistingImage(String existingImageUrl) {
        s3Service.deleteFileByImageUri(existingImageUrl); // 기존 이미지 삭제
        return null; // 이미지가 없음을 나타내는 null 반환
    }

    private String determineAuthority(User user, Post post) {
        if (isPostOwner(user, post))  {
            return "ALL";
        } else if  (user.getRoleType() == RoleType.ADMIN){
            return "DELETE";
        } else {
            return "N";
        }
    }

    private void validateUserAuthority(User user, Post post, String authority) {
        if (!isPostOwner(user, post) && !(authority.equals("DELETE") || authority.equals("ALL"))) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

}

