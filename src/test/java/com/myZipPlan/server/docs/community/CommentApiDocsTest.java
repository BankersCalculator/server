package com.myZipPlan.server.docs.community;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.community.controller.CommentApiController;
import com.myZipPlan.server.community.dto.comment.*;
import com.myZipPlan.server.community.service.CommentService;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class CommentApiDocsTest extends RestDocsSupport {
    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentApiController(commentService);
    }


    @Test
    @DisplayName("댓글 작성 API")
    void createComment() throws Exception {
        // Mock CommentCreateRequest 객체 생성
        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("New Comment Content");

        // Mock CommentResponse 객체 생성
        CommentResponse response = CommentResponse.builder()
                .id(1L)
                .postId(10L)
                .author("Test Author")
                .content("New Comment Content")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .like(false)
                .likes(0)
                .timeAgo("방금 전")
                .avatarUrl("kakaoUrl")
                .updateDeleteAuthority("N")
                .build();

        // Mocking CommentService의 addComment 메서드
        when(commentService.createComment(anyString(), anyLong(), any(CommentCreateRequest.class)))
                .thenReturn(response);

        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(post("/api/v1/comment/{postId}", 10L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document("comment/create-comment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("postId").description("댓글을 작성할 게시글 ID")
                            ),
                            requestFields(
                                    fieldWithPath("content").description("댓글 내용")
                            ),
                            responseFields(
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("status").description("응답 상태"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("data.id").description("댓글 ID"),
                                    fieldWithPath("data.postId").description("댓글이 달린 게시글 ID"),
                                    fieldWithPath("data.author").description("댓글 작성자 ID"),
                                    fieldWithPath("data.content").description("작성된 댓글 내용"),
                                    fieldWithPath("data.createdDate").description("댓글 작성일자").optional(),
                                    fieldWithPath("data.lastModifiedDate").description("댓글 수정일자").optional(),
                                    fieldWithPath("data.like").description("유저 댓글 좋아요 여부"),
                                    fieldWithPath("data.likes").description("유저 댓글 좋아요 수"),
                                    fieldWithPath("data.timeAgo").description("얼마 전에 작성되었는지"),
                                    fieldWithPath("data.avatarUrl").description("작성자 아바타 URL").optional(),
                                    fieldWithPath("data.updateDeleteAuthority").description("댓글 수정/삭제권한").optional()

                            )
                    ));
        }
    }



    @Test
    @DisplayName("댓글 수정 API")
    void updateComment() throws Exception {
        // Mock CommentUpdateRequest 객체 생성
        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setUpdatedContent("Updated Comment Content");

        // Mock CommentResponse 객체 생성
        CommentResponse response = CommentResponse.builder()
                .id(1L)
                .postId(10L)
                .author("Test Author")
                .content("Updated Comment Content")
                .createdDate(LocalDateTime.now().minusDays(1))
                .lastModifiedDate(LocalDateTime.now())
                .like(true)
                .likes(3)
                .timeAgo("1일 전")
                .avatarUrl("kakaoUrl")
                .updateDeleteAuthority("ALL")
                .build();

        // Mocking CommentService의 updateComment 메서드
        when(commentService.updateComment(anyString(), anyLong(), any(CommentUpdateRequest.class)))
                .thenReturn(response);

        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(put("/api/v1/comment/{commentId}", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document("comment/update-comment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("commentId").description("댓글 ID")
                            ),
                            requestFields(
                                    fieldWithPath("updatedContent").description("수정된 댓글 내용")
                            ),
                            responseFields(
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("status").description("응답 상태"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("data.id").description("댓글 ID"),
                                    fieldWithPath("data.postId").description("댓글이 달린 게시글 ID"),
                                    fieldWithPath("data.author").description("댓글 작성자 ID"),
                                    fieldWithPath("data.content").description("수정된 댓글 내용"),
                                    fieldWithPath("data.createdDate").description("댓글 작성일자").optional(),
                                    fieldWithPath("data.lastModifiedDate").description("댓글 수정일자").optional(),
                                    fieldWithPath("data.like").description("유저 댓글 좋아요 여부"),
                                    fieldWithPath("data.likes").description("유저 댓글 좋아요 수"),
                                    fieldWithPath("data.timeAgo").description("얼마 전에 작성되었는지"),
                                    fieldWithPath("data.avatarUrl").description("작성자 아바타 URL").optional(),
                                    fieldWithPath("data.updateDeleteAuthority").description("댓글 수정/삭제권한").optional()
                            )
                    ));
        }
    }

    @Test
    @DisplayName("댓글 삭제 API")
    void deleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(any(String.class), anyLong());

        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/comment/{commentId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(document("comment/delete-comment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("commentId").description("댓글 ID")
                            )
                    ));
        }
    }

    @Test
    @DisplayName("댓글 좋아요 API")
    void likeComment() throws Exception {
        doNothing().when(commentService).likeComment(any(String.class), anyLong());

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comment/{commentId}/like", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(document("comment/like-comment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("commentId").description("댓글 ID")
                            )
                    ));
        }
    }

    @Test
    @DisplayName("댓글 좋아요 취소 API")
    void unlikeComment() throws Exception {
        doNothing().when(commentService).unlikeComment(any(String.class), anyLong());

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");
            mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comment/{commentId}/unlike", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(document("comment/unlike-comment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("commentId").description("댓글 ID")
                            )
                    ));
        }
    }

}
