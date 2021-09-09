package com.my.db.dao;

import com.my.db.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    boolean saveUser(User user);

    User getUserByLogin(String login, String password);

    List<User> getAllUsers();

    void buyTickets(long userId, long exhibitionId, int amountOfTickets) throws SQLException;
}
