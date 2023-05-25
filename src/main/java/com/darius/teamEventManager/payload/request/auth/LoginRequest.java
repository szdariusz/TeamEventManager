package com.darius.teamEventManager.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "username have to contain non-whitespace characters")
    @Size(min = 3, max = 20, message = "username length should be between 3-20 characters")
    private String username;

    @NotBlank(message = "password have to contain non-whitespace characters")
    @Size(min = 8, message = "password is too short")
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password.hashCode() + '\'' +
                '}';
    }
}