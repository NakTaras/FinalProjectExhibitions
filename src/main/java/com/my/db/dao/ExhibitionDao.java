package com.my.db.dao;

import com.my.db.entity.Exhibition;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface ExhibitionDao {
    boolean saveExhibition(Exhibition exhibition, String[] locationsId);

    Exhibition getExhibitionById(long id);

    List<Exhibition> getExhibitionsOnPageByDefault(int pageNum);

    List<Exhibition> getExhibitionsOnPageByPrice(int pageNum);

    List<Exhibition> getExhibitionsOnPageByTopic(int pageNum);

    List<Exhibition> getExhibitionsOnPageByDate(int pageNum, Date chosenDate);

    List<Exhibition> getAllExhibitions();

    void setRowToExhibitionHasLocation(Connection connection, long exhibitionId, long locationId) throws SQLException;

    void cancelExhibitionById(long id) throws SQLException;

    int getAmountOfExhibitions();

    int getAmountOfExhibitionsByDate(Date chosenDate);
}
