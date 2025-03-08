package com.myZipPlan.server.docs;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.user.controller.GuestApiController;
import com.myZipPlan.server.user.userService.GuestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GuestApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/guest";
    private final GuestService guestService = mock(GuestService.class);

    @Override
    protected Object initController() {
        return new GuestApiController(guestService);
    }

    @DisplayName("일회성 고객 토큰 발급")
    @Test
    void loginGuest() throws Exception {

        TokenDto response = TokenDto.builder().accessToken("hi").refreshToken("bye").roleType(RoleType.GUEST).build();
        when(guestService.registerGuest()).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("guest/login-guest",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("게스트유저의 토큰"),
                    fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("게스트유저의 리프레시 토큰(미사용)"),
                    fieldWithPath("data.roleType").type(JsonFieldType.STRING).description("유저의 권한")
                )
            ));
    }
}