package com.lcc.monastery.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message extends CommonDateEntity {
    public enum MessageProperty {
        NORMAL(-1, "NORMAL"),
        PRIZE(0, "PRIZE"),
        CAUTION(1, "CAUTION"),
        CONVICTION(2, "CONVICTION");

        int code;
        String message;

        MessageProperty(int code, String message) {
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

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (gathering_id) references gathering (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Gathering gathering;

    @Column(nullable = false)
    private long sentUserId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private MessageProperty messageProperty;
}
