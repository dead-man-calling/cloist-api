package com.lcc.monastery.controller.business;

import com.lcc.monastery.dto.post.CreatePostRq;
import com.lcc.monastery.dto.post.CreatePostRs;
import com.lcc.monastery.dto.post.FindPostCommentRs;
import com.lcc.monastery.dto.post.FindPostRs;
import com.lcc.monastery.dto.postcomment.CreatePostCommentRq;
import com.lcc.monastery.model.response.CommonResult;
import com.lcc.monastery.model.response.MultipleResult;
import com.lcc.monastery.model.response.SingleResult;
import com.lcc.monastery.service.business.PostService;
import com.lcc.monastery.service.common.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"6. Post"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final ResponseService responseService;
    private final PostService postService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "게시글 조회", notes = "전체 게시글을 조회한다.")
    @GetMapping(value = "/posts/{gatheringid}")
    public MultipleResult<FindPostRs> findPosts(@PathVariable("gatheringid") Long gatheringId,
                                                @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(postService.findPosts(gatheringId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            ),
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "String", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회한다.")
    @GetMapping(value = "/posts/page/{gatheringid}")
    public MultipleResult<FindPostRs> findPosts(@PathVariable("gatheringid") Long gatheringId,
                                                @ApiIgnore final Pageable pageable,
                                                @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(postService.findPosts(gatheringId, pageable));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "댓글 조회", notes = "댓글을 조회한다.")
    @GetMapping(value = "/post/comments/{postid}")
    public MultipleResult<FindPostCommentRs> findPostComments(@PathVariable("postid") Long postId,
                                                              @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getMultipleResult(postService.findPostComments(postId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성한다.")
    @PostMapping(value = "/post")
    public SingleResult<CreatePostRs> createPost(@RequestBody CreatePostRq createPostRq,
                                                 @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(postService.createPost(createPostRq));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Access Token",
                    required = true,
                    dataType = "String",
                    paramType = "header"
            )
    })
    @ApiOperation(value = "댓글 생성", notes = "댓글을 생성한다.")
    @PostMapping(value = "/post/comment")
    public CommonResult createPostComment(@RequestBody CreatePostCommentRq createPostCommentRq,
                                          @ApiParam(value = "언어", allowableValues = "ko,en", defaultValue = "ko") @RequestParam String lang) {
        postService.createPostComment(createPostCommentRq);
        return responseService.getSuccessResult();
    }
}
