package com.darius.teamEventManager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "waiting_users")
public class TEMUserWaitingToJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "tem_user_id")
    private TEMUser temUser;
    @OneToOne
    @JoinColumn(name = "team_event_id")
    private TeamEvent teamEvent;

    protected TEMUserWaitingToJoin() {
    }

    public TEMUserWaitingToJoin(TEMUser temUser, TeamEvent teamEvent) {
        this.temUser = temUser;
        this.teamEvent = teamEvent;
    }
}
