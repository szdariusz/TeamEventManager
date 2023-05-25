package com.darius.teamEventManager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "team_events")
public class TeamEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    @Size(min = 3, max = 20, message = "The length of the event name should be 3-20 characters")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "location")
    private String location;
    @Column(name = "is_public")
    private boolean isPublic;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Vote> votes;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<TodoList> todolists;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_events_tem_users",
            joinColumns = {
                    @JoinColumn(name = "team_event_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "tem_user_id", referencedColumnName = "id")})
    private Set<TEMUser> temUsers = new HashSet<>();
    private Integer creatorId;

    public void addTemUser(TEMUser user) {
        this.temUsers.add(user);
    }

    public void removeTemUser(TEMUser user) {
        this.temUsers.remove(user);
    }

    protected TeamEvent() {
    }

    public TeamEvent(String name, String description, LocalDate date, String location, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isPublic = isPublic;
        this.votes = new HashSet<>();
        this.comments = new HashSet<>();
        this.todolists = new HashSet<>();
        this.temUsers = new HashSet<>();
    }
}
