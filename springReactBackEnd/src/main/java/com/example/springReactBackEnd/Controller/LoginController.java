package com.example.springReactBackEnd.Controller;

import com.example.springReactBackEnd.DTO.ProviderDTO;
import com.example.springReactBackEnd.Entity.Users;
import com.example.springReactBackEnd.Exception.LoginException;
import com.example.springReactBackEnd.Response.ResponseHandler;
import com.example.springReactBackEnd.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Authentication")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<Object> signup(@RequestBody Users user) {
        try {
            boolean isRegistered = loginService.registerUser(user);
            if (isRegistered) {
                return ResponseHandler.getResponse("User created successfully", HttpStatus.CREATED, user);
            } else {
                return ResponseHandler.getResponse("Username already exists", HttpStatus.BAD_REQUEST, user);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseHandler.getResponse("Error occurred during registration: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, user);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Users user) {
        try {
            boolean isAuthenticated = loginService.loginUser(user.getUserName(), user.getPassword());
            if (isAuthenticated) {
                return ResponseHandler.getResponse("User logged in successfully", HttpStatus.OK, user);
            } else {
                return ResponseHandler.getResponse("Invalid username or password", HttpStatus.UNAUTHORIZED, user);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseHandler.getResponse("Error occurred during login: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, user);
        }
    }
}
