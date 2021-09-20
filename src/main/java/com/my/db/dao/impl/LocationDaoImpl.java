package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.LocationDao;
import com.my.db.entity.Location;
import com.my.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocationDaoImpl implements LocationDao {

    private static LocationDaoImpl instance;
    private DataSource dataSource;

    private LocationDaoImpl() {
        dataSource = DataSourceUtil.getDataSource();
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
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_LOCATION, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, location.getName());

            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    location.setId(resultSet.getLong(1));
                }
            }

            for (Map.Entry<String, String> addressLanguage : location.getAddress().entrySet()){
                addLocationAddress(connection, location.getId(), addressLanguage.getKey(), addressLanguage.getValue());
            }

            connection.commit();

        } catch (SQLException e) {

            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Transaction rollback\t" + e.getMessage());

            return false;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }
        return true;
    }


    private void addLocationAddress(Connection connection, long locationId, String language, String address) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_LOCATION_ADDRESS);

            int i = 1;
            preparedStatement.setLong(i++, locationId);
            preparedStatement.setString(i++, language);
            preparedStatement.setString(i, address);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
            throw new SQLException();

        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }
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

            for (Location location: locations) {
                location.setAddress(getAddressByLocationId(location.getId()));
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

    private Map<String, String> getAddressByLocationId(long locationId) {
        Map<String, String> address = new LinkedHashMap<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_ADDRESS_BY_LOCATION_ID)) {

            preparedStatement.setLong(1, locationId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                address.put(resultSet.getString(Constants.SQL_FIELD_SHORT_NAME), resultSet.getString(Constants.SQL_FIELD_ADDRESS));
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
        return address;
    }

    private Location mapLocation(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setId(rs.getLong(Constants.SQL_FIELD_ID));
        location.setName(rs.getString(Constants.SQL_FIELD_NAME));

        return location;
    }

}
