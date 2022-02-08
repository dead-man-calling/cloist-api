package com.lcc.cloist.dto.sign;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "인증 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class SignInRq {
    @ApiModelProperty(required = true, value = "사용자 실명", position = 0)
    private String username;
    @ApiModelProperty(required = true, value = "사용자 전화번호", position = 1)
    private String phoneNumber;
    @ApiModelProperty(required = true, value = "사용자 비밀번호", position = 2)
    private String password;
}
