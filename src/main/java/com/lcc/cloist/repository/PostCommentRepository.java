package com.lcc.cloist.repository;

import com.lcc.cloist.entity.Post;
import com.lcc.cloist.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);

    List<PostComment> findByPost(Post post);
}
