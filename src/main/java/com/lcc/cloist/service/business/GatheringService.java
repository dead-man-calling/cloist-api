package com.lcc.cloist.service.business;

import com.lcc.cloist.advice.exception.GatheringNotFoundException;
import com.lcc.cloist.advice.exception.InvalidFormException;
import com.lcc.cloist.advice.exception.UserExistException;
import com.lcc.cloist.dto.gathering.CreateGatheringRq;
import com.lcc.cloist.dto.gathering.FindGatheringRs;
import com.lcc.cloist.dto.gathering.InviteUserRq;
import com.lcc.cloist.entity.*;
import com.lcc.cloist.repository.GatheringRepository;
import com.lcc.cloist.repository.UserGatheringRepository;
import com.lcc.cloist.repository.UserVoteRepository;
import com.lcc.cloist.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GatheringService {
    private final GatheringRepository gatheringRepository;
    private final UserGatheringRepository userGatheringRepository;
    private final VoteRepository voteRepository;
    private final UserVoteRepository userVoteRepository;

    private final UserService userService;
    private final MessageService messageService;

    @Transactional
    public void createGathering(String authorization, CreateGatheringRq createGatheringRq) {
        User user = userService.findUser(authorization);

        Gathering gathering =
                gatheringRepository.save(Gathering.builder()
                        .gatheringName(createGatheringRq.getGatheringName())
                        .description(createGatheringRq.getDescription())
                        .prizeVotes(0)
                        .cautionVotes(0)
                        .convictionVotes(0)
                        .userCount(1)
                        .build());

        userGatheringRepository.save(UserGathering.builder()
                .gathering(gathering)
                .user(user)
                .build());
    }

    @Transactional
    public void inviteUserToGathering(Long id, InviteUserRq inviteUserRq) {
        String regKoreanName = "^[가-힣]{2,6}$";
        String regPhoneNumber = "^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$";

        if (!Pattern.matches(regKoreanName, inviteUserRq.getUsername()) ||
                !Pattern.matches(regPhoneNumber, inviteUserRq.getPhoneNumber()))
            throw new InvalidFormException();

        Gathering gathering =
                gatheringRepository
                        .findById(id)
                        .orElseThrow(GatheringNotFoundException::new);

        User user = userService.findUser(inviteUserRq.getUsername(), inviteUserRq.getPhoneNumber());

        if (userGatheringRepository.findByUserAndGathering(user, gathering).isPresent())
            throw new UserExistException();

        gathering.setUserCount(gathering.getUserCount() + 1);

        userGatheringRepository.save(UserGathering.builder()
                .user(user)
                .gathering(gathering)
                .build());
    }

    @Transactional
    public List<FindGatheringRs> findGatheringsByUser(String authorization) {
        User user = userService.findUser(authorization);

        List<UserGathering> userGatherings = userGatheringRepository.findByUser(user);
        List<FindGatheringRs> result = new ArrayList<>();

        userGatherings.forEach(ug -> {
            Gathering g = ug.getGathering();
            result.add(
                    FindGatheringRs.builder()
                            .id(g.getId())
                            .gatheringName(g.getGatheringName())
                            .description(g.getDescription())
                            .prizeVotes(g.getPrizeVotes())
                            .cautionVotes(g.getCautionVotes())
                            .convictionVotes(g.getConvictionVotes())
                            .userCount(g.getUserCount())
                            .messageCount(messageService.getMessageCount(user.getId(), g.getId()))
                            .build()
            );
        });

        return result;
    }

    public FindGatheringRs findGatheringById(Long id) {
        Gathering gathering =
                gatheringRepository.findById(id).orElseThrow(GatheringNotFoundException::new);

        return FindGatheringRs.builder()
                .id(gathering.getId())
                .description(gathering.getDescription())
                .gatheringName(gathering.getGatheringName())
                .prizeVotes(gathering.getPrizeVotes())
                .cautionVotes(gathering.getCautionVotes())
                .convictionVotes(gathering.getConvictionVotes())
                .userCount(gathering.getUserCount())
                .build();
    }

    @Transactional
    public void withdrawFromGathering(Long id, String authorization) {
        User user = userService.findUser(authorization);

        Gathering gathering =
                gatheringRepository.findById(id).orElseThrow(GatheringNotFoundException::new);

        if (gathering.getUserCount() - 1 == 0) {
            gatheringRepository.delete(gathering);
        } else {
            gathering.setUserCount(gathering.getUserCount() - 1);

            UserGathering userGathering =
                    userGatheringRepository.findByUserAndGathering(user, gathering).orElseThrow();

            Optional<Vote> prizeVote =
                    voteRepository.findByGatheringAndVoteProperty(gathering, Vote.VoteProperty.PRIZE);
            Optional<Vote> cautionVote =
                    voteRepository.findByGatheringAndVoteProperty(gathering, Vote.VoteProperty.CAUTION);
            Optional<Vote> convictionVote =
                    voteRepository.findByGatheringAndVoteProperty(gathering, Vote.VoteProperty.CONVICTION);

            if (prizeVote.isPresent()) {
                Vote pv = prizeVote.get();

                List<UserVote> uvs = userVoteRepository.findByVote(pv);

                // 자신이 보낸 표 혹은 자신에게 보낸 표를 추려낸다.
                List<UserVote> uvsd =
                        uvs.stream().filter(
                                uv -> uv.getReceivedUserId() == user.getId() || uv.getUser() == user
                        ).toList();

                if (!uvsd.isEmpty()) {
                    pv.setVoteCount(pv.getVoteCount() - uvsd.size());
                    userVoteRepository.deleteAllInBatch(uvsd);
                }
            }

            if (cautionVote.isPresent()) {
                Vote cv = cautionVote.get();

                List<UserVote> uvs = userVoteRepository.findByVote(cv);

                // 자신이 보낸 표 혹은 자신에게 보낸 표를 추려낸다.
                List<UserVote> uvsd =
                        uvs.stream().filter(
                                uv -> uv.getReceivedUserId() == user.getId() || uv.getUser() == user
                        ).toList();

                if (!uvsd.isEmpty()) {
                    cv.setVoteCount(cv.getVoteCount() - uvsd.size());
                    userVoteRepository.deleteAllInBatch(uvsd);
                }
            }

            if (convictionVote.isPresent()) {
                Vote vv = convictionVote.get();

                List<UserVote> uvs = userVoteRepository.findByVote(vv);

                // 자신이 보낸 표 혹은 자신에게 보낸 표를 추려낸다.
                List<UserVote> uvsd =
                        uvs.stream().filter(
                                uv -> uv.getReceivedUserId() == user.getId() || uv.getUser() == user
                        ).toList();

                if (!uvsd.isEmpty()) {
                    vv.setVoteCount(vv.getVoteCount() - uvsd.size());
                    userVoteRepository.deleteAllInBatch(uvsd);
                }
            }

            userGatheringRepository.delete(userGathering);
        }
    }
}
