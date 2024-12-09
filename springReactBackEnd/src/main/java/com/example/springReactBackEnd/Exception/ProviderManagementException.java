package com.example.springReactBackEnd.Exception;

public class ProviderManagementException extends Exception{
    public ProviderManagementException(String message) {
        super(message);
    }

    public ProviderManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderManagementException(Throwable cause) {
        super(cause);
    }

    public ProviderManagementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
