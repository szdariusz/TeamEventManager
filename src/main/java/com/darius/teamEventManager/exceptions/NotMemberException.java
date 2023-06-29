package com.darius.teamEventManager.exceptions;

import static com.darius.teamEventManager.payload.response.ResponseMessages.NOT_MEMBER;

public class NotMemberException extends RuntimeException {
    @Override
    public String getMessage() {
        return NOT_MEMBER;
    }
}
