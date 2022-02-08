package com.lcc.cloist.repository;

import com.lcc.cloist.entity.Gathering;
import com.lcc.cloist.entity.User;
import com.lcc.cloist.entity.UserGathering;
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