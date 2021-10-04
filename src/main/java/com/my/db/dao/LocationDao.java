package com.my.db.dao;

import com.my.db.entity.Location;
import com.my.exception.DaoException;

import java.util.List;

/**
 * Interface LocationDao is used to access information about locations from the database.
 * @author Taras Nakonechnyi
 */
public interface LocationDao {

    /**
     * The method adds information about the location to the database.
     * @param location - information about the location that needs to be saved in the database.
     * @throws DaoException when the location could not be saved to the database.
     */
    void saveLocation(Location location) throws DaoException;

    /**
     * The method gets information about the locations where the exhibition takes place from the database.
     * Location has information about id, name and address.
     * @param exhibitionId - user login.
     */
    List<Location> getLocationsByExhibitionId(long exhibitionId);

    /**
     * The method gets information about all locations from the database.
     * Location has information about id and name.
     * @throws DaoException when the location could not be gotten from the database.
     */
    List<Location> getAllLocations() throws DaoException;
}
