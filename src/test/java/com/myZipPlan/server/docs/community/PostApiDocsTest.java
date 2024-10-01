package com.myZipPlan.server.docs.community;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.common.enums.community.PostSortType;
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
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;


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


    private static final String BASE_URL = "/api/v1/post";

    @Override
    protected Object initController() {
        return new PostApiController(postService);
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
                .timeAgo("방금 전")
                .avatarUrl("kakaoUrl")
                .build();

        CommentResponse commentResponse2 = CommentResponse.builder()
                .id(2L)
                .postId(2L)
                .author("호*이")
                .content("어 혹시, 수도권 인건가요? 물권 정보 공유 받을 수 있을까요...?")
                .createdDate(LocalDateTime.now().minusMonths(2))
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("2개월 전")
                .avatarUrl("kakaoUrl")
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
                .createdDate(LocalDateTime.now().minusYears(1))
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("1년 전")
                .like(true)
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
                .like(false)
                .build();

        List<PostResponse> posts = new ArrayList<>();
        posts.add(response);
        posts.add(response2);

        when(postService.getAllPosts(anyString(), anyInt(), anyInt()))
                .thenReturn(posts);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "10")
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
                                    queryParameters(
                                            parameterWithName("page").description("페이지 번호, 기본값 : 0").optional(),
                                            parameterWithName("size").description("페이지 당 게시글 수, 기본값 : 10").optional()
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
                                            fieldWithPath("data[].comments[].like").type(JsonFieldType.BOOLEAN).description("댓글 좋아요 여부"),
                                            fieldWithPath("data[].comments[].likes").type(JsonFieldType.NUMBER).description("댓글 좋아요 수"),
                                            fieldWithPath("data[].comments[].timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지"),
                                            fieldWithPath("data[].comments[].avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL"),


                                            fieldWithPath("data[].commentCount").description("댓글 수").optional(),
                                            fieldWithPath("data[].createdDate").description("게시글 생성 날짜"),
                                            fieldWithPath("data[].lastModifiedDate").description("게시글 수정 날짜"),
                                            fieldWithPath("data[].avatarUrl").description("작성자 아바타 URL").optional(),
                                            fieldWithPath("data[].timeAgo").description("게시글 작성 시간 경과 정보"),
                                            fieldWithPath("data[].like").description("유저 게시글 좋아요 여부"),
                                            fieldWithPath("data[].loanAdviceSummaryReport.loanAdviceResultId").description("대출 상담 결과 ID"),
                                            fieldWithPath("data[].loanAdviceSummaryReport.loanProductName").description("대출 상품 이름"),
                                            fieldWithPath("data[].loanAdviceSummaryReport.loanProductCode").description("대출 상품 코드"),
                                            fieldWithPath("data[].loanAdviceSummaryReport.possibleLoanLimit").description("가능 대출 한도"),
                                            fieldWithPath("data[].loanAdviceSummaryReport.expectedLoanRate").description("예상 대출 금리")
                                    )
                            )
                    );
        }
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
                .like(true)
                .timeAgo("방금 전")
                .avatarUrl("kakaoUrl")
                .build();

        CommentResponse commentResponse2 = CommentResponse.builder()
                .id(2L)
                .postId(2L)
                .author("호*이")
                .content("어 혹시, 수도권 인건가요? 물권 정보 공유 받을 수 있을까요...?")
                .createdDate(LocalDateTime.now().minusDays(5))
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("5일 전")
                .like(true)
                .avatarUrl("kakaoUrl")
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
                .createdDate(LocalDateTime.now().minusMonths(1))
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("1일 전")
                .like(true)
                .build();

         when(postService.getPostById(anyString(), any()))
                .thenReturn(response);
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

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
                                    fieldWithPath("data.comments[].like").type(JsonFieldType.BOOLEAN).description("댓글 좋아요 여부"),
                                    fieldWithPath("data.comments[].likes").type(JsonFieldType.NUMBER).description("댓글 좋아요 수"),
                                    fieldWithPath("data.comments[].timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지"),
                                    fieldWithPath("data.comments[].avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL"),

                                    fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                    fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일자").optional(),
                                    fieldWithPath("data.lastModifiedDate").type(JsonFieldType.ARRAY).description("수정일자").optional(),
                                    fieldWithPath("data.avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL").optional(),
                                    fieldWithPath("data.timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지").optional(),
                                    fieldWithPath("data.like").description("유저 게시글 좋아요 여부"),

                                    fieldWithPath("data.loanAdviceSummaryReport").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductName").type(JsonFieldType.STRING).description("대출 상품명").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리").optional()
                            )
                    ));
        }
    }
    @DisplayName("게시글 목록 정렬 조회 API")
    @Test
    void getPostsBySortType() throws Exception {
        // PostResponse1
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
                .timeAgo("방금 전")
                .avatarUrl("kakaoUrl")
                .build();

        CommentResponse commentResponse2 = CommentResponse.builder()
                .id(2L)
                .postId(2L)
                .author("호*이")
                .content("어 혹시, 수도권 인건가요? 물권 정보 공유 받을 수 있을까요...?")
                .createdDate(LocalDateTime.now().minusMinutes(5))
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("5분 전")
                .avatarUrl("kakaoUrl")
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
                .avatarUrl("amazonS3AvatarUrl")
                .likes(1)
                .comments(comments)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now().minusHours(4))
                .timeAgo("4시간 전")
                .like(true)
                .build();

        // PostResponse2
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
                .createdDate(LocalDateTime.now().minusMinutes(30))
                .lastModifiedDate(LocalDateTime.now())
                .timeAgo("30분 전")
                .build();

        List<PostResponse> posts = new ArrayList<>();
        posts.add(response2);
        posts.add(response);

        when(postService.getPostsBySortType(anyString(), any(PostSortType.class), anyInt(), anyInt()))
                .thenReturn(posts);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(get(BASE_URL + "/sorted")
                            .param("sortType", "POPULAR")
                            .param("page", "0")
                            .param("size", "10")
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
                                    parameterWithName("sortType").description("정렬 타입: LATEST(최신순), POPULAR(인기순)"),
                                    parameterWithName("page").description("페이지 번호, 기본값 : 0").optional(),
                                    parameterWithName("size").description("페이지 당 게시글 수, 기본값 : 10").optional()
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
                                    fieldWithPath("data[].comments[].like").type(JsonFieldType.BOOLEAN).description("댓글 좋아요 여부"),
                                    fieldWithPath("data[].comments[].likes").type(JsonFieldType.NUMBER).description("댓글 좋아요 수"),
                                    fieldWithPath("data[].comments[].timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지"),
                                    fieldWithPath("data[].comments[].avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL"),

                                    fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수").optional(),
                                    fieldWithPath("data[].createdDate").type(JsonFieldType.ARRAY).description("작성일자").optional(),
                                    fieldWithPath("data[].lastModifiedDate").type(JsonFieldType.ARRAY).description("수정일자").optional(),
                                    fieldWithPath("data[].avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL").optional(),
                                    fieldWithPath("data[].timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지").optional(),

                                    fieldWithPath("data[].like").type(JsonFieldType.BOOLEAN).description("유저 게시글 좋아요 여부"),

                                    fieldWithPath("data[].loanAdviceSummaryReport").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional(),
                                    fieldWithPath("data[].loanAdviceSummaryReport.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID").optional(),
                                    fieldWithPath("data[].loanAdviceSummaryReport.loanProductName").type(JsonFieldType.STRING).description("대출 상품명").optional(),
                                    fieldWithPath("data[].loanAdviceSummaryReport.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드").optional(),
                                    fieldWithPath("data[].loanAdviceSummaryReport.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도").optional(),
                                    fieldWithPath("data[].loanAdviceSummaryReport.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리").optional()
                            )
                    ));
        }
    }

    @Test
    @DisplayName("게시글 생성 API")
    @WithMockUser(username = "testUser", roles = {"USER"})
    void createPost() throws Exception {
        // Mock PostCreateRequest 객체 생성
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("테스트 제목");
        request.setContent("테스트 내용");
        request.setLoanAdviceResultId(1L); // loanAdviceResultId는 null로 설정

        LoanAdviceSummaryResponse loanAdviceSummaryResponse = LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("Test Loan Product")
                .loanProductCode("ABC123")
                .possibleLoanLimit(BigDecimal.valueOf(50000))
                .expectedLoanRate(BigDecimal.valueOf(3.5))
                .build();
        // 이미지 파일은 MultipartFile로 처리하므로 여기서는 생략

        // Mock PostResponse 객체 생성
        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .author("testUser@example.com")
                .imageUrl("https://myzipplan-service-storage.s3.ap-northeast-2.amazonaws.com/unique-id.png")
                .likes(0)
                .comments(Collections.emptyList())
                .createdDate(LocalDateTime.of(2024, 9, 15, 16, 0, 43))
                .lastModifiedDate(LocalDateTime.of(2024, 9, 15, 16, 0, 43))
                .avatarUrl("카카오프로필")
                .timeAgo("0분 전")
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .like(false)
                .build();

        // Mocking PostService의 createPost 메서드
        when(postService.createPost(any(PostCreateRequest.class), anyString()))
                .thenReturn(response);

        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            mockMvc.perform(multipart("/api/v1/post")
                            .file("imageFile", "test-image-content".getBytes())
                            .part(new MockPart("title", "테스트 제목".getBytes(StandardCharsets.UTF_8))) // title as part
                            .part(new MockPart("content", "테스트 내용".getBytes(StandardCharsets.UTF_8))) // content as part
                            .part(new MockPart("loanAdviceResultId", "1".getBytes(StandardCharsets.UTF_8))) // content as part
                            .contentType(MediaType.MULTIPART_FORM_DATA)
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
                            requestParts(
                                    partWithName("imageFile").description("첨부 이미지 파일").optional(),
                                    partWithName("title").description("게시글 제목"),
                                    partWithName("content").description("게시글 내용"),
                                    partWithName("loanAdviceResultId").description("대출 상담 결과 ID").optional()
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                    fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자 이메일"),
                                    fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional(),
                                    fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                    fieldWithPath("data.comments").type(JsonFieldType.ARRAY).description("댓글 목록").optional(),
                                    fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                    fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일자 [년, 월, 일, 시, 분, 초, 나노초]"),
                                    fieldWithPath("data.lastModifiedDate").type(JsonFieldType.ARRAY).description("수정일자 [년, 월, 일, 시, 분, 초, 나노초]"),
                                    fieldWithPath("data.avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL"),
                                    fieldWithPath("data.timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지"),

                                    fieldWithPath("data.like").description("유저 게시글 좋아요 여부"),

                                    fieldWithPath("data.loanAdviceSummaryReport").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductName").type(JsonFieldType.STRING).description("대출 상품명").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리").optional()
                            )
                    ));
        }
    }

    @Test
    @DisplayName("게시글 수정 API")
    @WithMockUser(username = "testUser", roles = {"USER"})
    void updatePost() throws Exception {
        // Mock PostUpdateRequest 객체 생성
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest();
        postUpdateRequest.setTitle("수정된 제목");
        postUpdateRequest.setContent("수정된 내용");
        postUpdateRequest.setLoanAdviceResultId(2L); // 새로운 LoanAdviceResult ID

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
                .title("수정된 제목")
                .content("수정된 내용")
                .author("testUser@example.com")
                .imageUrl("https://myzipplan-service-storage.s3.ap-northeast-2.amazonaws.com/updated-image-url.png")
                .likes(5)
                .comments(Collections.emptyList())
                .createdDate(LocalDateTime.of(2024, 9, 15, 16, 0, 43))
                .lastModifiedDate(LocalDateTime.of(2024, 9, 15, 16, 5, 43))
                .avatarUrl("카카오프로필")
                .timeAgo("5분 전")
                .like(false)
                .loanAdviceSummaryReport(loanAdviceSummaryResponse)
                .build();

        // Mocking PostService의 updatePost 메서드
        when(postService.updatePost(anyString(), anyLong(), any(PostUpdateRequest.class)))
                .thenReturn(response);

        // Mock SecurityUtils.getProviderId()
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getProviderId).thenReturn("mockedProviderId");

            // PUT 메서드로 multipart 요청을 보냄
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/post/{postId}", 1L)
                            .file("imageFile", "updated-image-content".getBytes()) // 이미지 파일
                            .part(new MockPart("title", "수정된 제목".getBytes(StandardCharsets.UTF_8))) // 제목
                            .part(new MockPart("content", "수정된 내용".getBytes(StandardCharsets.UTF_8))) // 내용
                            .part(new MockPart("existingImageUrl", "기존 이미지 URL".getBytes(StandardCharsets.UTF_8)))
                            .param("loanAdviceResultId", "2") // 새로운 LoanAdviceResult ID 추가
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("AccessToken", "액세스 토큰")
                            .with(request -> {
                                request.setMethod("PUT"); // PUT 메서드로 설정
                                return request;
                            }))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document("post/update-post",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("AccessToken").description("액세스 토큰")
                            ),
                            requestParts(
                                    partWithName("imageFile").description("첨부 이미지 파일").optional(),
                                    partWithName("title").description("수정된 게시글 제목"),
                                    partWithName("content").description("수정된 게시글 내용"),
                                    partWithName("existingImageUrl").description("기존 이미지 URL").optional(),
                                    partWithName("loanAdviceResultId").description("대출 상담 결과 ID").optional()
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                    fieldWithPath("data.author").type(JsonFieldType.STRING).description("작성자 이메일"),
                                    fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional(),
                                    fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                    fieldWithPath("data.comments").type(JsonFieldType.ARRAY).description("댓글 목록").optional(),
                                    fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                    fieldWithPath("data.createdDate").type(JsonFieldType.ARRAY).description("작성일자 [년, 월, 일, 시, 분, 초, 나노초]"),
                                    fieldWithPath("data.lastModifiedDate").type(JsonFieldType.ARRAY).description("수정일자 [년, 월, 일, 시, 분, 초, 나노초]"),
                                    fieldWithPath("data.avatarUrl").type(JsonFieldType.STRING).description("작성자 아바타 URL"),
                                    fieldWithPath("data.timeAgo").type(JsonFieldType.STRING).description("얼마 전에 작성되었는지"),
                                    fieldWithPath("data.like").description("유저 게시글 좋아요 여부"),
                                    fieldWithPath("data.loanAdviceSummaryReport").type(JsonFieldType.OBJECT).description("대출 상담 결과").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductName").type(JsonFieldType.STRING).description("대출 상품명").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도").optional(),
                                    fieldWithPath("data.loanAdviceSummaryReport.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리").optional()
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
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
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
