package com.lcc.cloist.controller.business;

import com.lcc.cloist.dto.message.FindMessageRs;
import com.lcc.cloist.dto.message.SendMessageRq;
import com.lcc.cloist.model.response.CommonResult;
import com.lcc.cloist.model.response.MultipleResult;
import com.lcc.cloist.model.response.SingleResult;
import com.lcc.cloist.service.business.MessageService;
import com.lcc.cloist.service.common.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"4. Message"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {
    private final ResponseService responseService;
    private final MessageService messageService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "메시지 수 조회", notes = "사용자에게 온 메시지 수 조회")
    @GetMapping(value = "/message/count/{gatheringid}")
    public SingleResult<Long> getMessageCount(@RequestHeader("Authorization") String authorization,
                                              @PathVariable("gatheringid") Long gatheringId,
                                              @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(messageService.getMessageCount(gatheringId, authorization));
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
    @ApiOperation(value = "메시지 조회", notes = "사용자에게 온 메시지를 조회한다.")
    @GetMapping(value = "/messages/{gatheringid}")
    public MultipleResult<FindMessageRs> findMessages(@RequestHeader("Authorization") String authorization,
                                                      @PathVariable("gatheringid") Long gatheringId,
                                                      @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(messageService.findMessages(gatheringId, authorization));
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
    @ApiOperation(value = "메시지 조회", notes = "사용자가 보낸 메시지를 조회한다.")
    @GetMapping(value = "/messages/sent/{gatheringid}")
    public MultipleResult<FindMessageRs> findSentMessages(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable("gatheringid") Long gatheringId,
                                                          @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(messageService.findSentMessages(gatheringId, authorization));
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
    @ApiOperation(value = "메시지 전송", notes = "메시지를 전송한다.")
    @PostMapping(value = "/message")
    public CommonResult sendMessage(@RequestHeader("Authorization") String authorization,
                                    @RequestBody SendMessageRq sendMessageRq,
                                    @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        messageService.sendMessage(sendMessageRq, authorization);
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
    @ApiOperation(value = "메시지 삭제", notes = "메시지를 삭제한다.")
    @DeleteMapping(value = "/message/{id}")
    public CommonResult deleteMessage(@RequestHeader("Authorization") String authorization,
                                      @PathVariable("id") Long id,
                                      @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        messageService.deleteMessage(id, authorization);
        return responseService.getSuccessResult();
    }
}
