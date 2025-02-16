package com.myZipPlan.server.docs;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.user.dto.GuestToUserTransferRequest;
import com.myZipPlan.server.user.userService.UserService;
import com.myZipPlan.server.user.controller.UserApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/user";
    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserApiController(userService);
    }

    @DisplayName("일회성 고객의 데이터를 기존 고객으로 편입")
    @Test
    void transferTempUserToLoginUser() throws Exception {
        // given
        GuestToUserTransferRequest content = new GuestToUserTransferRequest("guest");

        mockMvc.perform(post(BASE_URL + "/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(content))
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user/transfer-temp-user-to-login-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("guestToken").type(JsonFieldType.STRING).description("게스트유저의 토큰")
                )
            ));
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        // given
        mockMvc.perform(post(BASE_URL + "/logout")
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user/logout",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                )
            ));
    }

    @DisplayName("회원탈퇴")
    @Test
    void withdraw() throws Exception {
        mockMvc.perform(post(BASE_URL + "/withdraw")
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user/withdraw",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                )
            ));
    }

}