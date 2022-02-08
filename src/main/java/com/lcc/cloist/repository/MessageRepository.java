package com.lcc.cloist.repository;

import com.lcc.cloist.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Long countByUserIdAndGatheringId(Long userId, Long gatheringId);

    List<Message> findByUserIdAndGatheringIdOrderByCreatedAtDesc(Long userId, Long gatheringId);

    List<Message> findBySentUserIdAndGatheringIdOrderByCreatedAtDesc(Long sentUserId, Long gatheringId);
}
