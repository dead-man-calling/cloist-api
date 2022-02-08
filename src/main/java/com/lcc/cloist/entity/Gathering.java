package com.lcc.cloist.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gathering")
public class Gathering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 16)
    private String gatheringName;

    @Column(length = 32)
    private String description;

    @Column
    private int prizeVotes;

    @Column
    private int cautionVotes;

    @Column
    private int convictionVotes;

    @Column(nullable = false)
    private int userCount;
}