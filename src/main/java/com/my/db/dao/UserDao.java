package com.my.db.dao;

import com.my.db.entity.User;

import java.util.List;

public interface UserDao {
    boolean saveUser(User user);

    User getUserByLogin(String login, String password);

    List<User> getAllUsers();
}
