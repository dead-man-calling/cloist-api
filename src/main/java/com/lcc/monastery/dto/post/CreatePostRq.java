package com.lcc.monastery.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "게시글 모델")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CreatePostRq {
    @ApiModelProperty(required = true, value = "모임 번호", position = 0)
    private long gatheringId;

    @ApiModelProperty(required = true, value = "제목", position = 1)
    private String title;

    @ApiModelProperty(required = true, value = "내용", position = 2)
    private String content;
}
