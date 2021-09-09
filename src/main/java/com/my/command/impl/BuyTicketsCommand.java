package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.UserDao;
import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class BuyTicketsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession httpSession = req.getSession();
        int amountOfTickets = Integer.parseInt(req.getParameter("amountOfTickets"));
        long exhibitionId = Long.parseLong(req.getParameter("exhibitionId"));
        User user = (User) httpSession.getAttribute("user");
        long userId = user.getId();

        UserDao userDao = UserDaoImpl.getInstance();

        try {
            userDao.buyTickets(userId, exhibitionId, amountOfTickets);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "error.jsp";
        }

        return "index.jsp";
    }
}
