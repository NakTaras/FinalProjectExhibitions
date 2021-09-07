package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.LocationDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.entity.Exhibition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

public class GetExhibitionsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance();
        List<Exhibition> exhibitions = exhibitionDao.getAllExhibitions();

        LocationDao locationDao = LocationDaoImpl.getInstance();
        for (Exhibition exhibition : exhibitions){
            exhibition.setLocations(locationDao.getLocationsByExhibitionId(exhibition.getId()));
            exhibition.setStartDate(String.valueOf(exhibition.getStartTimestamp()).substring(0, 10));
            exhibition.setEndDate(String.valueOf(exhibition.getEndTimestamp()).substring(0, 10));
            exhibition.setStartTime(String.valueOf(exhibition.getStartTimestamp()).substring(11, 16));
            exhibition.setEndTime(String.valueOf(exhibition.getEndTimestamp()).substring(11, 16));
        }

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("exhibitions", exhibitions);

        return "index.jsp";
    }
}
