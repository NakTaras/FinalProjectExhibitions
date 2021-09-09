package com.my.db.dao.impl;

import com.my.db.constant.Constants;
import com.my.db.dao.ExhibitionDao;
import com.my.db.entity.Exhibition;
import com.my.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionDaoImpl implements ExhibitionDao {

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
    public boolean saveExhibition(Exhibition exhibition, String[] locationsId) {
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
            System.out.println(e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
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
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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
    public List<Exhibition> getAllExhibitions() {
        List<Exhibition> exhibitions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(Constants.SQL_GET_ALL_EXHIBITIONS)){
            while (rs.next()) {
                exhibitions.add(mapExhibition(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return exhibitions;
    }

    @Override
    public void setRowToExhibitionHasLocation(Connection connection, long exhibitionId, long locationId) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(Constants.SQL_ADD_ROW_TO_EXHIBITION_HAS_LOCATION);

            int i = 1;
            preparedStatement.setLong(i++, exhibitionId);
            preparedStatement.setLong(i, locationId);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new SQLException(ex);
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
    public void cancelExhibitionById(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Constants.SQL_CANCEL_EXHIBITION_BY_ID)){

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException(e);
        }
    }
}
