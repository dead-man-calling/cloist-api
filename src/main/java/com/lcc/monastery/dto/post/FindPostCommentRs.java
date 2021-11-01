package com.lcc.monastery.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

@ApiModel(description = "댓글 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FindPostCommentRs {
    @ApiModelProperty(required = true, value = "댓글 번호", position = 0)
    public long id;

    @ApiModelProperty(required = true, value = "댓글 내용", position = 1)
    public String content;
}
