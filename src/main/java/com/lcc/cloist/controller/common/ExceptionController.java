package com.lcc.cloist.controller.common;

import com.lcc.cloist.advice.exception.AuthenticationEntryPointException;
import com.lcc.cloist.model.response.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {
    @GetMapping(value = "/entrypoint")
    public CommonResult entryPointException(@RequestParam String lang) {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping(value = "/accessdenied")
    public CommonResult accessDenied(@RequestParam String lang) {
        throw new AccessDeniedException("");
    }
}
