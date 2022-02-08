package com.lcc.cloist.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lcc.cloist.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "메시지 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class SendMessageRq {
    @ApiModelProperty(required = true, value = "사용자 번호", position = 0)
    private String userId;
    @ApiModelProperty(required = true, value = "모임 번호", position = 1)
    private Long gatheringId;
    @ApiModelProperty(required = true, value = "메시지", position = 2)
    private String content;
    @ApiModelProperty(required = true, value = "메시지 속성", position = 3)
    private Message.MessageProperty messageProperty;
}
