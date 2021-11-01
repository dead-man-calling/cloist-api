package com.lcc.monastery.repository;

import com.lcc.monastery.entity.Gathering;
import com.lcc.monastery.entity.User;
import com.lcc.monastery.entity.UserGathering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGatheringRepository extends JpaRepository<UserGathering, Long> {
    List<UserGathering> findByUserId(Long userId);

    List<UserGathering> findByUser(User user);

    List<UserGathering> findByGatheringId(Long gatheringId);

    List<UserGathering> findByGathering(Gathering gathering);

    Optional<UserGathering> findByUserIdAndGatheringId(Long userId, Long gatheringId);

    Optional<UserGathering> findByUserAndGathering(User user, Gathering gathering);
}