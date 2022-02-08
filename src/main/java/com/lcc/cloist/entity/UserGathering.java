package com.lcc.cloist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_gathering")
public class UserGathering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (user_id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (gathering_id) references gathering (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Gathering gathering;
}