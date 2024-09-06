package com.myZipPlan.server.docs;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.board.Service.BoardService;
import com.myZipPlan.server.board.cotroller.BoardApiController;
import com.myZipPlan.server.board.dto.BoardRequest;
import com.myZipPlan.server.board.dto.BoardResponse;
import com.myZipPlan.server.common.api.SliceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/board";
    private final BoardService boardService = mock(BoardService.class);

    @Override
    protected Object initController() {
        return new BoardApiController(boardService);
    }

    @DisplayName("게시글 목록 조회 API")
    @Test
    void getAllPosts() throws Exception {
        BoardResponse response = new BoardResponse();
        response.setId(1L);
        response.setTitle("Test Title");
        response.setContent("Test Content");
        response.setAuthor("Test Author");
        response.setCreatedDate(LocalDate.of(2024, 3, 20));
        response.setModifiedDate(LocalDate.of(2024, 3, 20));

        Pageable pageable = PageRequest.of(0, 10);
        List<BoardResponse> content = Arrays.asList(response);
        SliceResponse<BoardResponse> sliceResponse = SliceResponse.of(content, pageable, false);

        when(boardService.getAllPosts(any(Pageable.class)))
            .thenReturn(sliceResponse);

        mockMvc.perform(get(BASE_URL + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("board/get-all-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("size").description("페이지 크기")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY).description("게시글 목록"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.content[].author").type(JsonFieldType.STRING).description("작성자"),
                    fieldWithPath("data.content[].createdDate").type(JsonFieldType.ARRAY).description("작성일"),
                    fieldWithPath("data.content[].modifiedDate").type(JsonFieldType.ARRAY).description("수정일"),
                    fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                    fieldWithPath("data.isFirst").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                    fieldWithPath("data.isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                )
            ));
    }

    @DisplayName("게시글 상세 조회 API")
    @Test
    void getPostById() throws Exception {
        BoardResponse response = new BoardResponse();
        response.setId(1L);
        response.setTitle("Test Title");
        response.setContent("Test Content");
        response.setAuthor("Test Author");
        response.setCreatedDate(LocalDate.of(2024, 3, 20));
        response.setModifiedDate(LocalDate.of(2024, 3, 20));

        when(boardService.getPostById(any()))
            .thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("board/get-post-by-id",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("postId").description("게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일"),
                    fieldWithPath("data.modifiedDate").type(JsonFieldType.ARRAY).description("수정일")
                )
            ));
    }

    @DisplayName("게시글 생성 API")
    @Test
    void createPost() throws Exception {
        BoardRequest request = new BoardRequest();
        request.setTitle("New Title");
        request.setContent("New Content");
        request.setAuthor("New Author");

        BoardResponse response = new BoardResponse();
        response.setId(1L);
        response.setTitle("New Title");
        response.setContent("New Content");
        response.setAuthor("New Author");
        response.setCreatedDate(LocalDate.of(2024, 3, 20));
        response.setModifiedDate(LocalDate.of(2024, 3, 20));

        when(boardService.createPost(any()))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/posts")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("board/create-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("author").type(JsonFieldType.STRING).description("작성자")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일"),
                    fieldWithPath("data.modifiedDate").type(JsonFieldType.ARRAY).description("수정일")
                )
            ));
    }

    @DisplayName("게시글 수정 API")
    @Test
    void updatePost() throws Exception {
        BoardRequest request = new BoardRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        request.setAuthor("Updated Author");

        BoardResponse response = new BoardResponse();
        response.setId(1L);
        response.setTitle("Updated Title");
        response.setContent("Updated Content");
        response.setAuthor("Updated Author");
        response.setCreatedDate(LocalDate.of(2024, 3, 20));
        response.setModifiedDate(LocalDate.of(2024, 3, 21));

        when(boardService.updatePost(any(), any()))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/posts/{postId}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("board/update-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("postId").description("게시글 ID")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("author").type(JsonFieldType.STRING).description("작성자")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일"),
                    fieldWithPath("data.modifiedDate").type(JsonFieldType.ARRAY).description("수정일")
                )
            ));
    }

    @DisplayName("게시글 삭제 API")
    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("board/delete-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
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
}