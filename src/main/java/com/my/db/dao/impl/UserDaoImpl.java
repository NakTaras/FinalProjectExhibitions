package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.UserDao;
import com.my.db.entity.User;
import com.my.exception.DaoException;
import com.my.util.AutoCloseableClose;
import com.my.util.PasswordEncryption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Class UserDaoImpl is used to access information about users from the database.
 * Class UserDaoImpl implements interface UserDao.
 * @author Taras Nakonechnyi
 */
public class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    /**
     *  Singleton object of class UserDaoImpl
     */
    private static UserDaoImpl instance;

    /**
     * A factory for connections to the database
     */
    private DataSource dataSource;

    /**
     * The private constructor that called from the method getInstance.
     * It should be called exactly once when the first time the method getInstance is called.
     * @param dataSource - factory for connections to the database
     */
    private UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Static method that create instance and call private constructor when the first time the method is called.
     * @param dataSource - factory for connections to the database.
     * @return instance - Singleton object of class UserDaoImpl
     */
    public static synchronized UserDaoImpl getInstance(DataSource dataSource) {
            if (instance == null) {
                instance = new UserDaoImpl(dataSource);
            }
        return instance;
    }

    @Override
    public void saveUser(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            preparedStatement.setString(i++, user.getLogin());
            preparedStatement.setString(i++, PasswordEncryption.encrypt(user.getPassword()));
            preparedStatement.setString(i, user.getRole());

            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException ex) {
            logger.error("Cannot save this user", ex);
            throw new DaoException("Cannot save this user", ex);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }
    }

    @Override
    public User getUserByLogin(String login, String password) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_GET_USER_BY_LOGIN);

            int i = 1;
            preparedStatement.setString(i++, login);
            preparedStatement.setString(i, PasswordEncryption.encrypt(password));
            resultSet = preparedStatement.executeQuery();

           resultSet.next();
           user = mapUser(resultSet);


        } catch (SQLException e) {
            logger.error(e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }
        return user;
    }

    /**
     * The method set information about the user into the object of class User.
     * @param rs - ResultSet with information about the user from the database.
     * @return object of class User with information about the user
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(Constants.SQL_FIELD_ID));
        user.setLogin(rs.getString(Constants.SQL_FIELD_LOGIN));
        user.setRole(rs.getString(Constants.SQL_FIELD_NAME));
        return user;
    }

    @Override
    public void buyTickets(long userId, long exhibitionId, int amountOfTickets) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_BUY_TICKETS);

            int i = 1;
            preparedStatement.setLong(i++, userId);
            preparedStatement.setLong(i++, exhibitionId);
            preparedStatement.setInt(i++, amountOfTickets);
            preparedStatement.setInt(i, amountOfTickets);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Cannot buy tickets!", e);
            throw new DaoException("Cannot buy tickets!", e);
        } finally {
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

    }
}