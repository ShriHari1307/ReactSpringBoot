package com.example.springReactBackEnd.Exception;

public class AgentNotFoundException extends Exception{
    public AgentNotFoundException(String message) {
        super(message);
    }

    public AgentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentNotFoundException(Throwable cause) {
        super(cause);
    }

    public AgentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
