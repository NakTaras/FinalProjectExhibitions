package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.ExhibitionDao;
import com.my.db.entity.Exhibition;
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

public class ExhibitionDaoImpl implements ExhibitionDao {

    private static final Logger logger = LogManager.getLogger(LocationDaoImpl.class);

    private static ExhibitionDaoImpl instance;
    private DataSource dataSource;

    private ExhibitionDaoImpl() {
        dataSource = DataSourceUtil.getDataSource();
    }

    public static synchronized ExhibitionDaoImpl getInstance() {
        if (instance == null) {
            instance = new ExhibitionDaoImpl();
        }
        return instance;
    }

    @Override
    public void saveExhibition(Exhibition exhibition, String[] locationsId) throws DaoException {
        ResultSet resultSet = null;
        Connection connection = null;
        long exhibitionId;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_ADD_EXHIBITION, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, exhibition.getTopic());
            preparedStatement.setString(i++, exhibition.getDescription());
            preparedStatement.setTimestamp(i++, exhibition.getStartTimestamp());
            preparedStatement.setTimestamp(i++, exhibition.getEndTimestamp());
            preparedStatement.setDouble(i++, exhibition.getPrice());
            preparedStatement.setBytes(i, exhibition.getPosterImg());

            if (preparedStatement.executeUpdate() > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    exhibition.setId(resultSet.getLong(1));
                }
            }

            exhibitionId = exhibition.getId();

            for (String locationId : locationsId){
                setRowToExhibitionHasLocation(connection, exhibitionId, Long.parseLong(locationId));
            }

        } catch (SQLException e) {
            logger.error("Cannot save exhibition!", e);
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
            throw new DaoException("Cannot save exhibition!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public Exhibition getExhibitionById(long id) {
        Exhibition exhibition = null;
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_EXHIBITION_BY_ID)) {

            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                exhibition = mapExhibition(resultSet);
            }

        } catch (SQLException e) {
            logger.error("Cannot get exhibition by id!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
        return exhibition;
    }

    private Exhibition mapExhibition(ResultSet rs) throws SQLException {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(rs.getLong(Constants.SQL_FIELD_ID));
        exhibition.setTopic(rs.getString(Constants.SQL_FIELD_TOPIC));
        exhibition.setDescription(rs.getString(Constants.SQL_FIELD_DESCRIPTION));
        exhibition.setStartTimestamp(rs.getTimestamp(Constants.SQL_FIELD_START_DATE_TIME));
        exhibition.setEndTimestamp(rs.getTimestamp(Constants.SQL_FIELD_END_DATE_TIME));
        exhibition.setPrice(rs.getDouble(Constants.SQL_FIELD_PRICE));
        exhibition.setPosterImg(rs.getBytes(Constants.SQL_FIELD_POSTER_IMG));
        exhibition.setStatus(rs.getInt(Constants.SQL_FIELD_STATUS));

        return exhibition;
    }

    @Override
    public List<Exhibition> getAllExhibitions() throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(Constants.SQL_GET_ALL_EXHIBITIONS)){
            while (rs.next()) {
                exhibitions.add(mapExhibition(rs));
            }
        } catch (SQLException e) {
            logger.error("Cannot get all exhibitions!", e);
            throw new DaoException("Cannot get all exhibitions!", e);
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByDefault(int pageNum) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_DEFAULT)) {

            preparedStatement.setInt(1, (pageNum - 1) * 2);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by default!", e);
            throw new DaoException("Cannot get exhibitions on page by default!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByPrice(int pageNum) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_PRICE)) {

            preparedStatement.setInt(1, (pageNum - 1) * 2);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by price!", e);
            throw new DaoException("Cannot get exhibitions on page by price!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByTopic(int pageNum) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_TOPIC)) {

            preparedStatement.setInt(1, (pageNum - 1) * 2);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by topic!", e);
            throw new DaoException("Cannot get exhibitions on page by topic!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByDate(int pageNum, Date chosenDate) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_DATE)) {

            int i = 1;
            preparedStatement.setDate(i++, chosenDate);
            preparedStatement.setInt(i, (pageNum - 1) * 2);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by date!", e);
            throw new DaoException("Cannot get exhibitions on page by date!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return exhibitions;
    }

    @Override
    public void setRowToExhibitionHasLocation(Connection connection, long exhibitionId, long locationId) throws DaoException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_ROW_TO_EXHIBITION_HAS_LOCATION);

            int i = 1;
            preparedStatement.setLong(i++, exhibitionId);
            preparedStatement.setLong(i, locationId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Cannot set row exhibitions has location!", e);
            throw new DaoException("Cannot set row exhibitions has location!", e);
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
    public void cancelExhibitionById(long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_CANCEL_EXHIBITION_BY_ID)){

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Cannot cancel exhibition!", e);
            throw new DaoException("Cannot cancel exhibition!", e);
        }
    }

    @Override
    public int getAmountOfExhibitions() throws DaoException {
        int amountOfExhibitions = 0;

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(Constants.SQL_GET_AMOUNT_OF_EXHIBITIONS)){
            while (rs.next()) {
                amountOfExhibitions = rs.getInt(Constants.SQL_FIELD_AMOUNT);
            }
        } catch (SQLException e) {
            logger.error("Cannot get amount of exhibitions!", e);
            throw new DaoException("Cannot get amount of exhibitions!", e);
        }

        return amountOfExhibitions;
    }

    @Override
    public int getAmountOfExhibitionsByDate(Date chosenDate) throws DaoException {
        int amountOfExhibitions = 0;
        ResultSet resultSet = null;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_AMOUNT_OF_EXHIBITIONS_BY_DATE)){

            preparedStatement.setDate(1, chosenDate);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                amountOfExhibitions = resultSet.getInt(Constants.SQL_FIELD_AMOUNT);
            }
        } catch (SQLException e) {
            logger.error("Cannot get amount of exhibitions by date!", e);
            throw new DaoException("Cannot get amount of exhibitions by date!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return amountOfExhibitions;
    }

    @Override
    public Integer getAmountOfSoldTicketsByExhibitionId(long exhibitionId) {
        int amountOfSoldTickets = 0;
        ResultSet resultSet = null;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_AMOUNT_OF_SOLD_TICKETS_BY_EXHIBITION_ID)){

            preparedStatement.setLong(1, exhibitionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                amountOfSoldTickets = resultSet.getInt(Constants.SQL_FIELD_TICKETS_AMOUNT);
            }
        } catch (SQLException e) {
            logger.error("Cannot get amount of sold tickets!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return amountOfSoldTickets;
    }

    @Override
    public List<Exhibition> getExhibitionsStatistics() throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(Constants.SQL_GET_EXHIBITIONS_STATISTICS)){
            while (rs.next()) {
                exhibitions.add(mapExhibitionStatistic(rs));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions statistics!", e);
            throw new DaoException("Cannot get exhibitions statistics!", e);
        }

        return exhibitions;
    }

    @Override
    public Map<String, Integer> getDetailedStatisticsByExhibitionId(long exhibitionId) throws DaoException {
        Map<String, Integer> detailedStatistic = new LinkedHashMap<>();
        ResultSet resultSet = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_GET_DETAILED_STATISTICS_BY_EXHIBITION_ID)){

            preparedStatement.setLong(1, exhibitionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                detailedStatistic.put(resultSet.getString(Constants.SQL_FIELD_LOGIN), resultSet.getInt(Constants.SQL_FIELD_AMOUNT_OF_BOUGHT_TICKETS));
            }
        } catch (SQLException e) {
            logger.error("Cannot get detailed statistics!", e);
            throw new DaoException("Cannot get detailed statistics!", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }

        return detailedStatistic;

    }

    private Exhibition mapExhibitionStatistic(ResultSet rs) throws SQLException {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(rs.getLong(Constants.SQL_FIELD_ID));
        exhibition.setTopic(rs.getString(Constants.SQL_FIELD_TOPIC));
        exhibition.setStartTimestamp(rs.getTimestamp(Constants.SQL_FIELD_START_DATE_TIME));
        exhibition.setEndTimestamp(rs.getTimestamp(Constants.SQL_FIELD_END_DATE_TIME));
        exhibition.setPrice(rs.getDouble(Constants.SQL_FIELD_PRICE));

        return exhibition;
    }
}
