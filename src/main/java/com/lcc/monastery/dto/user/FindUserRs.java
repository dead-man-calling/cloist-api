package com.lcc.monastery.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(description = "유저 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FindUserRs {
    @ApiModelProperty(value = "유저 번호", position = 0)
    public String id;

    @ApiModelProperty(value = "유저 이름", position = 1)
    public String username;

    @ApiModelProperty(value = "유저 점수", position = 2)
    public int rating;

    @ApiModelProperty(value = "유저 축복 수", position = 3)
    public int prize;

    @ApiModelProperty(value = "유저 경고 수", position = 4)
    public int caution;

    @ApiModelProperty(value = "유저 전과 수", position = 5)
    public int conviction;

    @ApiModelProperty(value = "유저 자신 여부", position = 6)
    public boolean isSelf;
}
