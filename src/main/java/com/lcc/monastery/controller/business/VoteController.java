package com.lcc.monastery.controller.business;

import com.lcc.monastery.dto.vote.*;
import com.lcc.monastery.entity.Vote;
import com.lcc.monastery.model.response.CommonResult;
import com.lcc.monastery.model.response.SingleResult;
import com.lcc.monastery.service.business.VoteService;
import com.lcc.monastery.service.common.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"5. Vote"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class VoteController {
    private final ResponseService responseService;
    private final VoteService voteService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "투표 생성", notes = "투표를 생성한다.")
    @PostMapping(value = "/vote")
    public CommonResult createVote(@RequestBody CreateVoteRq createVoteRq,
                                   @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        voteService.createVote(createVoteRq);
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
    @ApiOperation(value = "투표 조회", notes = "투표를 조회한다.")
    @PostMapping(value = "/vote/view")
    public SingleResult<FindVoteRs> findVote(@RequestBody FindVoteRq findVoteRq,
                                             @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(voteService.findVote(findVoteRq));
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
    @ApiOperation(value = "투표", notes = "투표 진행.")
    @PutMapping(value = "/vote")
    public SingleResult<Boolean> vote(@RequestHeader("Authorization") String authorization,
                                     @RequestBody VoteRq voteRq,
                                     @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(voteService.vote(voteRq, authorization));
    }
}
