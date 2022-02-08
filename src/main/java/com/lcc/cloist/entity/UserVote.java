package com.lcc.cloist.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_vote")
public class UserVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (user_id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (vote_id) references vote (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Vote vote;

    @Column(nullable = false)
    private boolean isAgreed;

    @Column(nullable = false)
    private long receivedUserId;
}
