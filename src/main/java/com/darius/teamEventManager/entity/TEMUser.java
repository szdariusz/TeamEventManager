package com.darius.teamEventManager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tem_users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class TEMUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @NotBlank(message = "username have to contain non-whitespace characters")
    @Size(min = 3, max = 20, message = "username length should be between 3-20 characters")
    @Column(name = "username")
    private String username;
    @NotBlank(message = "password have to contain non-whitespace characters")
    @Size(min = 8, message = "password is too short")
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Email(message = "incorrect email format")
    @Column(name = "email")
    private String emailAddress;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tem_users_user_roles", joinColumns = {@JoinColumn(name = "tem_user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>();

    protected TEMUser() {
    }

    public TEMUser(String name, String username, String password, String phoneNumber, String emailAddress) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public TEMUser(String username, String email, String password) {
        this.username = username;
        this.emailAddress = email;
        this.password = password;
    }

    public TEMUser(Integer id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.emailAddress = email;
        this.password = password;
    }


    @Override
    public String toString() {
        return "TEMUser{" + "id=" + id + ", name='" + name + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", emailAddress='" + emailAddress + '\'' + ", roles=" + roles + '}';
    }
}
