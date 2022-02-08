package com.lcc.cloist.dto.postcomment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "댓글 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CreatePostCommentRq {
    @ApiModelProperty(required = true, value = "게시글 번호", position = 0)
    private long postId;

    @ApiModelProperty(required = true, value = "내용", position = 1)
    private String content;
}
