package com.darius.teamEventManager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "description")
    private String description;
    @Column(name = "likes")
    private int likes;
    @Column(name = "dislikes")
    private int dislikes;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tem_user_id")
    private TEMUser temUser;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tem_event_id")
    private TeamEvent teamEvent;

    protected Comment() {
    }

    public Comment(LocalDate date, String description, int likes, int dislikes, TEMUser temUser, TeamEvent teamEvent) {
        this.date = date;
        this.description = description;
        this.likes = likes;
        this.dislikes = dislikes;
        this.temUser = temUser;
        this.teamEvent = teamEvent;
    }
}
