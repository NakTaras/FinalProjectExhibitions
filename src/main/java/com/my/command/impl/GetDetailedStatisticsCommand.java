package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.entity.Exhibition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class GetDetailedStatisticsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        Map<String, Integer> detailedStatistics;
        long exhibitionId = Long.parseLong(req.getParameter("exhibitionId"));
        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance();

        detailedStatistics = exhibitionDao.getDetailedStatisticsByExhibitionId(exhibitionId);

        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("detailedStatistics", detailedStatistics);

        return "jsp/admin/detailedStatistics.jsp";
    }
}
