package com.example.springReactBackEnd.Service;

import com.example.springReactBackEnd.BO.LoginBo;
import com.example.springReactBackEnd.Entity.Users;
import com.example.springReactBackEnd.Exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginBo loginBo;

    public Boolean registerUser(Users user) throws LoginException {
        return loginBo.registerUser(user);
    }

    public Boolean loginUser(String userName,String password) throws LoginException {
        return loginBo.loginUser(userName,password);
    }
}
