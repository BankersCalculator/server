package com.myZipPlan.server.community.service;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.domain.PostLike;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class    PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PostLikeRepository postLikeRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest, String oauthProviderId) throws IOException {
        // 1. 유효성 검증
        postCreateRequest.validate();

        // 2. 유저 확인
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
        logger.info("============== user : {}", user);

        // 3. 보고서
        LoanAdviceResult loanAdviceResult = null;
        if (postCreateRequest.getLoanAdviceResultId() != null) {
            loanAdviceResult = loanAdviceResultRepository.findById(postCreateRequest.getLoanAdviceResultId())
                    .orElseThrow(() -> new IllegalArgumentException("선택한 보고서는 존재하지 않습니다."));
        }

        LoanAdviceSummaryResponse loanAdviceSummaryReport = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);

        // 4. 파일업로드
        String imageUrl = null;
        if (postCreateRequest.getImageFile() != null && !postCreateRequest.getImageFile().isEmpty()) {
            MultipartFile file = postCreateRequest.getImageFile();
            imageUrl = s3Service.uploadFile(file);
        }

        logger.info("============== 진입, imageUrl : {}", imageUrl);


        Post post = postCreateRequest.toEntity(user, imageUrl, loanAdviceResult);
        postRepository.save(post);

        return PostResponse.fromEntity(post, loanAdviceSummaryReport);
    }

    // 모든 게시글 조회
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> {
                    LoanAdviceResult loanAdviceResult = post.getLoanAdviceResult();
                    LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);
                    return PostResponse.fromEntity(post, loanAdviceSummaryResponse);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        LoanAdviceResult loanAdviceResult = post.getLoanAdviceResult();
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);
        return PostResponse.fromEntity(post, loanAdviceSummaryResponse);
    }

    @Transactional
    public PostResponse updatePost(String oauthProviderId, Long postId, PostUpdateRequest postUpdateRequest) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 사용자 권한 확인
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("작성자만 게시글을 수정할 수 있습니다.");
        }

        LoanAdviceResult loanAdviceResult = null;
        if (postUpdateRequest.getLoanAdviceResultId() != null) {
            loanAdviceResult = loanAdviceResultRepository.findById(postUpdateRequest.getLoanAdviceResultId())
                    .orElseThrow(() -> new IllegalArgumentException("LoanAdviceResult를 찾을 수 없습니다."));
        }

        String imageUrl = postUpdateRequest.getExistingImageUrl();  // 기존 이미지 URL 사용
        if (postUpdateRequest.getImageFile() != null && !postUpdateRequest.getImageFile().isEmpty()) {
            imageUrl = s3Service.uploadFile(postUpdateRequest.getImageFile());
        }

        postUpdateRequest.updatePost(post, loanAdviceResult, imageUrl);
        postRepository.save(post);

        LoanAdviceSummaryResponse loanAdviceSummaryReport = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);
        return PostResponse.fromEntity(post, loanAdviceSummaryReport);
    }


    // 게시글 삭제 로직
    public void deletePost(String oauthProviderId, Long postId) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getOauthProviderId().equals(user.getOauthProviderId())) {
            throw new IllegalArgumentException("해당 글 작성자만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        postRepository.delete(post);
    }

    @Transactional
    public void likePost(String oauthProviderId, Long postId) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 이미 좋아요를 눌렀는지 확인
        boolean alreadyLiked = postLikeRepository.findByPostAndUser(post, user).isPresent();
        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        // 좋아요 추가
        postLikeRepository.save(new PostLike(post, user));
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(String oauthProviderId, Long postId) {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 좋아요를 누른 기록이 있는지 확인
        PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 좋아요를 누르지 않았습니다."));

        // 좋아요 제거
        postLikeRepository.delete(postLike);
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
    }

    // 게시글조회(정렬)
    public List<PostResponse> getPostsBySortType(PostSortType sortType) {
        List<Post> posts;

        if (sortType == PostSortType.LATEST) {
            posts = postRepository.findAllByOrderByCreatedDateDesc();
        } else if (sortType == PostSortType.POPULAR) {
            posts = postRepository.findAllByOrderByLikesDesc();
        } else {
            return Collections.emptyList();
        }

        return posts.stream()
                .map(post -> {
                    LoanAdviceResult loanAdviceResult = post.getLoanAdviceResult();
                    LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.fromEntity(loanAdviceResult);
                    return PostResponse.fromEntity(post, loanAdviceSummaryResponse);
                })
                .collect(Collectors.toList());
    }
}


