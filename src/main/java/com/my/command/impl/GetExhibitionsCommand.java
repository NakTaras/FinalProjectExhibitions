package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.LocationDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.entity.Exhibition;
import com.my.exception.DaoException;
import com.my.util.DataSourceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class GetExhibitionsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        int currentPage = Integer.parseInt(req.getParameter("pageNum"));
        String sortType = req.getParameter("sortType");
        List<Exhibition> exhibitions = null;

        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance(DataSourceUtil.getDataSource());

        if (sortType.equals("default")) {
            try {
                exhibitions = exhibitionDao.getExhibitionsOnPageByDefault(currentPage);
            } catch (DaoException e) {
                e.printStackTrace();
                return "jsp/error.jsp";
            }
        }

        if (sortType.equals("topic")) {
            try {
                exhibitions = exhibitionDao.getExhibitionsOnPageByTopic(currentPage);
            } catch (DaoException e) {
                e.printStackTrace();
                return "jsp/error.jsp";
            }
        }

        if (sortType.equals("price")) {
            try {
                exhibitions = exhibitionDao.getExhibitionsOnPageByPrice(currentPage);
            } catch (DaoException e) {
                e.printStackTrace();
                return "jsp/error.jsp";
            }
        }

        if (sortType.equals("date")) {
            Date chosenDate = Date.valueOf(req.getParameter("chosenDate"));
            try {
                exhibitions = exhibitionDao.getExhibitionsOnPageByDate(currentPage, chosenDate);
            } catch (DaoException e) {
                e.printStackTrace();
                return "jsp/error.jsp";
            }
        }

        LocationDao locationDao = LocationDaoImpl.getInstance(DataSourceUtil.getDataSource());
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
            try {
                amountOfExhibitions = exhibitionDao.getAmountOfExhibitionsByDate(Date.valueOf(req.getParameter("chosenDate")));
            } catch (DaoException e) {
                e.printStackTrace();
                return "jsp/error.jsp";
            }
        } else {
            try {
                amountOfExhibitions = exhibitionDao.getAmountOfExhibitions();
            } catch (DaoException e) {
                e.printStackTrace();
                return "jsp/error.jsp";
            }
        }
        if (amountOfExhibitions % 2 == 0) {
            amountOfPages = amountOfExhibitions / 2;
        } else {
            amountOfPages = (amountOfExhibitions / 2) + 1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new java.util.Date());

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("exhibitions", exhibitions);
        httpSession.setAttribute("amountOfPages", amountOfPages);
        httpSession.setAttribute("currentPage", currentPage);
        httpSession.setAttribute("currentDate", currentDate);


        return "index.jsp";
    }
}
