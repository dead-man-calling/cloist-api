package com.lcc.monastery.service.business;

import com.lcc.monastery.advice.exception.GatheringNotFoundException;
import com.lcc.monastery.advice.exception.VoteExistException;
import com.lcc.monastery.advice.exception.VoteNotFoundException;
import com.lcc.monastery.dto.message.SendMessageRq;
import com.lcc.monastery.dto.vote.CreateVoteRq;
import com.lcc.monastery.dto.vote.FindVoteRq;
import com.lcc.monastery.dto.vote.FindVoteRs;
import com.lcc.monastery.dto.vote.VoteRq;
import com.lcc.monastery.entity.*;
import com.lcc.monastery.repository.GatheringRepository;
import com.lcc.monastery.repository.UserGatheringRepository;
import com.lcc.monastery.repository.UserVoteRepository;
import com.lcc.monastery.repository.VoteRepository;
import com.lcc.monastery.util.AESCrypto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserVoteRepository userVoteRepository;
    private final UserGatheringRepository userGatheringRepository;
    private final GatheringRepository gatheringRepository;

    private final AESCrypto aesCrypto;
    private final UserService userService;
    private final MessageService messageService;

    @Transactional
    public void createVote(CreateVoteRq createVoteRq) {
        Gathering gathering =
                gatheringRepository.findById(createVoteRq.getGatheringId()).orElseThrow(GatheringNotFoundException::new);

        if (voteRepository.findByGatheringAndVoteProperty(
                        gathering,
                        createVoteRq.getVoteProperty())
                .isPresent())
            throw new VoteExistException();

        Vote vote = voteRepository.save(Vote.builder()
                .gathering(gathering)
                .description(createVoteRq.getDescription())
                .convictionUserId(createVoteRq.getConvictionUserId())
                .convictionMessage(createVoteRq.getConvictionMessage())
                .convictionOpinion(createVoteRq.getConvictionOpinion())
                .voteCount(0)
                .voteProperty(createVoteRq.getVoteProperty())
                .existProperty(Vote.ExistProperty.EXIST)
                .build());

        if (createVoteRq.getVoteProperty() == Vote.VoteProperty.PRIZE) {
            gathering.setPrizeVotes(gathering.getPrizeVotes() + 1);
        } else if (createVoteRq.getVoteProperty() == Vote.VoteProperty.CAUTION) {
            gathering.setCautionVotes(gathering.getCautionVotes() + 1);
        } else {
            gathering.setConvictionVotes(gathering.getConvictionVotes() + 1);

            User convictionUser = userService.findUser(createVoteRq.getConvictionUserId());

            vote.setVoteCount(1);

            userVoteRepository.save(UserVote.builder()
                    .user(convictionUser)
                    .vote(vote)
                    .receivedUserId(convictionUser.getId())
                    .isAgreed(true)
                    .build());
        }
    }

    @Transactional
    public FindVoteRs findVote(FindVoteRq findVoteRq) {
        Optional<Vote> vote = voteRepository.findByGatheringIdAndVoteProperty(
                        findVoteRq.getGatheringId(),
                        findVoteRq.getVoteProperty());

        if (vote.isEmpty())
            return FindVoteRs.builder().existProperty(Vote.ExistProperty.NOT_EXIST).build();

        if (vote.get().getExistProperty() == Vote.ExistProperty.FINISHED) {
            finish(vote.get());

            return FindVoteRs.builder().existProperty(Vote.ExistProperty.FINISHED).build();
        }

        return FindVoteRs.builder()
                .existProperty(Vote.ExistProperty.EXIST)
                .description(vote.get().getDescription())
                .convictionUserId(
                        aesCrypto.encrypt(
                                "AES/CBC/PKCS5Padding",
                                        String.valueOf(vote.get().getConvictionUserId())
                            )
                )
                .convictionMessage(vote.get().getConvictionMessage())
                .convictionOpinion(vote.get().getConvictionOpinion())
                .build();
    }

    @Transactional
    public void finish(Vote vote) {
        Gathering gathering = vote.getGathering();

        if (vote.getVoteProperty() == Vote.VoteProperty.PRIZE)
            gathering.setPrizeVotes(gathering.getPrizeVotes() - 1);
        else if (vote.getVoteProperty() == Vote.VoteProperty.CAUTION)
            gathering.setCautionVotes(gathering.getCautionVotes() - 1);
        else
            gathering.setConvictionVotes(gathering.getConvictionVotes() - 1);

        if (vote.getVoteCount() == 0) {
            voteRepository.delete(vote);
            return;
        }

        boolean isEqual = true;
        long maxId = -1;
        long maxCount = 0;
        String convictionUsername = null;

        List<UserVote> userVotes = userVoteRepository.findByVote(vote);
        List<UserGathering> userGatherings =
                userGatheringRepository.findByGathering(gathering);

        if (vote.getVoteProperty() != Vote.VoteProperty.CONVICTION) {
            HashMap<Long, Long> counter = new HashMap<>();

            userVotes.forEach(uv -> {
                long receivedUserId = uv.getReceivedUserId();
                if (counter.containsKey(receivedUserId))
                    counter.put(receivedUserId, counter.get(receivedUserId) + 1);
                else
                    counter.put(receivedUserId, 1L);
            });

            long maxValue = -1;

            for (var entry : counter.entrySet()) {
                if (maxValue < entry.getValue()) {
                    maxValue = entry.getValue();
                    maxId = entry.getKey();
                }
            }

            for (var entry : counter.entrySet()) {
                if (maxValue != entry.getValue()) {
                    isEqual = false;
                    break;
                }
            }
        } else {
            maxCount = userVotes.stream().filter(UserVote::isAgreed).count();
            convictionUsername =
                    userService.findUser(vote.getConvictionUserId()).getUsername();
        }

        boolean finalIsEqual = isEqual;
        long finalMaxId = maxId;
        long finalMaxCount = maxCount;
        String finalConvictionUsername = convictionUsername;

        String appointMessage = "가장 " + vote.getDescription() + " 사람으로 선정되었습니다.";
        String resultMessage = "가장 " + vote.getDescription() + " 사람에 대한 투표가 종료되었습니다.";

        userGatherings.forEach(g -> {
            User u = g.getUser();

            SendMessageRq sendMessageRq = new SendMessageRq();
            sendMessageRq.setGatheringId(g.getGathering().getId());
            sendMessageRq.setUserId(String.valueOf(u.getId()));

            if (vote.getVoteProperty() == Vote.VoteProperty.PRIZE) {
                if (finalIsEqual || u.getId() == finalMaxId) {
                    int rating = u.getRating();

                    u.setPrize(u.getPrize() + 1);
                    u.setRating(rating == 2000 ? 2000 : rating + 50);

                    sendMessageRq.setContent(appointMessage);
                } else {
                    sendMessageRq.setContent(resultMessage);
                }
                sendMessageRq.setMessageProperty(Message.MessageProperty.PRIZE);
            } else if (vote.getVoteProperty() == Vote.VoteProperty.CAUTION) {
                if (finalIsEqual || u.getId() == finalMaxId) {
                    int rating = u.getRating();

                    u.setCaution(u.getCaution() + 1);
                    u.setRating(rating == 0 ? 0 : rating - 50);

                    sendMessageRq.setContent(appointMessage);
                } else {
                    sendMessageRq.setContent(resultMessage);
                }
                sendMessageRq.setMessageProperty(Message.MessageProperty.CAUTION);
            } else {
                if (vote.getVoteCount() / 2 + 1 <= finalMaxCount) {
                    if (u.getId() == vote.getConvictionUserId()) {
                        int rating = u.getRating();

                        u.setConviction(u.getConviction() + 1);
                        u.setRating(rating == 0 ? 0 : rating - 100);

                        sendMessageRq.setContent("죄를 지은 사람으로 선정되었습니다.");
                    } else {
                        sendMessageRq.setContent(
                                "죄를 지은 사람은 " + finalConvictionUsername + " 입니다.\n" +
                                        "보낸 메시지 내용: \n" + vote.getConvictionMessage()
                        );
                    }
                } else {
                    sendMessageRq.setContent("피고의 무죄가 입증되었습니다.");
                }
                sendMessageRq.setMessageProperty(Message.MessageProperty.CONVICTION);
            }

            messageService.sendMessage(sendMessageRq, gathering, u);
        });

        voteRepository.delete(vote);
    }

    @Transactional
    public Boolean vote(VoteRq voteRq, String authorization) {
        User user = userService.findUser(authorization);
        Gathering gathering =
                gatheringRepository.findById(voteRq.getGatheringId()).orElseThrow(GatheringNotFoundException::new);

        Vote vote =
                voteRepository.findByGatheringAndVoteProperty(
                                gathering,
                                voteRq.getVoteProperty())
                        .orElseThrow(VoteNotFoundException::new);

        Optional<UserVote> userVote = userVoteRepository.findByUserAndVote(user, vote);

        if (userVote.isPresent()) {

            // 재판의 대상은 재판 생성시 자동으로 유죄 투표를 한 상태가 된다.
            if (vote.getVoteProperty() == Vote.VoteProperty.CONVICTION &&
                    user.getId() == vote.getConvictionUserId())
                return false;

            userVote.get().setAgreed(voteRq.isAgreed());
            return false;
        }

        vote.setVoteCount(vote.getVoteCount() + 1);

        long decryptReceivedUserId = -1;

        if (vote.getVoteProperty() != Vote.VoteProperty.CONVICTION) {
            decryptReceivedUserId = Long.parseLong(
                    aesCrypto.decrypt(
                            "AES/CBC/PKCS5Padding",
                            voteRq.getReceivedUserId())
            );
        }

        userVoteRepository.save(UserVote.builder()
                .user(user)
                .vote(vote)
                .receivedUserId(decryptReceivedUserId)
                .isAgreed(voteRq.isAgreed())
                .build());

        if (gathering.getUserCount() == vote.getVoteCount()) {
            if (vote.getVoteProperty() == Vote.VoteProperty.PRIZE)
                gathering.setPrizeVotes(gathering.getPrizeVotes() - 1);
            else if (vote.getVoteProperty() == Vote.VoteProperty.CAUTION)
                gathering.setCautionVotes(gathering.getCautionVotes() - 1);
            else
                gathering.setConvictionVotes(gathering.getConvictionVotes() - 1);

            boolean isEqual = true;
            long maxId = -1;
            long maxCount = 0;
            String convictionUsername = null;

            List<UserVote> userVotes = userVoteRepository.findByVote(vote);
            List<UserGathering> userGatherings =
                    userGatheringRepository.findByGathering(gathering);

            if (vote.getVoteProperty() != Vote.VoteProperty.CONVICTION) {
                HashMap<Long, Long> counter = new HashMap<>();

                userVotes.forEach(uv -> {
                    long receivedUserId = uv.getReceivedUserId();
                    if (counter.containsKey(receivedUserId))
                        counter.put(receivedUserId, counter.get(receivedUserId) + 1);
                    else
                        counter.put(receivedUserId, 1L);
                });

                long maxValue = -1;

                for (var entry : counter.entrySet()) {
                    if (maxValue < entry.getValue()) {
                        maxValue = entry.getValue();
                        maxId = entry.getKey();
                    }
                }

                for (var entry : counter.entrySet()) {
                    if (maxValue != entry.getValue()) {
                        isEqual = false;
                        break;
                    }
                }
            } else {
                maxCount = userVotes.stream().filter(UserVote::isAgreed).count();
                convictionUsername =
                        userService.findUser(vote.getConvictionUserId()).getUsername();
            }

            boolean finalIsEqual = isEqual;
            long finalMaxId = maxId;
            long finalMaxCount = maxCount;
            String finalConvictionUsername = convictionUsername;

            String appointMessage = "가장 " + vote.getDescription() + " 사람으로 선정되었습니다.";
            String resultMessage = "가장 " + vote.getDescription() + " 사람에 대한 투표가 종료되었습니다.";

            userGatherings.forEach(g -> {
                User u = g.getUser();

                SendMessageRq sendMessageRq = new SendMessageRq();
                sendMessageRq.setGatheringId(g.getGathering().getId());
                sendMessageRq.setUserId(String.valueOf(u.getId()));

                if (vote.getVoteProperty() == Vote.VoteProperty.PRIZE) {
                    if (finalIsEqual || u.getId() == finalMaxId) {
                        int rating = u.getRating();

                        u.setPrize(u.getPrize() + 1);
                        u.setRating(rating == 2000 ? 2000 : rating + 50);

                        sendMessageRq.setContent(appointMessage);
                    } else {
                        sendMessageRq.setContent(resultMessage);
                    }
                    sendMessageRq.setMessageProperty(Message.MessageProperty.PRIZE);
                } else if (vote.getVoteProperty() == Vote.VoteProperty.CAUTION) {
                    if (finalIsEqual || u.getId() == finalMaxId) {
                        int rating = u.getRating();

                        u.setCaution(u.getCaution() + 1);
                        u.setRating(rating == 0 ? 0 : rating - 50);

                        sendMessageRq.setContent(appointMessage);
                    } else {
                        sendMessageRq.setContent(resultMessage);
                    }
                    sendMessageRq.setMessageProperty(Message.MessageProperty.CAUTION);
                } else {
                    if (gathering.getUserCount() / 2 + 1 <= finalMaxCount) {
                        if (u.getId() == vote.getConvictionUserId()) {
                            int rating = u.getRating();

                            u.setConviction(u.getConviction() + 1);
                            u.setRating(rating == 0 ? 0 : rating - 100);

                            sendMessageRq.setContent("죄를 지은 사람으로 선정되었습니다.");
                        } else {
                            sendMessageRq.setContent(
                                    "죄를 지은 사람은 " + finalConvictionUsername + " 입니다.\n" +
                                            "보낸 메시지 내용: \n" + vote.getConvictionMessage()
                            );
                        }
                    } else {
                        sendMessageRq.setContent("피고의 무죄가 입증되었습니다.");
                    }
                    sendMessageRq.setMessageProperty(Message.MessageProperty.CONVICTION);
                }

                messageService.sendMessage(sendMessageRq, gathering, u);
            });

            voteRepository.delete(vote);
            return true;
        }

        return false;
    }
}
