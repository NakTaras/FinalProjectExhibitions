package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.LocationDao;
import com.my.db.entity.Location;
import com.my.exception.DaoException;
import com.my.util.DataSourceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocationDaoImpl implements LocationDao {

    private static final Logger logger = LogManager.getLogger(LocationDaoImpl.class);

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
    public void saveLocation(Location location) throws DaoException{
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
            connection.setAutoCommit(true);
        } catch (SQLException | DaoException e) {

            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
            logger.error("Cannot save location!", e);

            throw new DaoException("Cannot save location!", e);

        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }

        }
    }


    private void addLocationAddress(Connection connection, long locationId, String language, String address) throws DaoException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_LOCATION_ADDRESS);

            int i = 1;
            preparedStatement.setLong(i++, locationId);
            preparedStatement.setString(i++, language);
            preparedStatement.setString(i, address);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {

            logger.error("Cannot add location address!", ex);
            throw new DaoException("Cannot add location address!", ex);

        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }

        }
    }


    @Override
    public List<Location> getAllLocations() throws DaoException{
        List<Location> locations = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(Constants.SQL_GET_ALL_LOCATION)){
            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        } catch (SQLException e) {
            logger.error("Cannot get locations!", e);
            throw new DaoException("Cannot get locations!", e);
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
        return address;
    }

    private Location mapLocation(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setId(rs.getLong(Constants.SQL_FIELD_ID));
        location.setName(rs.getString(Constants.SQL_FIELD_NAME));

        return location;
    }

}
