package com.my.db.dao;

import com.my.db.entity.Exhibition;
import com.my.exception.DaoException;

import java.sql.Connection;
import java.sql.Date;

import java.util.List;
import java.util.Map;

public interface ExhibitionDao {
    void saveExhibition(Exhibition exhibition, String[] locationsId) throws DaoException;

    Exhibition getExhibitionById(long id);

    List<Exhibition> getExhibitionsOnPageByDefault(int pageNum) throws DaoException;

    List<Exhibition> getExhibitionsOnPageByPrice(int pageNum) throws DaoException;

    List<Exhibition> getExhibitionsOnPageByTopic(int pageNum) throws DaoException;

    List<Exhibition> getExhibitionsOnPageByDate(int pageNum, Date chosenDate) throws DaoException;

    void setRowToExhibitionHasLocation(Connection connection, long exhibitionId, long locationId) throws DaoException;

    void cancelExhibitionById(long id) throws DaoException;

    int getAmountOfExhibitions() throws DaoException;

    int getAmountOfExhibitionsByDate(Date chosenDate) throws DaoException;

    Integer getAmountOfSoldTicketsByExhibitionId(long exhibitionId);

    List<Exhibition> getExhibitionsStatistics() throws DaoException;

    Map<String, Integer> getDetailedStatisticsByExhibitionId(long exhibitionId) throws DaoException;

    List<Exhibition> getAllExhibitions() throws DaoException;
}
