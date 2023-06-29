package com.darius.teamEventManager.exceptions;

import static com.darius.teamEventManager.payload.response.ResponseMessages.NOT_FOUND_USER;

public class NotFoundTEMUserException extends RuntimeException {
    @Override
    public String getMessage() {
        return NOT_FOUND_USER;
    }
}

