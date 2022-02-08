package com.lcc.cloist.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "사용자 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class FindUserRq {
    @ApiModelProperty(required = true, value = "사용자 이름", position = 0)
    private String username;
    @ApiModelProperty(required = true, value = "사용자 전화번호", position = 1)
    private String phoneNumber;
}
