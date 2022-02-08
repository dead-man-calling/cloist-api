package com.lcc.cloist.controller.business;

import com.lcc.cloist.dto.gathering.CreateGatheringRq;
import com.lcc.cloist.dto.gathering.FindGatheringRs;
import com.lcc.cloist.dto.gathering.InviteUserRq;
import com.lcc.cloist.model.response.CommonResult;
import com.lcc.cloist.model.response.MultipleResult;
import com.lcc.cloist.model.response.SingleResult;
import com.lcc.cloist.service.business.GatheringService;
import com.lcc.cloist.service.common.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"3. Gathering"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GatheringController {
    private final ResponseService responseService;
    private final GatheringService gatheringService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "모임 생성", notes = "모임을 생성한다.")
    @PostMapping(value = "/gathering")
    public CommonResult createGathering(@RequestHeader("Authorization") String authorization,
                                        @RequestBody CreateGatheringRq createGatheringRq,
                                        @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        gatheringService.createGathering(authorization, createGatheringRq);
        return responseService.getSuccessResult();
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
    @ApiOperation(value = "모임 초대", notes = "사용자를 모임에 초대한다.")
    @PostMapping(value = "/gathering/{id}")
    public CommonResult inviteUserToGathering(@RequestBody InviteUserRq inviteUserRq,
                                              @PathVariable("id") Long id,
                                              @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        gatheringService.inviteUserToGathering(id, inviteUserRq);
        return responseService.getSuccessResult();
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
    @ApiOperation(value = "모임 조회", notes = "사용자가 속한 모임을 조회한다.")
    @GetMapping(value = "/gatherings")
    public MultipleResult<FindGatheringRs> findGatheringsByUser(@RequestHeader("Authorization") String authorization,
                                                                @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(gatheringService.findGatheringsByUser(authorization));
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
    @ApiOperation(value = "모임 정보", notes = "모임의 정보를 가져온다.")
    @GetMapping(value = "/gathering/{id}")
    public SingleResult<FindGatheringRs> findGatheringById(@PathVariable("id") Long id,
                                                           @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(gatheringService.findGatheringById(id));
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
    @ApiOperation(value = "모임 탈퇴", notes = "사용자가 속한 모임에서 탈퇴한다.")
    @PutMapping(value = "/gathering/{id}")
    public CommonResult withdrawFromGathering(@RequestHeader("Authorization") String authorization,
                                              @PathVariable("id") Long id,
                                              @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        gatheringService.withdrawFromGathering(id, authorization);
        return responseService.getSuccessResult();
    }
}
