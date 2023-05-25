package com.darius.teamEventManager.payload.request.auth;

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
