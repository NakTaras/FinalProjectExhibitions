package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.UserDao;
import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.User;
import com.my.exception.DaoException;
import com.my.util.DataSourceUtil;
import com.my.util.EmailSender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BuyTicketsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession httpSession = req.getSession();
        int amountOfTickets = Integer.parseInt(req.getParameter("amountOfTickets"));
        long exhibitionId = Long.parseLong(req.getParameter("exhibitionId"));
        User user = (User) httpSession.getAttribute("user");
        long userId = user.getId();

        UserDao userDao = UserDaoImpl.getInstance(DataSourceUtil.getDataSource());

        try {
            userDao.buyTickets(userId, exhibitionId, amountOfTickets);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return "error.jsp";
        }

        String recipientEmail = req.getParameter("email");

        if (!recipientEmail.equals("")) {
            String exhibitionTopic = req.getParameter("exhibitionTopic");

            EmailSender.sendEmail(recipientEmail, exhibitionTopic, amountOfTickets);
        }

        httpSession.setAttribute("exhibitions", null);
        httpSession.setAttribute("amountOfPages", null);
        httpSession.setAttribute("currentPage", null);

        return "index.jsp";
    }
}
