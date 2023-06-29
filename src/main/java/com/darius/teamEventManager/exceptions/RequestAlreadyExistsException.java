package com.darius.teamEventManager.exceptions;

import static com.darius.teamEventManager.payload.response.ResponseMessages.ALREADY_REQUESTED;

public class RequestAlreadyExistsException extends RuntimeException {
    @Override
    public String getMessage() {
        return ALREADY_REQUESTED;
    }
}
