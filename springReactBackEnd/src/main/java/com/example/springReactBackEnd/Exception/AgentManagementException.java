package com.example.springReactBackEnd.Exception;

public class AgentManagementException extends Exception{
    public AgentManagementException(String message) {
        super(message);
    }

    public AgentManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentManagementException(Throwable cause) {
        super(cause);
    }

    public AgentManagementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
