package com.DariusApp.TeamEventManager.Payload.Request;

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
