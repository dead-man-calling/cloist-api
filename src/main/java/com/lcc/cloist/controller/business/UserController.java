package com.lcc.cloist.controller.business;

import com.lcc.cloist.dto.user.FindUserRs;
import com.lcc.cloist.dto.user.FindUserRq;
import com.lcc.cloist.model.response.CommonResult;
import com.lcc.cloist.model.response.MultipleResult;
import com.lcc.cloist.model.response.SingleResult;
import com.lcc.cloist.service.business.UserService;
import com.lcc.cloist.service.common.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. User"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final ResponseService responseService;
    private final UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "유저 조회", notes = "유저 정보를 조회한다.")
    @GetMapping(value = "/user")
    public SingleResult<FindUserRs> findUser(@RequestHeader("Authorization") String authorization,
                                             @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(userService.findUser(userService.findUser(authorization)));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "유저 조회", notes = "유저 정보를 조회한다.")
    @PostMapping(value = "/user")
    public SingleResult<FindUserRs> findUser(@RequestBody FindUserRq findUserRq,
                                             @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(userService.findUser(userService.findUser(findUserRq)));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "모임 유저 조회", notes = "모임에 속한 유저 정보를 조회한다.")
    @GetMapping(value = "/users/{gatheringid}")
    public MultipleResult<FindUserRs> findUsersByGathering(@RequestHeader("Authorization") String authorization,
                                                           @PathVariable("gatheringid") Long gatheringId,
                                                           @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(userService.findUsersByGathering(gatheringId, authorization));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "유저 삭제", notes = "유저 정보를 삭제한다.")
    @DeleteMapping(value = "/user")
    public CommonResult deleteUser(@RequestHeader("Authorization") String authorization,
                                   @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        userService.deleteUser(authorization);
        return responseService.getSuccessResult();
    }
}
