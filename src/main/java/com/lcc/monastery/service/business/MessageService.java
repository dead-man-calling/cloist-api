package com.lcc.monastery.service.business;

import com.lcc.monastery.advice.exception.GatheringNotFoundException;
import com.lcc.monastery.advice.exception.MessageNotFoundException;
import com.lcc.monastery.advice.exception.NotAuthorizedException;
import com.lcc.monastery.dto.message.FindMessageRs;
import com.lcc.monastery.dto.message.SendMessageRq;
import com.lcc.monastery.entity.Gathering;
import com.lcc.monastery.entity.Message;
import com.lcc.monastery.entity.User;
import com.lcc.monastery.repository.GatheringRepository;
import com.lcc.monastery.repository.MessageRepository;
import com.lcc.monastery.util.AESCrypto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final GatheringRepository gatheringRepository;
    private final MessageRepository messageRepository;

    private final AESCrypto aesCrypto;
    private final UserService userService;

    public Long getMessageCount(Long userId, Long gatheringId) {
        return messageRepository.countByUserIdAndGatheringId(userId, gatheringId);
    }

    public Long getMessageCount(Long gatheringId, String authorization) {
        User user = userService.findUser(authorization);

        return messageRepository.countByUserIdAndGatheringId(user.getId(), gatheringId);
    }

    public List<FindMessageRs> findMessages(Long gatheringId, String authorization) {
        User user = userService.findUser(authorization);

        List<Message> messages =
                messageRepository.findByUserIdAndGatheringIdOrderByCreatedAtDesc(user.getId(), gatheringId);

        List<FindMessageRs> result = new ArrayList<>();

        messages.forEach(m -> result.add(
                FindMessageRs.builder()
                        .id(m.getId())
                        .sentUserId(m.getSentUserId())
                        .content(m.getContent())
                        .messageProperty(m.getMessageProperty())
                        .build()
        ));

        return result;
    }

    public List<FindMessageRs> findSentMessages(Long gatheringId, String authorization) {
        User user = userService.findUser(authorization);

        List<Message> messages =
                messageRepository.findBySentUserIdAndGatheringIdOrderByCreatedAtDesc(user.getId(), gatheringId);

        List<FindMessageRs> result = new ArrayList<>();

        messages.forEach(m ->
                result.add(
                        FindMessageRs.builder()
                                .id(m.getId())
                                .username(m.getUsername())
                                .content(m.getContent())
                                .messageProperty(m.getMessageProperty())
                                .build()
                ));

        return result;
    }

    @Transactional
    public void sendMessage(SendMessageRq sendMessageRq, String authorization) {
        User user = userService.findUser(authorization);

        long decryptUserId = Long.parseLong(
                aesCrypto.decrypt(
                        "AES/CBC/PKCS5Padding",
                        sendMessageRq.getUserId())
        );

        User receiveUser = userService.findUser(decryptUserId);

        Gathering gathering =
                gatheringRepository
                        .findById(sendMessageRq.getGatheringId())
                        .orElseThrow(GatheringNotFoundException::new);

        messageRepository.save(Message.builder()
                .userId(decryptUserId)
                .username(receiveUser.getUsername())
                .gathering(gathering)
                .content(sendMessageRq.getContent())
                .sentUserId(user.getId())
                .messageProperty(sendMessageRq.getMessageProperty())
                .build());
    }

    @Transactional
    public void sendMessage(SendMessageRq sendMessageRq, Gathering gathering, User receiveUser) {
        messageRepository.save(Message.builder()
                .userId(Long.parseLong(sendMessageRq.getUserId()))
                .username(receiveUser.getUsername())
                .gathering(gathering)
                .content(sendMessageRq.getContent())
                .sentUserId(-1)
                .messageProperty(sendMessageRq.getMessageProperty())
                .build());
    }

    @Transactional
    public void deleteMessage(Long id, String authorization) {
        User user = userService.findUser(authorization);

        Message message =
                messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        if (message.getUserId() != user.getId())
            throw new NotAuthorizedException();

        messageRepository.delete(message);
    }
}
