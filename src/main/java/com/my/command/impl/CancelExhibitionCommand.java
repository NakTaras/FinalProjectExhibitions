package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;
import com.my.db.entity.Exhibition;
import com.my.exception.DaoException;
import com.my.util.DataSourceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public class CancelExhibitionCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession httpSession = req.getSession();
        long canceledExhibitionId = Long.parseLong(req.getParameter("canceledExhibitionId"));

        List<Exhibition> exhibitions = (List<Exhibition>) httpSession.getAttribute("exhibitions");

        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance(DataSourceUtil.getDataSource());

        for (Exhibition exhibition : exhibitions) {
            if (exhibition.getId() == canceledExhibitionId) {
                try {
                    exhibitionDao.cancelExhibitionById(canceledExhibitionId);
                } catch (DaoException e) {
                    System.out.println(e.getMessage());
                    return "jsp/error.jsp";
                }

                exhibition = exhibitionDao.getExhibitionById(canceledExhibitionId);
            }
        }

        httpSession.setAttribute("exhibitions", null);

        return "index.jsp";
    }
}
