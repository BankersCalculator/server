package com.myZipPlan.server.docs.community;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.community.controller.PostApiController;
import com.myZipPlan.server.community.dto.post.*;
import com.myZipPlan.server.community.enums.PostSortType;
import com.myZipPlan.server.community.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class PostApiDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/post";
    private final PostService postService = mock(PostService.class);

    @Override
    protected Object initController() {
        return new PostApiController(postService);
    }

    @Test
    @DisplayName("게시글 작성 API 문서화 테스트")
    void addPost() throws Exception {
        AddPostRequest request = new AddPostRequest();
        request.setTitle("게시글 제목");
        request.setContent("게시글 내용");

        when(postService.addPost(request, 1L)).thenReturn(null);

        mockMvc.perform(post(BASE_URL + "?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"게시글 제목\",\"content\":\"게시글 내용\"}"))
                .andExpect(status().isOk())
                .andDo(document("post-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("lastModifiedDate").type(JsonFieldType.STRING).description("수정일"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정 API 문서화 테스트")
    void updatePost() throws Exception {
        UpdatePostRequest request = new UpdatePostRequest("제목", "내용", null);
        request.setTitle("수정된 제목");
        request.setContent("수정된 내용");

        when(postService.updatePost(1L, 1L, request)).thenReturn(null);

        mockMvc.perform(put(BASE_URL + "/1?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"수정된 제목\",\"content\":\"수정된 내용\"}"))
                .andExpect(status().isOk())
                .andDo(document("post-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정된 게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정된 게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 게시글 내용"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("lastModifiedDate").type(JsonFieldType.STRING).description("수정일"),
                                fieldWithPath("likes").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 삭제 API 문서화 테스트")
    void deletePost() throws Exception {
        DeletePostRequest request = new DeletePostRequest(1L);

        mockMvc.perform(delete(BASE_URL + "/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1}"))
                .andExpect(status().isOk())
                .andDo(document("post-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("postId").description("삭제할 게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("게시글을 삭제하는 사용자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 좋아요 API 문서화 테스트")
    void likePost() throws Exception {
        LikePostRequest request = new LikePostRequest(1L);

        mockMvc.perform(post(BASE_URL + "/1/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1}"))
                .andExpect(status().isOk())
                .andDo(document("post-like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("좋아요를 누른 사용자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 좋아요 취소 API 문서화 테스트")
    void unlikePost() throws Exception {
        LikePostRequest request = new LikePostRequest(1L);

        mockMvc.perform(post(BASE_URL + "/1/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1}"))
                .andExpect(status().isOk())
                .andDo(document("post-unlike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("좋아요 취소를 한 사용자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 목록 조회 (최신순) API 문서화 테스트")
    void getPostsByLatest() throws Exception {
        PostSortRequest request = new PostSortRequest(PostSortType.LATEST);

        mockMvc.perform(get(BASE_URL + "/sorted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sortType\":\"LATEST\"}"))
                .andExpect(status().isOk())
                .andDo(document("post-list-latest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("sortType").description("정렬 방식 (LATEST: 최신순, POPULAR: 인기순)")
                        ),
                        responseFields(
                                fieldWithPath("[]").description("게시글 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("[].lastModifiedDate").type(JsonFieldType.STRING).description("수정일"),
                                fieldWithPath("[].likes").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )
                ));
    }

}
