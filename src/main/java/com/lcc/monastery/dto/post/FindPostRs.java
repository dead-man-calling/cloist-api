package com.lcc.monastery.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(description = "게시글 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FindPostRs {
    @ApiModelProperty(required = true, value = "게시글 번호", position = 0)
    public long id;

    @ApiModelProperty(required = true, value = "게시글 제목", position = 1)
    public String title;

    @ApiModelProperty(required = true, value = "게시글 내용", position = 2)
    public String content;
}
