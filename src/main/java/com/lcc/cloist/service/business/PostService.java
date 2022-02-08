package com.lcc.cloist.service.business;

import com.lcc.cloist.advice.exception.GatheringNotFoundException;
import com.lcc.cloist.advice.exception.PostNotFoundException;
import com.lcc.cloist.dto.post.CreatePostRs;
import com.lcc.cloist.dto.post.FindPostCommentRs;
import com.lcc.cloist.dto.post.FindPostRs;
import com.lcc.cloist.entity.Gathering;
import com.lcc.cloist.entity.Post;
import com.lcc.cloist.entity.PostComment;
import com.lcc.cloist.dto.postcomment.CreatePostCommentRq;
import com.lcc.cloist.dto.post.CreatePostRq;
import com.lcc.cloist.repository.GatheringRepository;
import com.lcc.cloist.repository.PostCommentRepository;
import com.lcc.cloist.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final GatheringRepository gatheringRepository;

    public List<FindPostRs> findPosts(Long gatheringId) {
        List<Post> posts = postRepository.findByGatheringId(gatheringId);

        List<FindPostRs> result = new ArrayList<>();

        posts.forEach(p ->
                result.add(
                        FindPostRs.builder()
                                .id(p.getId())
                                .title(p.getTitle())
                                .content(p.getContent())
                                .build()
                ));

        return result;
    }

    public List<FindPostRs> findPosts(Long gatheringId, Pageable pageable) {
        List<Post> posts = postRepository.findByGatheringId(gatheringId, pageable);

        List<FindPostRs> result = new ArrayList<>();

        posts.forEach(p ->
                result.add(
                        FindPostRs.builder()
                                .id(p.getId())
                                .title(p.getTitle())
                                .content(p.getContent())
                                .build()
                ));

        return result;
    }

    public List<FindPostCommentRs> findPostComments(Long postId) {
        List<PostComment> postComments = postCommentRepository.findByPostId(postId);

        List<FindPostCommentRs> result = new ArrayList<>();

        postComments.forEach(c -> result.add(
                FindPostCommentRs.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .build()
        ));

        return result;
    }

    @Transactional
    public CreatePostRs createPost(CreatePostRq createPostRq) {
        Gathering gathering = gatheringRepository
                .findById(createPostRq.getGatheringId())
                .orElseThrow(GatheringNotFoundException::new);

        Post post = postRepository.save(Post.builder()
                .gathering(gathering)
                .title(createPostRq.getTitle())
                .content(createPostRq.getContent())
                .build());

        return CreatePostRs.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Transactional
    public void createPostComment(CreatePostCommentRq createPostCommentRq) {
        Post post = postRepository
                .findById(createPostCommentRq.getPostId())
                .orElseThrow(PostNotFoundException::new);

        postCommentRepository.save(PostComment.builder()
                .post(post)
                .content(createPostCommentRq.getContent())
                .build());
    }
}
