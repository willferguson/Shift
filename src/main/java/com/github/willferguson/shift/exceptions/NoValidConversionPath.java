package com.github.willferguson.shift.exceptions;

/**
 * Thrown when no converter exists to convert between the required version
 * Created by will on 12/03/2016.
 */
public class NoValidConversionPath extends RuntimeException {

    public NoValidConversionPath() {
        super();
    }

    public NoValidConversionPath(String message) {
        super(message);
    }

    public NoValidConversionPath(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValidConversionPath(Throwable cause) {
        super(cause);
    }
}
