package com.DariusApp.TeamEventManager.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20)
    private UserRoleTypes name;

    protected Role() {

    }

    public Role(UserRoleTypes name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<TEMUser> temUsers = new HashSet<TEMUser>();
}
