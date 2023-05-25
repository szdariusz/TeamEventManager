package com.darius.teamEventManager.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "options")
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Column(name = "likes")
    private int likes;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    protected VoteOption() {
    }

    public VoteOption(String description, int likes, Vote vote) {
        this.description = description;
        this.likes = likes;
        this.vote = vote;
    }
}
