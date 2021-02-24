package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final Logger logger = Logger.getLogger(LoginService.class);

    private final UserService userService;

    @Autowired
    public LoginService(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try auth with user-form: " + loginForm);
//        return userService.checkUserExists(loginForm.getUsername(), loginForm.getPassword());
        return loginForm.getUsername().equals("root") && loginForm.getPassword().equals("123");
    }
}
