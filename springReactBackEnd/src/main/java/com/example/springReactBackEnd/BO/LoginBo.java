package com.example.springReactBackEnd.BO;

import com.example.springReactBackEnd.Entity.Users;
import com.example.springReactBackEnd.Exception.LoginException;
import com.example.springReactBackEnd.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class LoginBo {

    @Autowired
    private UserRepository userRepository;

    public Boolean registerUser(Users user) throws LoginException {
        Optional<Users> existingUsers = userRepository.findByUserName(user.getUserName());
        if(existingUsers.isPresent()) {
            throw new LoginException("User already registered");
        }
        userRepository.save(user);
        return true;
    }

    public Boolean loginUser(String userName, String password) throws LoginException {
        Optional<Users> user = userRepository.findByUserNameAndPassword(userName,password);
        if(!user.isPresent()){
            throw new LoginException("Username or password incorrect");
        }
        return true;
    }
}
