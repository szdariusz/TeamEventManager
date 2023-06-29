package com.darius.teamEventManager.exceptions;

import static com.darius.teamEventManager.payload.response.ResponseMessages.NOT_FOUND_QUEUE_ELEMENT;

public class NotFoundQueueElementException extends RuntimeException {
    @Override
    public String getMessage() {
        return NOT_FOUND_QUEUE_ELEMENT;
    }
}
