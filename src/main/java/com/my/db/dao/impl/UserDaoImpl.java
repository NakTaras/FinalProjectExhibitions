package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.UserDao;
import com.my.db.entity.User;
import com.my.exception.DaoException;
import com.my.servlet.Controller;
import com.my.util.DataSourceUtil;
import com.my.util.PasswordEncryption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    private static UserDaoImpl instance;
    private DataSource dataSource;

    private UserDaoImpl() {
        dataSource = DataSourceUtil.getDataSource();
    }

    public static synchronized UserDaoImpl getInstance() {
            if (instance == null) {
                instance = new UserDaoImpl();
            }
        return instance;
    }

    @Override
    public void saveUser(User user) throws DaoException {
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
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
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }

    @Override
    public User getUserByLogin(String login, String password) {
        User user = null;
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_USER_BY_LOGIN)) {
            int i = 1;
            preparedStatement.setString(i++, login);
            preparedStatement.setString(i, PasswordEncryption.encrypt(password));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = mapUser(resultSet);
            }

        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
        return user;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(Constants.SQL_FIELD_ID));
        user.setLogin(rs.getString(Constants.SQL_FIELD_LOGIN));
        user.setRole(rs.getString(Constants.SQL_FIELD_NAME));
        return user;
    }

    @Override
    public void buyTickets(long userId, long exhibitionId, int amountOfTickets) throws DaoException {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_BUY_TICKETS)) {
            int i = 1;
            preparedStatement.setLong(i++, userId);
            preparedStatement.setLong(i++, exhibitionId);
            preparedStatement.setInt(i++, amountOfTickets);
            preparedStatement.setInt(i, amountOfTickets);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Cannot buy tickets!", e);
            throw new DaoException("Cannot buy tickets!", e);
        }

    }
}