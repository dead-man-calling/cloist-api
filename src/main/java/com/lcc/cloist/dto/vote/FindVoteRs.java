package com.lcc.cloist.dto.vote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lcc.cloist.entity.Vote;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(description = "투표 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FindVoteRs {
    @ApiModelProperty(value = "투표 존재 여부", position = 0)
    public Vote.ExistProperty existProperty;

    @ApiModelProperty(value = "투표 상세", position = 1)
    public String description;

    @ApiModelProperty(value = "피고 번호", position = 2)
    public String convictionUserId;

    @ApiModelProperty(value = "피고 메시지", position = 3)
    public String convictionMessage;

    @ApiModelProperty(value = "원고 주장", position = 4)
    public String convictionOpinion;
}
