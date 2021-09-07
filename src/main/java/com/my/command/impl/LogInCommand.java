package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.UserDao;
import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogInCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession();

        UserDao userDao = UserDaoImpl.getInstance();
        User user = userDao.getUserByLogin(req.getParameter("login"), req.getParameter("password"));
        httpSession.setAttribute("user", user);
        return "index.jsp";
    }
}
