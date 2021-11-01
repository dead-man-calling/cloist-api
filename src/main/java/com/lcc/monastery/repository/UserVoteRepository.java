package com.lcc.monastery.repository;

import com.lcc.monastery.entity.User;
import com.lcc.monastery.entity.UserVote;
import com.lcc.monastery.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVoteRepository extends JpaRepository<UserVote, Long> {
    Optional<UserVote> findByUserIdAndVoteId(Long userId, Long voteId);

    Optional<UserVote> findByUserAndVote(User user, Vote vote);

    List<UserVote> findByVoteId(Long voteId);

    List<UserVote> findByVote(Vote vote);
}
