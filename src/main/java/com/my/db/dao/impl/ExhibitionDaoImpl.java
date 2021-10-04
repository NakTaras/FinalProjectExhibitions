package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.ExhibitionDao;
import com.my.db.entity.Exhibition;
import com.my.exception.DaoException;
import com.my.util.AutoCloseableClose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class ExhibitionDaoImpl is used to access information about exhibitions from the database.
 * Class ExhibitionDaoImpl implements interface ExhibitionDao.
 *
 * @author Taras Nakonechnyi
 */
public class ExhibitionDaoImpl implements ExhibitionDao {

    private static final Logger logger = LogManager.getLogger(LocationDaoImpl.class);

    /**
     * Singleton object of class ExhibitionDaoImpl
     */
    private static ExhibitionDaoImpl instance;

    /**
     * A factory for connections to the database
     */
    private DataSource dataSource;

    /**
     * The private constructor that called from the method getInstance.
     * It should be called exactly once when the first time the method getInstance is called.
     *
     * @param dataSource - factory for connections to the database
     */
    private ExhibitionDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Static method that create instance and call private constructor when the first time the method is called.
     *
     * @param dataSource - factory for connections to the database.
     * @return instance - Singleton object of class ExhibitionDaoImpl
     */
    public static synchronized ExhibitionDaoImpl getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new ExhibitionDaoImpl(dataSource);
        }
        return instance;
    }

    @Override
    public void saveExhibition(Exhibition exhibition, String[] locationsId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long exhibitionId;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_EXHIBITION, Statement.RETURN_GENERATED_KEYS);
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

            for (String locationId : locationsId) {
                setRowToExhibitionHasLocation(connection, exhibitionId, Long.parseLong(locationId));
            }

            connection.commit();
            connection.setAutoCommit(true);

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
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }
    }

    @Override
    public Exhibition getExhibitionById(long id) {
        Exhibition exhibition = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_GET_EXHIBITION_BY_ID);

            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                exhibition = mapExhibition(resultSet);
            }

        } catch (SQLException e) {
            logger.error("Cannot get exhibition by id!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }
        return exhibition;
    }

    /**
     * The method set information about the exhibition into the object of class Exhibition.
     * @param rs - ResultSet with information about the exhibition from the database.
     * @return object of class Exhibition with information about the exhibition.
     */
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
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(Constants.SQL_GET_ALL_EXHIBITIONS);

            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get all exhibitions!", e);
            throw new DaoException("Cannot get all exhibitions!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(statement);
            AutoCloseableClose.close(connection);
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByDefault(int pageNum) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_DEFAULT);

            preparedStatement.setInt(1, (pageNum - 1) * 2);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by default!", e);
            throw new DaoException("Cannot get exhibitions on page by default!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByPrice(int pageNum) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_PRICE);

            preparedStatement.setInt(1, (pageNum - 1) * 2);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by price!", e);
            throw new DaoException("Cannot get exhibitions on page by price!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByTopic(int pageNum) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_TOPIC);

            preparedStatement.setInt(1, (pageNum - 1) * 2);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exhibitions.add(mapExhibition(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions on page by topic!", e);
            throw new DaoException("Cannot get exhibitions on page by topic!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return exhibitions;
    }

    @Override
    public List<Exhibition> getExhibitionsOnPageByDate(int pageNum, Date chosenDate) throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_EXHIBITIONS_ON_PAGE_BY_DATE);

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
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return exhibitions;
    }

    /**
     * The method adds information about the location where the exhibition takes place to the database.
     * @param connection - connection with transaction that was created in saveExhibition.
     * @param exhibitionId   - exhibition id.
     * @param locationId - id of the location where the exhibition takes place we want to add to the database.
     * @throws DaoException when the exhibition and location where the exhibition takes place could not be saved to the database.
     */
    private void setRowToExhibitionHasLocation(Connection connection, long exhibitionId, long locationId) throws DaoException {
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
            AutoCloseableClose.close(preparedStatement);
        }
    }

    @Override
    public void cancelExhibitionById(long id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_CANCEL_EXHIBITION_BY_ID);

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Cannot cancel exhibition!", e);
            throw new DaoException("Cannot cancel exhibition!", e);
        } finally {
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }
    }

    @Override
    public int getAmountOfExhibitions() throws DaoException {
        int amountOfExhibitions = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(Constants.SQL_GET_AMOUNT_OF_EXHIBITIONS);

            while (resultSet.next()) {
                amountOfExhibitions = resultSet.getInt(Constants.SQL_FIELD_AMOUNT);
            }
        } catch (SQLException e) {
            logger.error("Cannot get amount of exhibitions!", e);
            throw new DaoException("Cannot get amount of exhibitions!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(statement);
            AutoCloseableClose.close(connection);
        }

        return amountOfExhibitions;
    }

    @Override
    public int getAmountOfExhibitionsByDate(Date chosenDate) throws DaoException {
        int amountOfExhibitions = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_GET_AMOUNT_OF_EXHIBITIONS_BY_DATE);

            preparedStatement.setDate(1, chosenDate);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                amountOfExhibitions = resultSet.getInt(Constants.SQL_FIELD_AMOUNT);
            }
        } catch (SQLException e) {
            logger.error("Cannot get amount of exhibitions by date!", e);
            throw new DaoException("Cannot get amount of exhibitions by date!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return amountOfExhibitions;
    }

    @Override
    public int getAmountOfSoldTicketsByExhibitionId(long exhibitionId) {
        int amountOfSoldTickets = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_GET_AMOUNT_OF_SOLD_TICKETS_BY_EXHIBITION_ID);

            preparedStatement.setLong(1, exhibitionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                amountOfSoldTickets = resultSet.getInt(Constants.SQL_FIELD_TICKETS_AMOUNT);
            }
        } catch (SQLException e) {
            logger.error("Cannot get amount of sold tickets!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return amountOfSoldTickets;
    }

    @Override
    public List<Exhibition> getExhibitionsStatistics() throws DaoException {
        List<Exhibition> exhibitions = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(Constants.SQL_GET_EXHIBITIONS_STATISTICS);

            while (resultSet.next()) {
                exhibitions.add(mapExhibitionStatistic(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Cannot get exhibitions statistics!", e);
            throw new DaoException("Cannot get exhibitions statistics!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(statement);
            AutoCloseableClose.close(connection);
        }

        return exhibitions;
    }

    @Override
    public Map<String, Integer> getDetailedStatisticsByExhibitionId(long exhibitionId) throws DaoException {
        Map<String, Integer> detailedStatistic = new LinkedHashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(Constants.SQL_GET_DETAILED_STATISTICS_BY_EXHIBITION_ID);

            preparedStatement.setLong(1, exhibitionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                detailedStatistic.put(resultSet.getString(Constants.SQL_FIELD_LOGIN), resultSet.getInt(Constants.SQL_FIELD_AMOUNT_OF_BOUGHT_TICKETS));
            }
        } catch (SQLException e) {
            logger.error("Cannot get detailed statistics!", e);
            throw new DaoException("Cannot get detailed statistics!", e);
        } finally {
            AutoCloseableClose.close(resultSet);
            AutoCloseableClose.close(preparedStatement);
            AutoCloseableClose.close(connection);
        }

        return detailedStatistic;

    }

    /**
     * The method set information about the exhibition statistic into the object of class Exhibition.
     * @param rs - ResultSet with information about the exhibition statistic from the database.
     * @return object of class Exhibition with information about the exhibition statistic.
     */
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
