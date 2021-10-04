package com.my.db.dao;

import com.my.db.entity.User;
import com.my.exception.DaoException;

/**
 * Interface UserDao is used to access information about users from the database.
 * @author Taras Nakonechnyi
 */
public interface UserDao {
    /**
     * The method adds information about the user to the database.
     * @param user - information about the user that needs to be saved in the database.
     * @throws DaoException when the user could not be saved to the database.
     */
    void saveUser(User user) throws DaoException;

    /**
     * The method gets information about the user from the database.
     * @param login - user login.
     * @param password - user password.
     */
    User getUserByLogin(String login, String password);

    /**
     * The method adds information that user has bought tickets for exhibition.
     * @param userId - user id who bought the tickets.
     * @param exhibitionId - exhibition for which tickets were bought.
     * @param amountOfTickets - amount of tickets that the user bought.
     * @throws DaoException when the information could not be saved to the database.
     */
    void buyTickets(long userId, long exhibitionId, int amountOfTickets) throws DaoException;
}
