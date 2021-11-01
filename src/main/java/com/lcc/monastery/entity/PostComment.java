package com.lcc.monastery.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_comment")
public class PostComment extends CommonDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (post_id) references post (id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Post post;

    @Column
    private String content;
}
