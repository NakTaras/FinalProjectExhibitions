package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.LocationDao;
import com.my.db.dao.UserDao;
import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.Location;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddLocationCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        Location location = new Location();

        location.setName(req.getParameter("locationName"));
        location.setAddress(req.getParameter("locationAddress"));

        LocationDao locationDao = LocationDaoImpl.getInstance();
        boolean isAdded = locationDao.saveLocation(location);

        if (!isAdded){
            return "error.jsp";
        }

        return "jsp/admin/addLocation.jsp";
    }
}
