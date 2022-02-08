package com.lcc.cloist.controller.common;

import com.lcc.cloist.model.response.SingleResult;
import com.lcc.cloist.service.common.RecaptchaService;
import com.lcc.cloist.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recaptcha")
public class RecaptchaController {
    private final ResponseService responseService;
    private final RecaptchaService recaptchaService;

    @GetMapping(value = "/verify/{token}")
    public SingleResult<Boolean> verifyRecaptcha(@PathVariable String token) {
        return responseService.getSingleResult(recaptchaService.verifyRecaptcha(token));
    }
}
