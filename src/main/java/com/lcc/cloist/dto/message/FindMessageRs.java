package com.lcc.cloist.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lcc.cloist.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(description = "메시지 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FindMessageRs {
    @ApiModelProperty(required = true, value = "메시지 번호", position = 0)
    public long id;

    @ApiModelProperty(value = "수신자 이름", position = 1)
    public String username;

    @ApiModelProperty(value = "발신자 번호", position = 2)
    public long sentUserId;

    @ApiModelProperty(required = true, value = "메시지 내용", position = 3)
    public String content;

    @ApiModelProperty(value = "메시지 속성", position = 4)
    public Message.MessageProperty messageProperty;
}
