package com.myZipPlan.server.docs.community;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.community.controller.CommentApiController;
import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.dto.comment.*;
import com.myZipPlan.server.community.service.CommentService;
import com.myZipPlan.server.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class CommentApiDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/comment";
    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentApiController(commentService);
    }

    @Test
    @DisplayName("댓글 작성 API 문서화 테스트")
    void addComment() throws Exception {
        AddCommentRequest request = new AddCommentRequest();
        request.setUserId(1L);
        request.setContent("댓글 내용");

        // Create mock User using factory method
        User mockUser = User.create("testProvider", "testProviderId", "test@example.com", RoleType.USER);

        // Mocking Post using builder
        Post mockPost = Post.builder()
                .title("Test Post Title")
                .content("Test Post Content")
                .user(mockUser)  // Set the user for Post
                .likes(0)
                .build();

        // Mocking Comment
        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setContent("댓글 내용");
        mockComment.setPost(mockPost);  // Set Post
        mockComment.setUser(mockUser);  // Set User
        mockComment.setCreatedDate(LocalDateTime.now());
        mockComment.setLastModifiedDate(LocalDateTime.now());

        when(commentService.addComment(1L, request)).thenReturn(CommentResponse.fromEntity(mockComment));

        mockMvc.perform(post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"content\":\"댓글 내용\"}"))
                .andExpect(status().isOk())
                .andDo(print()) // 응답을 출력하여 확인합니다.
                .andDo(document("comment-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").optional().type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("data.content").optional().type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("data.userId").optional().type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("data.createdDate").optional().type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("data.lastModifiedDate").optional().type(JsonFieldType.STRING).description("수정일")
                        )
                ));

    }



    @Test
    @DisplayName("댓글 수정 API 문서화 테스트")
    void updateComment() throws Exception {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUpdatedContent("수정된 댓글 내용");
        when(commentService.updateComment("oauthPrividerId",1L, request)).thenReturn(null);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"updatedContent\":\"수정된 댓글 내용\"}"))
                .andExpect(status().isOk())
                .andDo(document("comment-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("댓글 작성자 ID"),
                                fieldWithPath("updatedContent").type(JsonFieldType.STRING).description("수정된 댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 댓글 내용"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("lastModifiedDate").type(JsonFieldType.STRING).description("수정일")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 삭제 API 문서화 테스트")
    void deleteComment() throws Exception {
        DeleteCommentRequest request = new DeleteCommentRequest(1L);

        mockMvc.perform(delete(BASE_URL + "/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1}"))
                .andExpect(status().isOk())
                .andDo(document("comment-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("commentId").description("삭제할 댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("댓글을 삭제하는 사용자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 좋아요 API 문서화 테스트")
    void likeComment() throws Exception {


        mockMvc.perform(post(BASE_URL + "/1/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1}"))
                .andExpect(status().isOk())
                .andDo(document("comment-like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("좋아요를 누른 사용자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 좋아요 취소 API 문서화 테스트")
    void unlikeComment() throws Exception {
         mockMvc.perform(post(BASE_URL + "/1/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1}"))
                .andExpect(status().isOk())
                .andDo(document("comment-unlike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("좋아요 취소를 한 사용자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("대댓글 작성 API 문서화 테스트")
    void addReply() throws Exception {
        AddReplyRequest request = new AddReplyRequest(1L, "대댓글 내용");

        when(commentService.addReply(1L, request)).thenReturn(null);

        mockMvc.perform(post(BASE_URL + "/1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"content\":\"대댓글 내용\"}"))
                .andExpect(status().isOk())
                .andDo(document("comment-reply-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글 내용"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("작성일"),
                                fieldWithPath("lastModifiedDate").type(JsonFieldType.STRING).description("수정일")
                        )
                ));
    }
}
