package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.LocationDao;
import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.entity.Location;
import com.my.exception.DaoException;
import com.my.util.DataSourceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddLocationCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        Location location = new Location();

        Map<String, String> address = new LinkedHashMap<>();

        if (req.getParameter("locationAddressUk").equals("") || req.getParameter("locationAddressEn").equals("")){
            return "jsp/error.jsp";
        }

        address.put("uk", req.getParameter("locationAddressUk"));
        address.put("en", req.getParameter("locationAddressEn"));
        location.setName(req.getParameter("locationName"));
        location.setAddress(address);

        LocationDao locationDao = LocationDaoImpl.getInstance(DataSourceUtil.getDataSource());
        try {
            locationDao.saveLocation(location);
        } catch (DaoException e) {
            e.printStackTrace();
            return "jsp/error.jsp";
        }

        return "jsp/admin/addLocation.jsp";
    }
}
