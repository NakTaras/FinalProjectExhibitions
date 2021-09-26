package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.LocationDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.dao.impl.LocationDaoImpl;
import com.my.db.entity.Exhibition;
import com.my.exception.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetExhibitionsStatisticsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        Map<Exhibition, Integer> exhibitionsStatistics = new LinkedHashMap<>();
        List<Exhibition> exhibitions;
        Integer soldTickets;

        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance();

        try {
            exhibitions = exhibitionDao.getExhibitionsStatistics();
        } catch (DaoException e) {
            e.printStackTrace();
            return "jsp/error.jsp";
        }

        LocationDao locationDao = LocationDaoImpl.getInstance();
        for (Exhibition exhibition : exhibitions) {
            exhibition.setLocations(locationDao.getLocationsByExhibitionId(exhibition.getId()));
            exhibition.setStartDate(String.valueOf(exhibition.getStartTimestamp()).substring(0, 10));
            exhibition.setEndDate(String.valueOf(exhibition.getEndTimestamp()).substring(0, 10));
            soldTickets = exhibitionDao.getAmountOfSoldTicketsByExhibitionId(exhibition.getId());
            exhibitionsStatistics.put(exhibition, soldTickets);
        }

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("exhibitionsStatistics", exhibitionsStatistics);

        return "jsp/admin/exhibitionStatistics.jsp";
    }
}
