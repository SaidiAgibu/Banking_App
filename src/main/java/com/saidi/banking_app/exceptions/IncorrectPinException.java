package com.saidi.banking_app.exceptions;

public class IncorrectPinException extends Exception{
    public IncorrectPinException() {
        super();
    }

    public IncorrectPinException(String message) {
        super(message);
    }

    public IncorrectPinException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectPinException(Throwable cause) {
        super(cause);
    }

    protected IncorrectPinException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
