package com.Backend.Back.dao;

import com.Backend.Back.entity.User;

public class RegistrationRequest {

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    private User user;
    private String roleName;





}
