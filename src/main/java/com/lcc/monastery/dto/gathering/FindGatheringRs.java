package com.lcc.monastery.dto.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(description = "모임 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FindGatheringRs {
    @ApiModelProperty(value = "모임 번호", position = 0)
    public long id;

    @ApiModelProperty(value = "모임 상세", position = 1)
    public String description;

    @ApiModelProperty(value = "모임 이름", position = 2)
    public String gatheringName;

    @ApiModelProperty(value = "축복 투표회 수", position = 3)
    public int prizeVotes;

    @ApiModelProperty(value = "경고 투표회 수", position = 4)
    public int cautionVotes;

    @ApiModelProperty(value = "재판 수", position = 5)
    public int convictionVotes;

    @ApiModelProperty(value = "모임 인원", position = 6)
    public int userCount;

    @ApiModelProperty(value = "메세지 수", position = 7)
    public long messageCount;
}
