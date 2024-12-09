package com.example.springReactBackEnd.ExceptionHandler;

import com.example.springReactBackEnd.Exception.AgentManagementException;
import com.example.springReactBackEnd.Exception.AgentNotFoundException;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Exception.ProviderNotFoundException;
import com.example.springReactBackEnd.Response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProviderManagementException.class)
    public ResponseEntity<Object> handleProviderManagementException(ProviderManagementException exception) {
        return ResponseHandler.getResponse("ProviderManagementException: "+exception.getMessage(), HttpStatus.BAD_REQUEST,null);
    }
    @ExceptionHandler(AgentManagementException.class)
    public ResponseEntity<Object> handleAgentManagementException(AgentManagementException exception) {
        return ResponseHandler.getResponse("AgentManagementException: " + exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<Object> handleProviderNotFoundException(ProviderNotFoundException exception) {
        return ResponseHandler.getResponse("ProviderNotFoundException: " + exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }
    @ExceptionHandler(AgentNotFoundException.class)
    public ResponseEntity<Object> handleAgentNotFoundException(AgentNotFoundException exception) {
        return ResponseHandler.getResponse("AgentNotFoundException: " + exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }
}
