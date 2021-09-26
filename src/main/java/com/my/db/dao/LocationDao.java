package com.my.db.dao;

import com.my.db.entity.Location;
import com.my.exception.DaoException;

import java.util.List;

public interface LocationDao {

    void saveLocation(Location location) throws DaoException;
    List<Location> getLocationsByExhibitionId(long exhibitionId);
    List<Location> getAllLocations() throws DaoException;
}
