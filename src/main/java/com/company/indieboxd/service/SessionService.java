package com.company.indieboxd.service;

import com.company.indieboxd.model.User;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
}