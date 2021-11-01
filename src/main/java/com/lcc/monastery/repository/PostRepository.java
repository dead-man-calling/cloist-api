package com.lcc.monastery.repository;

import com.lcc.monastery.entity.Gathering;
import com.lcc.monastery.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByGatheringId(Long gatheringId);

    List<Post> findByGathering(Gathering gathering);

    List<Post> findByGatheringId(Long gatheringId, Pageable pageable);

    List<Post> findByGathering(Gathering gathering, Pageable pageable);
}
