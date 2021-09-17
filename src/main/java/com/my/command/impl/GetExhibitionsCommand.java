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
import java.sql.Date;
import java.util.List;

public class GetExhibitionsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        int currentPage = Integer.parseInt(req.getParameter("pageNum"));
        String sortType = req.getParameter("sortType");
        List<Exhibition> exhibitions = null;

        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance();

        if (sortType.equals("default")) {
            exhibitions = exhibitionDao.getExhibitionsOnPageByDefault(currentPage);
        }

        if (sortType.equals("topic")) {
            exhibitions = exhibitionDao.getExhibitionsOnPageByTopic(currentPage);
        }

        if (sortType.equals("price")) {
            exhibitions = exhibitionDao.getExhibitionsOnPageByPrice(currentPage);
        }

        if (sortType.equals("date")) {
            Date chosenDate = Date.valueOf(req.getParameter("chosenDate"));
            exhibitions = exhibitionDao.getExhibitionsOnPageByDate(currentPage, chosenDate);
        }

        LocationDao locationDao = LocationDaoImpl.getInstance();
        for (Exhibition exhibition : exhibitions) {
            exhibition.setLocations(locationDao.getLocationsByExhibitionId(exhibition.getId()));
            exhibition.setStartDate(String.valueOf(exhibition.getStartTimestamp()).substring(0, 10));
            exhibition.setEndDate(String.valueOf(exhibition.getEndTimestamp()).substring(0, 10));
            exhibition.setStartTime(String.valueOf(exhibition.getStartTimestamp()).substring(11, 16));
            exhibition.setEndTime(String.valueOf(exhibition.getEndTimestamp()).substring(11, 16));
        }

        int amountOfPages;
        int amountOfExhibitions = 0;

        if (sortType.equals("date"))
        {
            amountOfExhibitions = exhibitionDao.getAmountOfExhibitionsByDate(Date.valueOf(req.getParameter("chosenDate")));
        } else {
            amountOfExhibitions = exhibitionDao.getAmountOfExhibitions();
        }
        if (amountOfExhibitions % 2 == 0) {
            amountOfPages = amountOfExhibitions / 2;
        } else {
            amountOfPages = (amountOfExhibitions / 2) + 1;
        }

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("exhibitions", exhibitions);
        httpSession.setAttribute("amountOfPages", amountOfPages);
        httpSession.setAttribute("currentPage", currentPage);


        return "index.jsp";
    }
}
