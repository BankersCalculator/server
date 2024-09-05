package com.myZipPlan.server.common.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/v1/exception")
@RestController
public class ExceptionController {

    @GetMapping("/entry-point")
    public void authenticateException() {
        throw new AccessDeniedException("접근 불가능한 요청입니다.");
    }

    @GetMapping("/access-denied")
    public void accessDeniedException() {
        throw new AccessDeniedException("로그인이 필요한 요청입니다.");
    }

}
