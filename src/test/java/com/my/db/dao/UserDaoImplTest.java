package com.my.db.dao;

import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.User;
import com.my.exception.DaoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.*;

public class UserDaoImplTest {

    @InjectMocks
    private UserDaoImpl userDao;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private User user;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setId(1);
        user.setLogin("Login");
        user.setPassword("Password");
        user.setRole("Role");
    }

    @Test
    public void saveUserTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("INSERT INTO user (login, password, role_id) VALUES (?, ?, (SELECT id FROM role WHERE name = ?));", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        userDao.saveUser(user);
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void buyTicketsTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("INSERT INTO user_has_exhibition (user_id, exhibition_id, amount_of_bought_tickets) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE amount_of_bought_tickets = user_has_exhibition.amount_of_bought_tickets + ?;")).thenReturn(preparedStatement);
        userDao.buyTickets(user.getId(), 1, 3);
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void getUserByLoginTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT user.id, user.login, role.name FROM user INNER JOIN role ON user.role_id = role.id WHERE login = ? AND password = ?;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("login")).thenReturn("Login");
        Mockito.when(resultSet.getString("name")).thenReturn("Role");

        User user = userDao.getUserByLogin("Login", "Password");
        Assert.assertEquals(1L, user.getId());
        Assert.assertEquals("Login", user.getLogin());
        Assert.assertEquals("Role", user.getRole());
    }
}
