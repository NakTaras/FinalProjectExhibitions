package com.my.db.dao;

import com.my.db.entity.Exhibition;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ExhibitionDao {
    boolean saveExhibition(Exhibition exhibition, String[] locationsId);

    Exhibition getExhibitionById(long id);

    List<Exhibition> getAllExhibitions();

    void setRowToExhibitionHasLocation(Connection connection, long exhibitionId, long locationId) throws SQLException;

    void cancelExhibitionById(long id) throws SQLException;
}
