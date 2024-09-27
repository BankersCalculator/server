package com.myZipPlan.server.api.community.service;

import com.myZipPlan.server.common.enums.community.PostSortType;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.repository.PostLikeRepository;
import com.myZipPlan.server.community.repository.PostRepository;
import com.myZipPlan.server.community.service.CommentService;
import com.myZipPlan.server.community.service.PostService;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private CommentService commentService;

    private User user;

    @BeforeEach
    public void setUp() {
        // User 객체를 모킹하여 필요한 메서드를 설정
        user = mock(User.class);
        // 불필요한 스터빙 제거
    }

    @Test
    public void testGetPostsBySortType_LATEST() {
        // Given
        String oauthProviderId = "test_oauth_id";
        when(userRepository.findByOauthProviderId(oauthProviderId)).thenReturn(Optional.of(user));

        List<Post> posts = Arrays.asList(
                createMockPost(1L, LocalDateTime.now().minusDays(1), 5),
                createMockPost(2L, LocalDateTime.now().minusHours(1), 10),
                createMockPost(3L, LocalDateTime.now().minusDays(2), 15)
        );

        // posts 리스트를 createdDate 기준 내림차순으로 정렬
        posts.sort(Comparator.comparing(Post::getCreatedDate).reversed());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
        when(postRepository.findAllByOrderByCreatedDateAsc(pageable)).thenReturn(postPage);

        when(commentService.getComments(anyString(), anyLong())).thenReturn(Collections.emptyList());
        when(postLikeRepository.findByPostAndUser(any(Post.class), eq(user))).thenReturn(Optional.empty());

        // When
        List<PostResponse> result = postService.getPostsBySortType(oauthProviderId, PostSortType.LATEST, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(2L, result.get(0).getId()); // 가장 최근 게시글
        assertEquals(1L, result.get(1).getId());
        assertEquals(3L, result.get(2).getId());
    }

    @Test
    public void testGetPostsBySortType_POPULAR() {
        // Given
        String oauthProviderId = "test_oauth_id";
        when(userRepository.findByOauthProviderId(oauthProviderId)).thenReturn(Optional.of(user));

        List<Post> posts = Arrays.asList(
                createMockPost(1L, LocalDateTime.now().minusDays(1), 5),
                createMockPost(2L, LocalDateTime.now().minusHours(1), 15),
                createMockPost(3L, LocalDateTime.now().minusDays(2), 10)
        );

        // posts 리스트를 likes 기준 내림차순으로 정렬
        posts.sort(Comparator.comparing(Post::getLikes).reversed());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
        when(postRepository.findAllByOrderByLikesDesc(pageable)).thenReturn(postPage);

        when(commentService.getComments(anyString(), anyLong())).thenReturn(Collections.emptyList());
        when(postLikeRepository.findByPostAndUser(any(Post.class), eq(user))).thenReturn(Optional.empty());

        // When
        List<PostResponse> result = postService.getPostsBySortType(oauthProviderId, PostSortType.POPULAR, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(2L, result.get(0).getId()); // 좋아요 수가 가장 많은 게시글
        assertEquals(3L, result.get(1).getId());
        assertEquals(1L, result.get(2).getId());
    }

    @Test
    public void testGetPostsBySortType_LATEST_Pagination() {
        // Given
        String oauthProviderId = "test_oauth_id";
        when(userRepository.findByOauthProviderId(oauthProviderId)).thenReturn(Optional.of(user));

        List<Post> allPosts = new ArrayList<>();
        for (long i = 1; i <= 15; i++) {
            allPosts.add(createMockPost(i, LocalDateTime.now().minusMinutes(i), (int) i));
        }

        // 전체 posts를 createdDate 기준 내림차순으로 정렬
        allPosts.sort(Comparator.comparing(Post::getCreatedDate).reversed());

        // 첫 번째 페이지
        Pageable pageablePage0 = PageRequest.of(0, 10);
        List<Post> page0Posts = allPosts.subList(0, 10);
        Page<Post> postPage0 = new PageImpl<>(page0Posts, pageablePage0, allPosts.size());
        when(postRepository.findAllByOrderByCreatedDateAsc(pageablePage0)).thenReturn(postPage0);

        // 두 번째 페이지
        Pageable pageablePage1 = PageRequest.of(1, 10);
        List<Post> page1Posts = allPosts.subList(10, 15);
        Page<Post> postPage1 = new PageImpl<>(page1Posts, pageablePage1, allPosts.size());
        when(postRepository.findAllByOrderByCreatedDateAsc(pageablePage1)).thenReturn(postPage1);

        when(commentService.getComments(anyString(), anyLong())).thenReturn(Collections.emptyList());
        when(postLikeRepository.findByPostAndUser(any(Post.class), eq(user))).thenReturn(Optional.empty());

        // When - 첫 번째 페이지
        List<PostResponse> resultPage0 = postService.getPostsBySortType(oauthProviderId, PostSortType.LATEST, 0, 10);

        // Then
        assertNotNull(resultPage0);
        assertEquals(10, resultPage0.size());
        assertEquals(1L, resultPage0.get(0).getId());
        assertEquals(10L, resultPage0.get(9).getId());

        // When - 두 번째 페이지
        List<PostResponse> resultPage1 = postService.getPostsBySortType(oauthProviderId, PostSortType.LATEST, 1, 10);

        // Then
        assertNotNull(resultPage1);
        assertEquals(5, resultPage1.size());
        assertEquals(11L, resultPage1.get(0).getId());
        assertEquals(15L, resultPage1.get(4).getId());
    }

    private Post createMockPost(Long id, LocalDateTime createdDate, int likes) {
        // Post 객체를 모킹하여 필요한 메서드를 설정
        Post post = mock(Post.class);
        when(post.getId()).thenReturn(id);
        when(post.getCreatedDate()).thenReturn(createdDate);
        when(post.getLikes()).thenReturn(likes);
        when(post.getTitle()).thenReturn("Title " + id);
        when(post.getContent()).thenReturn("Content " + id);

        // User 객체를 모킹하여 Post에 설정
        User postUser = mock(User.class);
        when(postUser.getEmail()).thenReturn("user" + id + "@example.com");
        when(post.getUser()).thenReturn(postUser);

        return post;
    }
}