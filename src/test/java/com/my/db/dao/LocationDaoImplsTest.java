package com.my.db.dao;

import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.entity.Location;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.atLeast;

public class LocationDaoImplsTest {
    @InjectMocks
    private LocationDaoImpl locationDao;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private PreparedStatement innerPreparedStatement;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet innerResultSet;

    private Location location;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        Map<String, String> address = new LinkedHashMap<>();
        address.put("uk", "Адреса");

        location = new Location();
        location.setId(1);
        location.setName("Name");
        location.setAddress(address);
    }

    @Test
    public void saveLocationTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("INSERT INTO location (name) VALUES (?);", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(1)).thenReturn(2L);
        Mockito.when(connection.prepareStatement("INSERT INTO location_address_has_language (location_id, language_id, address) VALUES (?, (SELECT id FROM language WHERE short_name = ?), ?);")).thenReturn(preparedStatement);

        locationDao.saveLocation(location);
        Mockito.verify(preparedStatement, atLeast(2)).executeUpdate();
        Assert.assertEquals(2L, location.getId());
    }

    @Test
    public void getAllLocationsTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery("SELECT * FROM location;")).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("Name");


        List<Location> locations = locationDao.getAllLocations();
        Mockito.verify(statement).executeQuery("SELECT * FROM location;");
        Assert.assertEquals(1L, locations.get(0).getId());
        Assert.assertEquals("Name", locations.get(0).getName());
    }

    @Test
    public void getLocationsByExhibitionIdTest() throws SQLException, DaoException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT id, name FROM location INNER JOIN exhibition_has_location ehl on location.id = ehl.location_id WHERE exhibition_id = ?;")).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getString("name")).thenReturn("Name");

        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement("SELECT short_name, address FROM location INNER JOIN location_address_has_language lahl on location.id = lahl.location_id INNER JOIN language l on lahl.language_id = l.id WHERE location_id = ?;")).thenReturn(innerPreparedStatement);
        Mockito.when(innerPreparedStatement.executeQuery()).thenReturn(innerResultSet);
        Mockito.when(innerResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(innerResultSet.getString("short_name")).thenReturn("uk");
        Mockito.when(innerResultSet.getString("address")).thenReturn("Адреса");

        List<Location> locations = locationDao.getLocationsByExhibitionId(1);
        Mockito.verify(preparedStatement, atLeast(1)).executeQuery();
        Assert.assertEquals("Name", locations.get(0).getName());
        Assert.assertEquals("Адреса", locations.get(0).getAddress().get("uk"));
    }
}
