package com.lcc.cloist.dto.vote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lcc.cloist.entity.Vote;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "투표 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CreateVoteRq {
    @ApiModelProperty(required = true, value = "모임 번호", position = 0)
    private long gatheringId;

    @ApiModelProperty(required = true, value = "투표 속성", position = 1)
    private Vote.VoteProperty voteProperty;

    @ApiModelProperty(value = "투표 설명", position = 2)
    private String description;

    @ApiModelProperty(value = "부당 유저", position = 3)
    private long convictionUserId;

    @ApiModelProperty(value = "부당 메시지", position = 4)
    private String convictionMessage;

    @ApiModelProperty(value = "부당 메시지 주장", position = 5)
    private String convictionOpinion;
}
