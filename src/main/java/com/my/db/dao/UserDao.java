package com.my.db.dao;

import com.my.db.entity.User;
import com.my.exception.DaoException;

public interface UserDao {
    void saveUser(User user) throws DaoException;

    User getUserByLogin(String login, String password);

    void buyTickets(long userId, long exhibitionId, int amountOfTickets) throws DaoException;
}
