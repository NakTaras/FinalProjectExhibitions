package com.my.db.dao;

import com.my.db.entity.Location;

import java.util.List;

public interface LocationDao {
    boolean saveLocation(Location location);

    Location getLocationById(long id);
    List<Location> getLocationsByExhibitionId(long exhibitionId);
    List<Location> getAllLocations();
}
