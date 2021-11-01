package com.lcc.monastery.service.business;

import com.lcc.monastery.advice.exception.UserNotFoundException;
import com.lcc.monastery.dto.user.FindUserRq;
import com.lcc.monastery.dto.user.FindUserRs;
import com.lcc.monastery.entity.*;
import com.lcc.monastery.repository.*;
import com.lcc.monastery.util.AESCrypto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("spring.jwt.secret")
    private String secretKey;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final UserGatheringRepository userGatheringRepository;
    private final VoteRepository voteRepository;
    private final UserVoteRepository userVoteRepository;

    private final AESCrypto aesCrypto;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public FindUserRs findUser(User user) {
        return FindUserRs.builder()
                .username(user.getUsername())
                .rating(user.getRating())
                .prize(user.getPrize())
                .caution(user.getCaution())
                .conviction(user.getConviction())
                .build();
    }

    public User findUser(String authorization) {
        Claims claims =
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authorization.substring(7)).getBody();

        return userRepository.findById(Long.parseLong(claims.getSubject())).orElseThrow(UserNotFoundException::new);
    }

    public User findUser(String username, String phoneNumber) {
        return userRepository.findByPhoneNumberAndUsername(phoneNumber, username).orElseThrow(UserNotFoundException::new);
    }

    public User findUser(FindUserRq findUserRq) {
        return findUser(findUserRq.getUsername(), findUserRq.getPhoneNumber());
    }

    public User findUser(long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public List<FindUserRs> findUsersByGathering(Long gatheringId, String authorization) {
        User user = findUser(authorization);

        List<UserGathering> userGatherings = userGatheringRepository.findByGatheringId(gatheringId);
        List<FindUserRs> result = new ArrayList<>();

        userGatherings.forEach(g -> {
            User u = g.getUser();
            result.add(
                    FindUserRs.builder()
                            .id(aesCrypto.encrypt("AES/CBC/PKCS5Padding", String.valueOf(u.getId())))
                            .username(u.getUsername())
                            .isSelf(user.getId() == u.getId())
                            .build()
            );
        });

        return result;
    }

    @Transactional
    public void createUser(String username, String password, String phoneNumber) {
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .rating(1000)
                .prize(0)
                .caution(0)
                .conviction(0)
                .role(Collections.singletonList("ROLE_USER"))
                .build());
    }

    @Transactional
    public void deleteUser(String authorization) {
        User user = findUser(authorization);

        List<UserGathering> userGatherings =
                userGatheringRepository.findByUser(user);

        userGatherings.forEach(ug -> withdrawFromGathering(user, ug));

        userRepository.delete(user);
    }

    @Transactional
    public void withdrawFromGathering(User user, UserGathering userGathering) {
        Gathering gathering = userGathering.getGathering();

        if (gathering.getUserCount() - 1 == 0) {
            gatheringRepository.delete(gathering);
        } else {
            gathering.setUserCount(gathering.getUserCount() - 1);

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
