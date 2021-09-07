package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.LocationDao;
import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.entity.Location;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class GetLocationCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LocationDao locationDao = LocationDaoImpl.getInstance();
        List<Location> locations = locationDao.getAllLocations();
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("locations", locations);

        return "jsp/addExhibition.jsp";
    }
}
