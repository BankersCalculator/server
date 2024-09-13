package com.myZipPlan.server.docs.community;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.community.controller.PostApiController;
import com.myZipPlan.server.community.dto.comment.CommentResponse;
import com.myZipPlan.server.community.dto.post.request.PostCreateRequest;
import com.myZipPlan.server.community.dto.post.request.PostUpdateRequest;
import com.myZipPlan.server.community.dto.post.response.PostResponse;
import com.myZipPlan.server.community.service.PostService;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    private final PostService postService = mock(PostService.class);
    private final LoanAdviceResultRepository loanAdviceResultRepository = mock(LoanAdviceResultRepository.class);


    private static final String BASE_URL = "/api/v1/post";

    @Override
    protected Object initController() {
        return new PostApiController(postService, loanAdviceResultRepository);
    }

    @DisplayName("게시글 목록 조회 API")
    @Test
    void getAllPosts() throws Exception {


        //PostResponse1
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("Test Loan Product")
                .loanProductCode("ABC123")
                .possibleLoanLimit(BigDecimal.valueOf(50000))
                .expectedLoanRate(BigDecimal.valueOf(3.5))
                .build();

        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .postId(1L)
                .author("오*환")
                .content("후... 금리 장난아니네요")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CommentResponse commentResponse2 = CommentResponse.builder()
                .id(2L)
                .postId(2L)
                .author("호*이")
                .content("어 혹시, 수도권 인건가요? 물권 정보 공유 받을 수 있을까요...?")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        List<CommentResponse> comments = new ArrayList<>();
        comments.add(commentResponse);
        comments.add(commentResponse2);

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("후, 지난주에 할걸그랬어요")
                .content("3개월 만에 0.5% 올라버림 ㅋ 지금이라도 사렵니다~~ 바로 은행가보려구요")
                .author("무지무지")
                .imageUrl("amazonS3ImageUrl")
                .avatarUrl("amazonS3Avataurl")
                .likes(5)
                .comments(comments)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("4시간 전")
                .build();

        //PostResponse2
        PostResponse response2 = PostResponse.builder()
                .id(2L)
                .title("이 앱 믿어도되나요?")
                .content("써보신분들 어떄요?")
                .author("궁금궁금이")
                .imageUrl(null)
                .avatarUrl(null)
                .likes(5)
                .comments(comments)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("30분 전")
                .build();

        List<PostResponse> posts = new ArrayList<>();
        posts.add(response);
        posts.add(response2);

        when(postService.getAllPosts())
                .thenReturn(posts);

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
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("status").description("응답 상태"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data[].id").description("게시글 ID"),
                                        fieldWithPath("data[].title").description("게시글 제목"),
                                        fieldWithPath("data[].content").description("게시글 내용"),
                                        fieldWithPath("data[].author").description("게시글 작성자"),
                                        fieldWithPath("data[].imageUrl").description("게시글 이미지 URL").optional(),
                                        fieldWithPath("data[].likes").description("게시글 좋아요 수"),
                                        fieldWithPath("data[].comments[].id").description("댓글 ID"),
                                        fieldWithPath("data[].comments[].postId").description("댓글이 달린 게시글 ID"),
                                        fieldWithPath("data[].comments[].author").description("댓글 작성자"),
                                        fieldWithPath("data[].comments[].content").description("댓글 내용"),
                                        fieldWithPath("data[].comments[].createdDate").description("댓글 생성 날짜"),
                                        fieldWithPath("data[].comments[].lastModifiedDate").description("댓글 수정 날짜"),
                                        fieldWithPath("data[].commentCount").description("댓글 수").optional(),
                                        fieldWithPath("data[].createdDate").description("게시글 생성 날짜"),
                                        fieldWithPath("data[].lastModifiedDate").description("게시글 수정 날짜"),
                                        fieldWithPath("data[].avatarUrl").description("작성자 아바타 URL").optional(),
                                        fieldWithPath("data[].timeAgo").description("게시글 작성 시간 경과 정보"),
                                        fieldWithPath("data[].loanAdviceSummaryReport.loanAdviceResultId").description("대출 상담 결과 ID"),
                                        fieldWithPath("data[].loanAdviceSummaryReport.loanProductName").description("대출 상품 이름"),
                                        fieldWithPath("data[].loanAdviceSummaryReport.loanProductCode").description("대출 상품 코드"),
                                        fieldWithPath("data[].loanAdviceSummaryReport.possibleLoanLimit").description("가능 대출 한도"),
                                        fieldWithPath("data[].loanAdviceSummaryReport.expectedLoanRate").description("예상 대출 금리")
                                )
                )
                );
    }

    @DisplayName("게시글 상세 조회 API")
    @Test
    void getPostById() throws Exception {
        //PostResponse1
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("Test Loan Product")
                .loanProductCode("ABC123")
                .possibleLoanLimit(BigDecimal.valueOf(50000))
                .expectedLoanRate(BigDecimal.valueOf(3.5))
                .build();

        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .postId(1L)
                .author("오*환")
                .content("후... 금리 장난아니네요")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CommentResponse commentResponse2 = CommentResponse.builder()
                .id(2L)
                .postId(2L)
                .author("호*이")
                .content("어 혹시, 수도권 인건가요? 물권 정보 공유 받을 수 있을까요...?")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        List<CommentResponse> comments = new ArrayList<>();
        comments.add(commentResponse);
        comments.add(commentResponse2);

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("후, 지난주에 할걸그랬어요")
                .content("3개월 만에 0.5% 올라버림 ㅋ 지금이라도 사렵니다~~ 바로 은행가보려구요")
                .author("무지무지")
                .imageUrl("amazonS3ImageUrl")
                .avatarUrl("amazonS3Avataurl")
                .likes(5)
                .comments(comments)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("4시간 전")
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
                                fieldWithPath("data.comments[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("data.comments[].postId").type(JsonFieldType.NUMBER).description("댓글이 달린 게시글 ID"),
                                fieldWithPath("data.comments[].author").type(JsonFieldType.STRING).description("댓글 작성자"),
                                fieldWithPath("data.comments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("data.comments[].createdDate").type(JsonFieldType.ARRAY).description("댓글 생성 날짜 [년, 월, 일, 시, 분, 초, 나노초]"),
                                fieldWithPath("data.comments[].lastModifiedDate").type(JsonFieldType.ARRAY).description("댓글 수정 날짜 [년, 월, 일, 시, 분, 초, 나노초]"),

                                fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일자").optional(),
                                fieldWithPath("data.lastModifiedDate").type(JsonFieldType.ARRAY).description("수정일자").optional(),
                                fieldWithPath("data.avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL").optional(),
                                fieldWithPath("data.timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지").optional(),
                                fieldWithPath("data.loanAdviceSummaryReport").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional(),
                                fieldWithPath("data.loanAdviceSummaryReport.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID").optional(),
                                fieldWithPath("data.loanAdviceSummaryReport.loanProductName").type(JsonFieldType.STRING).description("대출 상품명").optional(),
                                fieldWithPath("data.loanAdviceSummaryReport.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드").optional(),
                                fieldWithPath("data.loanAdviceSummaryReport.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도").optional(),
                                fieldWithPath("data.loanAdviceSummaryReport.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리").optional()
                        )
                ));
    }
    @DisplayName("게시글 목록 정렬 조회 API")
    @Test
    void getPostsBySortType() throws Exception {
        //PostResponse1
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("Test Loan Product")
                .loanProductCode("ABC123")
                .possibleLoanLimit(BigDecimal.valueOf(50000))
                .expectedLoanRate(BigDecimal.valueOf(3.5))
                .build();

        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .postId(1L)
                .author("오*환")
                .content("후... 금리 장난아니네요")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CommentResponse commentResponse2 = CommentResponse.builder()
                .id(2L)
                .postId(2L)
                .author("호*이")
                .content("어 혹시, 수도권 인건가요? 물권 정보 공유 받을 수 있을까요...?")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        List<CommentResponse> comments = new ArrayList<>();
        comments.add(commentResponse2);
        comments.add(commentResponse);


        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("후, 지난주에 할걸그랬어요")
                .content("3개월 만에 0.5% 올라버림 ㅋ 지금이라도 사렵니다~~ 바로 은행가보려구요")
                .author("무지무지")
                .imageUrl("amazonS3ImageUrl")
                .avatarUrl("amazonS3Avataurl")
                .likes(1)
                .comments(comments)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("4시간 전")
                .build();

        //PostResponse2
        PostResponse response2 = PostResponse.builder()
                .id(2L)
                .title("이 앱 믿어도되나요?")
                .content("써보신분들 어떄요?")
                .author("궁금궁금이")
                .imageUrl(null)
                .avatarUrl(null)
                .likes(10)
                .comments(comments)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("30분 전")
                .build();

        List<PostResponse> posts = new ArrayList<>();
        posts.add(response2);
        posts.add(response);


        when(postService.getPostsBySortType(any()))
                .thenReturn(posts);

        mockMvc.perform(get(BASE_URL + "/sorted")
                        .param("sortType", "POPULAR")
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
                                fieldWithPath("data[].likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("data[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional(),

                                fieldWithPath("data[].comments").type(JsonFieldType.ARRAY).description("댓글 목록").optional(),
                                fieldWithPath("data[].comments[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("data[].comments[].postId").type(JsonFieldType.NUMBER).description("댓글이 달린 게시글 ID"),
                                fieldWithPath("data[].comments[].author").type(JsonFieldType.STRING).description("댓글 작성자"),
                                fieldWithPath("data[].comments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("data[].comments[].createdDate").type(JsonFieldType.ARRAY).description("댓글 생성 날짜 [년, 월, 일, 시, 분, 초, 나노초]"),
                                fieldWithPath("data[].comments[].lastModifiedDate").type(JsonFieldType.ARRAY).description("댓글 수정 날짜 [년, 월, 일, 시, 분, 초, 나노초]"),

                                fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수").optional(),
                                fieldWithPath("data[].createdDate").type(JsonFieldType.ARRAY).description("작성일자").optional(),
                                fieldWithPath("data[].lastModifiedDate").type(JsonFieldType.ARRAY).description("수정일자").optional(),
                                fieldWithPath("data[].avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL").optional(),
                                fieldWithPath("data[].timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지").optional(),
                                fieldWithPath("data[].loanAdviceSummaryReport").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional(),
                                fieldWithPath("data[].loanAdviceSummaryReport.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID").optional(),
                                fieldWithPath("data[].loanAdviceSummaryReport.loanProductName").type(JsonFieldType.STRING).description("대출 상품명").optional(),
                                fieldWithPath("data[].loanAdviceSummaryReport.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드").optional(),
                                fieldWithPath("data[].loanAdviceSummaryReport.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도").optional(),
                                fieldWithPath("data[].loanAdviceSummaryReport.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리").optional()
                        )
                ));
    }
    @Test
    @DisplayName("게시글 생성 API")
    @WithMockUser(username = "testUser", roles = {"USER"})
    void createPost() throws Exception {
        // Mock PostCreateRequest 객체 생성
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("New Title");
        request.setContent("New Content");
        request.setLoanAdviceResultId(1L);

        // Mock PostResponse 객체 생성
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("Test Loan Product")
                .loanProductCode("ABC123")
                .possibleLoanLimit(BigDecimal.valueOf(50000))
                .expectedLoanRate(BigDecimal.valueOf(3.5))
                .build();

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("New Title")
                .content("New Content")
                .author("Test Author")
                .likes(0)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .build();

        // Mocking PostService의 createPost 메서드
        when(postService.createPost(any(PostCreateRequest.class), anyString()))
                .thenReturn(response);

        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(post("/api/v1/post")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document("post/create-post",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            requestFields(
                                    fieldWithPath("title").description("게시글 제목"),
                                    fieldWithPath("content").description("게시글 내용"),
                                    fieldWithPath("loanAdviceResultId").description("대출 상담 결과 ID"),
                                    fieldWithPath("imageFile").description("첨부 이미지 파일").optional() // imageFile 필드를 선택적으로 추가
                            ),
                            responseFields(
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("status").description("응답 상태"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("data.id").description("게시글 ID"),
                                    fieldWithPath("data.title").description("게시글 제목"),
                                    fieldWithPath("data.content").description("게시글 내용"),
                                    fieldWithPath("data.author").description("작성자"),
                                    fieldWithPath("data.imageUrl").description("이미지 URL").optional(),
                                    fieldWithPath("data.likes").description("좋아요 수"),
                                    fieldWithPath("data.comments").description("댓글 목록").optional(),
                                    fieldWithPath("data.commentCount").description("댓글 수").optional(),
                                    fieldWithPath("data.createdDate").description("작성일자").optional(),
                                    fieldWithPath("data.lastModifiedDate").description("수정일자").optional(),
                                    fieldWithPath("data.avatarUrl").description("작성자 아바타 URL").optional(),
                                    fieldWithPath("data.timeAgo").description("얼마 전에 작성되었는지").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport").description("대출 상담 결과").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanAdviceResultId").description("대출 상담 결과 ID"),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductName").description("대출 상품명"),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductCode").description("대출 상품 코드"),
                                    fieldWithPath("data.loanAdviceSummaryReport.possibleLoanLimit").description("가능한 대출 한도"),
                                    fieldWithPath("data.loanAdviceSummaryReport.expectedLoanRate").description("예상 대출 금리")
                            )
                    ));
        }
    }

    @Test
    @DisplayName("게시글 수정 API")
    void updatePost() throws Exception {
        // Mock PostUpdateRequest 객체 생성
        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        request.setLoanAdviceResultId(2L); // 새로운 LoanAdviceResult ID

        // Mock LoanAdviceSummaryResponse 객체 생성
        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(2L)
                .loanProductName("Updated Loan Product")
                .loanProductCode("DEF456")
                .possibleLoanLimit(BigDecimal.valueOf(70000))
                .expectedLoanRate(BigDecimal.valueOf(4.0))
                .build();

        // Mock PostResponse 객체 생성
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .author("Test Author")
                .imageUrl("newImageUrl")  // 새 이미지 URL
                .likes(5)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .build();

        // Mocking PostService의 updatePost 메서드
        when(postService.updatePost(anyString(), anyLong(), any(PostUpdateRequest.class)))
                .thenReturn(response);
        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");
            mockMvc.perform(put("/api/v1/post/{postId}", 1L)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
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
                            requestFields(
                                    fieldWithPath("title").description("수정된 게시글 제목"),
                                    fieldWithPath("content").description("수정된 게시글 내용"),
                                    fieldWithPath("loanAdviceResultId").description("새로운 LoanAdviceResult ID").optional(),
                                    fieldWithPath("existingImageUrl").description("기존 이미지 URL").optional(),
                                    fieldWithPath("imageFile").description("첨부 이미지 파일").optional() // imageFile 필드를 선택적으로 추가
                            ),
                            responseFields(
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("status").description("응답 상태"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("data.id").description("게시글 ID"),
                                    fieldWithPath("data.title").description("게시글 제목"),
                                    fieldWithPath("data.content").description("게시글 내용"),
                                    fieldWithPath("data.author").description("작성자"),
                                    fieldWithPath("data.likes").description("좋아요 수"),
                                    fieldWithPath("data.comments").description("댓글 목록").optional(),
                                    fieldWithPath("data.commentCount").description("댓글 수").optional(),
                                    fieldWithPath("data.createdDate").description("작성일자").optional(),
                                    fieldWithPath("data.lastModifiedDate").description("수정일자").optional(),
                                    fieldWithPath("data.avatarUrl").description("작성자 아바타 URL").optional(),
                                    fieldWithPath("data.timeAgo").description("얼마 전에 작성되었는지").optional(),
                                    fieldWithPath("data.imageUrl").description("새로운 이미지 URL"),
                                    fieldWithPath("data.loanAdviceSummaryReport").description("대출 상담 결과"),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanAdviceResultId").description("대출 상담 결과 ID"),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductName").description("대출 상품명"),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductCode").description("대출 상품 코드"),
                                    fieldWithPath("data.loanAdviceSummaryReport.possibleLoanLimit").description("가능한 대출 한도"),
                                    fieldWithPath("data.loanAdviceSummaryReport.expectedLoanRate").description("예상 대출 금리")
                            )
                    ));
        }
    }

    @Test
    @DisplayName("게시글 삭제 API")
    void deletePost() throws Exception {
        doNothing().when(postService).deletePost(any(String.class), anyLong());
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");
            mockMvc.perform(delete("/api/v1/post/{postId}", 1L)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(document("post/delete-post",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("postId").description("게시글 ID")
                            )
                    ));
        }

    }

    @Test
    @DisplayName("게시글 좋아요 API")
    void likePost() throws Exception {
        doNothing().when(postService).likePost(any(String.class), anyLong());
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");
            mockMvc.perform(post("/api/v1/post/{postId}/like", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(document("post/like-post",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("postId").description("게시글 ID")
                            )
                    ));
        }
    }

    @Test
    @DisplayName("게시글 좋아요 취소 API")
    void unlikePost() throws Exception {
        doNothing().when(postService).unlikePost(any(String.class), anyLong());
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");
            mockMvc.perform(post("/api/v1/post/{postId}/unlike", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰"))
                    .andExpect(status().isOk())
                    .andDo(document("post/unlike-post",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            pathParameters(
                                    parameterWithName("postId").description("게시글 ID")
                            )
                    ));
        }
    }

}
