package com.DariusApp.TeamEventManager.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "todolists")
public class TodoList {
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
    @JoinColumn(name = "todo_item_id")
    private TeamEvent teamEvent;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoItem> items;

    protected TodoList() {
    }

    public TodoList(LocalDate date, String description, TEMUser temUser, TeamEvent teamEvent, List<TodoItem> items) {
        this.date = date;
        this.description = description;
        this.temUser = temUser;
        this.teamEvent = teamEvent;
        this.items = items;
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", temUser=" + temUser +
                ", teamEvent=" + teamEvent +
                ", items=" + items +
                '}';
    }
}
