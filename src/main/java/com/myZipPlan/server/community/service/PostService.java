package com.myZipPlan.server.community.service;

import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.post.AddPostRequest;
import com.myZipPlan.server.community.dto.post.PostResponse;
import com.myZipPlan.server.community.dto.post.UpdatePostRequest;
import com.myZipPlan.server.community.repository.PostRepository;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public Post addPost(AddPostRequest addPostRequest, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 이미지 파일 업로드 후 URL 획득
        String imageUrl = null;

        if (addPostRequest.getImageFile() != null && !addPostRequest.getImageFile().isEmpty()) {
            imageUrl = s3Service.uploadFile(addPostRequest.getImageFile());
        }

        // AddPostRequest를 이용해 Post 엔티티 생성
        Post post = addPostRequest.toEntity(user, imageUrl);

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
    public Post updatePost(Long postId, Long userId, UpdatePostRequest updatePostRequest) throws IOException  {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Only the author can update the post");
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

        return post;
    }

    // 게시글 삭제 로직
    public void deletePost(Long userId, Long postId) {
        // 사용자가 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 해당 게시글이 사용자가 작성한 게시글인지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getUser().equals(user)) {
            throw new IllegalStateException("User is not authorized to delete this post");
        }

        // 게시글 삭제
        postRepository.delete(post);
    }

    // 게시글 좋아요
    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        // 좋아요 처리 로직 (예: 좋아요 수 증가 등)
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        // 좋아요 취소 처리 로직 (예: 좋아요 수 감소 등)
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
    }
}


