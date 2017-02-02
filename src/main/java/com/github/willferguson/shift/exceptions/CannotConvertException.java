package com.github.willferguson.shift.exceptions;

/**
 * Created by will on 16/03/16.
 */
public class CannotConvertException extends RuntimeException {

    public CannotConvertException() {
        super();
    }

    public CannotConvertException(String message) {
        super(message);
    }

    public CannotConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotConvertException(Throwable cause) {
        super(cause);
    }
}
