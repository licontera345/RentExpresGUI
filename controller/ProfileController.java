package com.pinguela.rentexpres.desktop.controller;

import java.util.List;

import com.pinguela.rentexpres.desktop.util.AppContext;
import com.pinguela.rentexpres.model.UserDTO;
import com.pinguela.rentexpres.service.UserService;

/** Controller for user profile operations. */
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /** Returns the current user refreshed from persistence. */
    public UserDTO getCurrentUser() throws Exception {
        UserDTO current = AppContext.getCurrentUser();
        if (current == null) return null;
        UserDTO refreshed = userService.findById(current.getId());
        AppContext.setCurrentUser(refreshed);
        return refreshed;
    }

    public List<String> getUserImages(int userId) throws Exception {
        return userService.getUserImages(userId);
    }

    /** Updates the user and refreshes the current user context. */
    public UserDTO updateUser(UserDTO updated) throws Exception {
        userService.update(updated);
        UserDTO refreshed = userService.findById(updated.getId());
        AppContext.setCurrentUser(refreshed);
        return refreshed;
    }
}
