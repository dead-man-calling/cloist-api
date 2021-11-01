package com.lcc.monastery.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vote")
public class Vote extends CommonDateEntity {
    public enum VoteProperty {
        PRIZE(0, "PRIZE"),
        CAUTION(1, "CAUTION"),
        CONVICTION(2, "CONVICTION");

        int code;
        String message;

        VoteProperty(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum ExistProperty {
        EXIST(0, "EXIST"),
        NOT_EXIST(1, "NOT_EXIST"),
        FINISHED(2, "FINISHED");

        int code;
        String message;

        ExistProperty(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (gathering_id) references gathering (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Gathering gathering;

    @Column(nullable = false)
    private long voteCount;

    @Column(nullable = false)
    private VoteProperty voteProperty;

    @Column(nullable = false)
    private ExistProperty existProperty;

    @Column
    private String description;

    @Column
    private long convictionUserId;

    @Column
    private String convictionMessage;

    @Column
    private String convictionOpinion;
}
