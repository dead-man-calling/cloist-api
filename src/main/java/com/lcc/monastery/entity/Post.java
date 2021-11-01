package com.lcc.monastery.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post extends CommonDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (gathering_id) references gathering (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Gathering gathering;

    @Column
    private String title;

    @Column
    private String content;
}
