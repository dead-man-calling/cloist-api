package com.lcc.cloist.repository;

import com.lcc.cloist.entity.User;
import com.lcc.cloist.entity.UserVote;
import com.lcc.cloist.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVoteRepository extends JpaRepository<UserVote, Long> {
    Optional<UserVote> findByUserIdAndVoteId(Long userId, Long voteId);

    Optional<UserVote> findByUserAndVote(User user, Vote vote);

    List<UserVote> findByVoteId(Long voteId);

    List<UserVote> findByVote(Vote vote);
}
