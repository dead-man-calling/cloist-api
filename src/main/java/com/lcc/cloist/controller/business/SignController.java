package com.lcc.cloist.controller.business;

import com.lcc.cloist.dto.sign.SignInRq;
import com.lcc.cloist.dto.sign.SignUpRq;
import com.lcc.cloist.model.response.CommonResult;
import com.lcc.cloist.model.response.SingleResult;
import com.lcc.cloist.service.business.SignService;
import com.lcc.cloist.service.common.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Sign"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SignController {
    private final ResponseService responseService;
    private final SignService signService;

    @ApiOperation(value = "Sign in", notes = "Sign in to email")
    @PostMapping(value = "/signin")
    public SingleResult<String> signIn(@RequestBody SignInRq signInRq,
                                       @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(signService.signIn(signInRq));
    }

    @ApiOperation(value = "Sign up", notes = "Sign up")
    @PostMapping(value = "/signup")
    public CommonResult signUp(@RequestBody SignUpRq signUpRq,
                               @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        signService.signUp(signUpRq);
        return responseService.getSuccessResult();
    }
}
