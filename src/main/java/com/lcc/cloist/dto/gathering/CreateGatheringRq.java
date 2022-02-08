package com.lcc.cloist.dto.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "모임 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CreateGatheringRq {
    @ApiModelProperty(required = true, value = "모임 이름", position = 0)
    private String gatheringName;

    @ApiModelProperty(required = true, value = "모임 설명", position = 1)
    private String description;
}
