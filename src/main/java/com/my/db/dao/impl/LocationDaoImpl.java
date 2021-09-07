package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.LocationDao;
import com.my.db.entity.Exhibition;
import com.my.db.entity.Location;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDaoImpl implements LocationDao {

    private static LocationDaoImpl instance;
    private DataSource dataSource;

    private LocationDaoImpl() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/TestDB");
        } catch (NamingException ex) {
            throw new IllegalStateException("Cannot init DBManager", ex);
        }
    }

    public static synchronized LocationDaoImpl getInstance() {
        if (instance == null) {
            instance = new LocationDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean saveLocation(Location location) {
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_ADD_LOCATION, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, location.getName());
            preparedStatement.setString(i, location.getAddress());

            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    location.setId(resultSet.getLong(1));
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
    public Location getLocationById(long id) {
        return null;
    }

    @Override
    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(Constants.SQL_GET_ALL_LOCATION)){
            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return locations;
    }

    public List<Location> getLocationsByExhibitionId(long exhibitionId){
        List<Location> locations = new ArrayList<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_LOCATION_BY_EXHIBITION_ID)) {

            preparedStatement.setLong(1, exhibitionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                locations.add(mapLocation(resultSet));
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
        return locations;
    }

    private Location mapLocation(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setId(rs.getLong(Constants.SQL_FIELD_ID));
        location.setName(rs.getString(Constants.SQL_FIELD_NAME));
        location.setAddress(rs.getString(Constants.SQL_FIELD_ADDRESS));
        return location;
    }
}
