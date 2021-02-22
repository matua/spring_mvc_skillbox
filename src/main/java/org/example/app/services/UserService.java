package org.example.app.services;

import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final ProjectRepository<User> userRepo;

    @Autowired
    public UserService(ProjectRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.retreiveAll();
    }

    public void saveUser(User user) {
        userRepo.store(user);
    }

    public boolean removeUserById(String userIdToRemove) {
        return userRepo.removeItemById(userIdToRemove);
    }

    public boolean checkUserExists(String username, String password) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
