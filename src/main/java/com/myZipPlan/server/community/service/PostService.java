package com.myZipPlan.server.community.service;

import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.post.request.PostCreateRequest;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.dto.post.request.UpdatePostRequest;
import com.myZipPlan.server.common.enums.community.PostSortType;
import com.myZipPlan.server.community.repository.PostRepository;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public Post addPost(PostCreateRequest postCreateRequest, String oauthProviderId) throws IOException {
        // security session 통해
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));

        // 이미지 파일 업로드 후 URL 획득
        String imageUrl = null;

        if (postCreateRequest.getImageFile() != null && !postCreateRequest.getImageFile().isEmpty()) {
            imageUrl = s3Service.uploadFile(postCreateRequest.getImageFile());
        }

        // AddPostRequest를 이용해 Post 엔티티 생성
        Post post = postCreateRequest.toEntity(user, imageUrl);
        return postRepository.save(post);
    }

    // 모든 게시글 조회
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostResponse::fromEntity)  // Post 엔티티를 PostResponse로 변환
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    @Transactional
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return PostResponse.fromEntity(post);
    }

    // 게시글 수정
    @Transactional
    public Post updatePost(String oauthProviderId, Long postId, UpdatePostRequest updatePostRequest) throws IOException  {
        User user = userRepository.findByOauthProviderId(oauthProviderId)
                .orElseThrow(() -> new IllegalArgumentException("세션에 연결된 oauthProviderId를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getOauthProviderId().equals(user.getOauthProviderId())) {
            throw new IllegalArgumentException("해당 글 작성자만 수정이 할 수 있습니다.");
        }

        // 이미지 파일이 새로 업로드되면 이미지 URL 업데이트
        String imageUrl = post.getImageUrl();  // 기존 이미지 URL 유지
        MultipartFile imageFile = updatePostRequest.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadFile(imageFile);
        }

        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());
        post.setImageUrl(imageUrl);
        post.setLastModifiedDate(LocalDateTime.now()); // 업데이트 시 수정일 갱신
        return postRepository.save(post);
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

    // 게시글 좋아요
    @Transactional
    public void likePost(String oauthProviderId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // 좋아요 처리 로직 (예: 좋아요 수 증가 등)
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(String oauthProviderId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // 좋아요 취소 처리 로직 (예: 좋아요 수 감소 등)
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
    }

    // 게시글조회(정렬)
    public List<PostResponse> getPostsBySortType(PostSortType sortType) {
        if (sortType == PostSortType.LATEST) {
            return postRepository.findAllByOrderByCreatedDateDesc()
                    .stream().map(PostResponse::fromEntity)
                    .collect(Collectors.toList());
        } else if (sortType == PostSortType.POPULAR) {
            return postRepository.findAllByOrderByLikesDesc()
                    .stream().map(PostResponse::fromEntity)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}


