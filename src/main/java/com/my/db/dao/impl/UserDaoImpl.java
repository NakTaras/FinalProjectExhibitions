package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.UserDao;
import com.my.db.entity.User;
import com.my.utils.PasswordEncryption;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static UserDaoImpl instance;
    private DataSource dataSource;

    private UserDaoImpl() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/TestDB");
        } catch (NamingException ex) {
            throw new IllegalStateException("Cannot init DBManager", ex);
        }
    }

    public static synchronized UserDaoImpl getInstance() {
            if (instance == null) {
                instance = new UserDaoImpl();
            }
        return instance;
    }

    @Override
    public boolean saveUser(User user) {
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
            System.out.println(ex.getMessage());
            return false;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return true;
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
            System.out.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(Constants.SQL_FIELD_ID));
        user.setLogin(rs.getString(Constants.SQL_FIELD_LOGIN));
        user.setRole(rs.getString(Constants.SQL_FIELD_NAME));
        return user;
    }
}