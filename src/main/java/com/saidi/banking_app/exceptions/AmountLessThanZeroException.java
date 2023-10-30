package com.saidi.banking_app.exceptions;

public class AmountLessThanZeroException extends Exception{
    public AmountLessThanZeroException() {
        super();
    }

    public AmountLessThanZeroException(String message) {
        super(message);
    }

    public AmountLessThanZeroException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountLessThanZeroException(Throwable cause) {
        super(cause);
    }

    protected AmountLessThanZeroException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
