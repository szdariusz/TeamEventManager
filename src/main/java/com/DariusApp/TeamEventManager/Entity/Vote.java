package com.DariusApp.TeamEventManager.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "description")
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tem_user_id")
    private TEMUser temUser;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_event_id")
    private TeamEvent teamEvent;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoteOption> options;

    protected Vote() {
    }

    public Vote(LocalDate date, String description, TEMUser temUser, TeamEvent teamEvent, List<VoteOption> options) {
        this.date = date;
        this.description = description;
        this.temUser = temUser;
        this.teamEvent = teamEvent;
        this.options = options;
    }
}
