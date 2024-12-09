package com.example.springReactBackEnd.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object>getResponse(String responseMessage, HttpStatus status,Object responseObject){
        Map<String,Object> map = new HashMap<>();
        map.put("response", responseMessage);
        map.put("status", status);
        map.put("data", responseObject);
        return new ResponseEntity<>(map, status);
    }
}
