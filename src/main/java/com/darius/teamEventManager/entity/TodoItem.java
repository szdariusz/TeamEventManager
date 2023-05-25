package com.darius.teamEventManager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "todo_items")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unit")
    private String unit;
    //private enum Unit{ml,cl,dl,l,hl,mm,cm,dm,m,km,g,dkg,kg,t}
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "todolist_id")
    private TodoList todoList;

    protected TodoItem() {
    }

    public TodoItem(Integer id, String description, int quantity) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
    }
}

