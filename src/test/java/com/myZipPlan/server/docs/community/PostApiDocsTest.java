package com.myZipPlan.server.docs.community;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.community.controller.PostApiController;
import com.myZipPlan.server.common.enums.community.PostSortType;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.post.request.PostCreateRequest;
import com.myZipPlan.server.community.dto.post.request.PostSortRequest;
import com.myZipPlan.server.community.dto.post.request.PostUpdateRequest;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class PostApiDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/post";
    private final PostService postService = mock(PostService.class);
    private final LoanAdviceResultRepository loanAdviceResultRepository = mock(LoanAdviceResultRepository.class);

    @Override
    protected Object initController() {
        return new PostApiController(postService, loanAdviceResultRepository);
    }

    @DisplayName("게시글 목록 조회 API")
    @Test
    void getAllPosts() throws Exception {
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .author("Test Author")
                .likes(5)
                .build();

        when(postService.getAllPosts())
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("AccessToken", "액세스 토큰"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/get-all-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("게시글 목록"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("data[].author").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("data[].likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("data[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional(),
                                fieldWithPath("data[].comments").type(JsonFieldType.ARRAY).description("댓글 목록").optional(),
                                fieldWithPath("data[].createdDate").type(JsonFieldType.STRING).description("작성일자").optional(),
                                fieldWithPath("data[].lastModifiedDate").type(JsonFieldType.STRING).description("수정일자").optional(),
                                fieldWithPath("data[].avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL").optional(),
                                fieldWithPath("data[].timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지").optional(),
                                fieldWithPath("data[].loanAdviceResult").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional()
                        )
                ));
    }

    @DisplayName("게시글 상세 조회 API")
    @Test
    void getPostById() throws Exception {
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .author("Test Author")
                .likes(5)
                .build();

        when(postService.getPostById(any()))
                .thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("AccessToken", "액세스 토큰"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/get-post-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional(),
                                fieldWithPath("data.comments").type(JsonFieldType.ARRAY).description("댓글 목록").optional(),
                                fieldWithPath("data.createdDate").type(JsonFieldType.STRING).description("작성일자").optional(),
                                fieldWithPath("data.lastModifiedDate").type(JsonFieldType.STRING).description("수정일자").optional(),
                                fieldWithPath("data.avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL").optional(),
                                fieldWithPath("data.timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지").optional(),
                                fieldWithPath("data.loanAdviceResult").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional()
                        )
                ));
    }
    /*

    @DisplayName("게시글 생성 API")
    @Test
    void createPost() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("New Title");
        request.setContent("New Content");

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("New Title")
                .content("New Content")
                .author("New Author")
                .likes(0)
                .build();

        when(postService.createPost(any(), any()))
                .thenReturn(new Post());

        mockMvc.perform(multipart(BASE_URL)
                        .file("imageFile", "test image".getBytes())
                        .param("title", "New Title")
                        .param("content", "New Content")
                        .header("AccessToken", "액세스 토큰")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/create-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        requestParameters(
                                parameterWithName("title").description("게시글 제목"),
                                parameterWithName("content").description("게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )
                ));
    }

    @DisplayName("게시글 수정 API")
    @Test
    void updatePost() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .author("Updated Author")
                .likes(0)
                .build();

        when(postService.updatePost(any(), any(), any()))
                .thenReturn(new Post());

        mockMvc.perform(multipart(BASE_URL + "/{postId}", 1L)
                        .file("imageFile", "updated image".getBytes())
                        .param("title", "Updated Title")
                        .param("content", "Updated Content")
                        .header("AccessToken", "액세스 토큰")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/update-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestParameters(
                                parameterWithName("title").description("게시글 제목"),
                                parameterWithName("content").description("게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )
                ));
    }

    @DisplayName("게시글 삭제 API")
    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("AccessToken", "액세스 토큰"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/delete-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @DisplayName("게시글 목록 정렬 조회 API")
    @Test
    void getPostsBySortType() throws Exception {
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .author("Test Author")
                .likes(10)
                .build();

        when(postService.getPostsBySortType(any()))
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get(BASE_URL + "/sorted")
                        .param("sortType", "POPULAR")  // 정렬 타입: LATEST, POPULAR
                        .header("AccessToken", "액세스 토큰")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/get-sorted-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        queryParameters(
                                parameterWithName("sortType").description("정렬 타입: LATEST(최신순), POPULAR(인기순)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("게시글 목록"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("data[].author").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("data[].likes").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )
                ));
    }

    @DisplayName("게시글 좋아요 API")
    @Test
    void likePost() throws Exception {
        mockMvc.perform(post(BASE_URL + "/{postId}/like", 1L)
                        .header("AccessToken", "액세스 토큰")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/like-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("좋아요를 누를 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @DisplayName("게시글 좋아요 취소 API")
    @Test
    void unlikePost() throws Exception {
        mockMvc.perform(post(BASE_URL + "/{postId}/unlike", 1L)
                        .header("AccessToken", "액세스 토큰")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/unlike-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("AccessToken").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("좋아요를 취소할 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
     */

}
