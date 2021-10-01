package com.my.db.dao;

import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.entity.Exhibition;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.atLeast;

public class ExhibitionDaoImplTest {

    @InjectMocks
    private ExhibitionDaoImpl exhibitionDao;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    private Exhibition exhibition;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        byte[] posterImg = {'a'};
        exhibition = new Exhibition();

        exhibition.setId(1);
        exhibition.setTopic("Topic");
        exhibition.setDescription("Description");
        exhibition.setStartTimestamp(Timestamp.valueOf("2002-05-12 10:00:00"));
        exhibition.setStartTimestamp(Timestamp.valueOf("2002-05-15 17:00:00"));
        exhibition.setPrice(100);
        exhibition.setPosterImg(posterImg);
        exhibition.setStatus(1);
    }

    @Test
    public void saveExhibitionTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("INSERT INTO exhibition (topic, description, start_date_time, end_date_time, price, poster_img) VALUES (?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(1)).thenReturn(2L);
        Mockito.when(connection.prepareStatement("INSERT INTO exhibition_has_location (exhibition_id, location_id) VALUES (?, ?);")).thenReturn(preparedStatement);

        exhibitionDao.saveExhibition(exhibition, new String[] {"1"});
        Mockito.verify(preparedStatement, atLeast(2)).executeUpdate();
        Assert.assertEquals(2L, exhibition.getId());
    }

    @Test
    public void getExhibitionByIdTest() throws SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT * FROM exhibition WHERE id = ?;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getString("description")).thenReturn("Description");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);
        Mockito.when(resultSet.getBytes("poster_img")).thenReturn(new byte[] {'a'});
        Mockito.when(resultSet.getInt("status")).thenReturn(1);

        Exhibition exhibition = exhibitionDao.getExhibitionById(2);
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(2L, exhibition.getId());
        Assert.assertEquals("Topic", exhibition.getTopic());
        Assert.assertEquals("Description", exhibition.getDescription());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibition.getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibition.getEndTimestamp());
        Assert.assertEquals(100.0, exhibition.getPrice(), 0.5);
        Assert.assertEquals("[97]", Arrays.toString(exhibition.getPosterImg()));
        Assert.assertEquals(1, exhibition.getStatus());
    }

    @Test
    public void getAllExhibitionsTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery("SELECT * FROM exhibition;")).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getString("description")).thenReturn("Description");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);
        Mockito.when(resultSet.getBytes("poster_img")).thenReturn(new byte[] {'a'});
        Mockito.when(resultSet.getInt("status")).thenReturn(1);

        List<Exhibition> exhibitions = exhibitionDao.getAllExhibitions();
        Mockito.verify(statement).executeQuery("SELECT * FROM exhibition;");
        Assert.assertEquals(2L, exhibitions.get(0).getId());
        Assert.assertEquals("Topic", exhibitions.get(0).getTopic());
        Assert.assertEquals("Description", exhibitions.get(0).getDescription());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibitions.get(0).getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibitions.get(0).getEndTimestamp());
        Assert.assertEquals(100.0, exhibitions.get(0).getPrice(), 0.5);
        Assert.assertEquals("[97]", Arrays.toString(exhibitions.get(0).getPosterImg()));
        Assert.assertEquals(1, exhibitions.get(0).getStatus());
    }

    @Test
    public void getExhibitionsOnPageByDefaultTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT * FROM exhibition WHERE status = 1 LIMIT ?, 2;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getString("description")).thenReturn("Description");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);
        Mockito.when(resultSet.getBytes("poster_img")).thenReturn(new byte[] {'a'});
        Mockito.when(resultSet.getInt("status")).thenReturn(1);

        List<Exhibition> exhibitions = exhibitionDao.getExhibitionsOnPageByDefault(1);
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(2L, exhibitions.get(0).getId());
        Assert.assertEquals("Topic", exhibitions.get(0).getTopic());
        Assert.assertEquals("Description", exhibitions.get(0).getDescription());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibitions.get(0).getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibitions.get(0).getEndTimestamp());
        Assert.assertEquals(100.0, exhibitions.get(0).getPrice(), 0.5);
        Assert.assertEquals("[97]", Arrays.toString(exhibitions.get(0).getPosterImg()));
        Assert.assertEquals(1, exhibitions.get(0).getStatus());
    }

    @Test
    public void getExhibitionsOnPageByPriceTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT * FROM exhibition WHERE status = 1 ORDER BY price LIMIT ?, 2;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getString("description")).thenReturn("Description");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);
        Mockito.when(resultSet.getBytes("poster_img")).thenReturn(new byte[] {'a'});
        Mockito.when(resultSet.getInt("status")).thenReturn(1);

        List<Exhibition> exhibitions = exhibitionDao.getExhibitionsOnPageByPrice(1);
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(2L, exhibitions.get(0).getId());
        Assert.assertEquals("Topic", exhibitions.get(0).getTopic());
        Assert.assertEquals("Description", exhibitions.get(0).getDescription());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibitions.get(0).getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibitions.get(0).getEndTimestamp());
        Assert.assertEquals(100.0, exhibitions.get(0).getPrice(), 0.5);
        Assert.assertEquals("[97]", Arrays.toString(exhibitions.get(0).getPosterImg()));
        Assert.assertEquals(1, exhibitions.get(0).getStatus());
    }

    @Test
    public void getExhibitionsOnPageByTopicTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT * FROM exhibition WHERE status = 1 ORDER BY topic LIMIT ?, 2;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getString("description")).thenReturn("Description");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);
        Mockito.when(resultSet.getBytes("poster_img")).thenReturn(new byte[] {'a'});
        Mockito.when(resultSet.getInt("status")).thenReturn(1);

        List<Exhibition> exhibitions = exhibitionDao.getExhibitionsOnPageByTopic(1);
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(2L, exhibitions.get(0).getId());
        Assert.assertEquals("Topic", exhibitions.get(0).getTopic());
        Assert.assertEquals("Description", exhibitions.get(0).getDescription());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibitions.get(0).getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibitions.get(0).getEndTimestamp());
        Assert.assertEquals(100.0, exhibitions.get(0).getPrice(), 0.5);
        Assert.assertEquals("[97]", Arrays.toString(exhibitions.get(0).getPosterImg()));
        Assert.assertEquals(1, exhibitions.get(0).getStatus());
    }

    @Test
    public void getExhibitionsOnPageByDateTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT * FROM exhibition WHERE status = 1 AND ? BETWEEN start_date_time AND end_date_time LIMIT ?, 2;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getString("description")).thenReturn("Description");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);
        Mockito.when(resultSet.getBytes("poster_img")).thenReturn(new byte[]{'a'});
        Mockito.when(resultSet.getInt("status")).thenReturn(1);

        List<Exhibition> exhibitions = exhibitionDao.getExhibitionsOnPageByDate(1, Date.valueOf("2002-05-14"));
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(2L, exhibitions.get(0).getId());
        Assert.assertEquals("Topic", exhibitions.get(0).getTopic());
        Assert.assertEquals("Description", exhibitions.get(0).getDescription());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibitions.get(0).getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibitions.get(0).getEndTimestamp());
        Assert.assertEquals(100.0, exhibitions.get(0).getPrice(), 0.5);
        Assert.assertEquals("[97]", Arrays.toString(exhibitions.get(0).getPosterImg()));
        Assert.assertEquals(1, exhibitions.get(0).getStatus());
    }

    @Test
    public void cancelExhibitionByIdTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("UPDATE exhibition SET status = 0 WHERE id = ?;")).thenReturn(preparedStatement);

        exhibitionDao.cancelExhibitionById(1);
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void getAmountOfExhibitionsTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery("SELECT COUNT(*) as 'amount' FROM exhibition WHERE status = 1;")).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getInt("amount")).thenReturn(1);

        int amountOfExhibitions = exhibitionDao.getAmountOfExhibitions();
        Mockito.verify(statement).executeQuery("SELECT COUNT(*) as 'amount' FROM exhibition WHERE status = 1;");
        Assert.assertEquals(1, amountOfExhibitions);
    }

    @Test
    public void getAmountOfExhibitionsByDateTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT COUNT(*) as 'amount' FROM exhibition WHERE status = 1 AND ? BETWEEN start_date_time AND end_date_time;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getInt("amount")).thenReturn(1);

        int amountOfExhibitions = exhibitionDao.getAmountOfExhibitionsByDate(Date.valueOf("2002-05-14"));
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(1, amountOfExhibitions);
    }

    @Test
    public void getAmountOfSoldTicketsByExhibitionIdTest() throws SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT SUM(amount_of_bought_tickets) as 'tickets_amount' FROM user_has_exhibition WHERE exhibition_id = ? GROUP BY exhibition_id;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getInt("tickets_amount")).thenReturn(1);

        int amountOfSoldTickets = exhibitionDao.getAmountOfSoldTicketsByExhibitionId(1);
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(1, amountOfSoldTickets);
    }

    @Test
    public void getExhibitionsStatisticsTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery("SELECT id, topic, start_date_time, end_date_time, price FROM exhibition WHERE status = 1;")).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getLong("id")).thenReturn(2L);
        Mockito.when(resultSet.getString("topic")).thenReturn("Topic");
        Mockito.when(resultSet.getTimestamp("start_date_time")).thenReturn(Timestamp.valueOf("2002-05-12 10:00:00"));
        Mockito.when(resultSet.getTimestamp("end_date_time")).thenReturn(Timestamp.valueOf("2002-05-15 17:00:00"));
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0);

        List<Exhibition> exhibitions = exhibitionDao.getExhibitionsStatistics();
        Mockito.verify(statement).executeQuery("SELECT id, topic, start_date_time, end_date_time, price FROM exhibition WHERE status = 1;");
        Assert.assertEquals(2L, exhibitions.get(0).getId());
        Assert.assertEquals("Topic", exhibitions.get(0).getTopic());
        Assert.assertEquals(Timestamp.valueOf("2002-05-12 10:00:00"), exhibitions.get(0).getStartTimestamp());
        Assert.assertEquals(Timestamp.valueOf("2002-05-15 17:00:00"), exhibitions.get(0).getEndTimestamp());
        Assert.assertEquals(100.0, exhibitions.get(0).getPrice(), 0.5);
    }

    @Test
    public void getDetailedStatisticsByExhibitionIdTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT user.login, amount_of_bought_tickets FROM user_has_exhibition INNER JOIN user ON user.id = user_has_exhibition.user_id WHERE user_has_exhibition.exhibition_id = ?;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getString("login")).thenReturn("Login");
        Mockito.when(resultSet.getInt("amount_of_bought_tickets")).thenReturn(1);

        Map<String, Integer> detailedStatistic = exhibitionDao.getDetailedStatisticsByExhibitionId(1);
        Mockito.verify(preparedStatement).executeQuery();
        Assert.assertEquals(new Integer(1), detailedStatistic.get("Login"));

    }
}
