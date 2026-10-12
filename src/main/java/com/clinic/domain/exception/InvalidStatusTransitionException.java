package com.clinic.domain.exception;

public class InvalidStatusTransitionException extends DomainException {

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
