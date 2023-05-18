package com.DariusApp.TeamEventManager.Payload.Request.auth;

import lombok.Getter;

@Getter
public class UserIdRequest {
    private Integer userId;

    @Override
    public String toString() {
        return "UserIdRequest{" +
                "userId=" + userId +
                '}';
    }
}
