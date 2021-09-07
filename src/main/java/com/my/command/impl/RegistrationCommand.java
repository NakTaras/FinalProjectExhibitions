package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.UserDao;
import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        user.setRole(req.getParameter("role"));

        UserDao userDao = UserDaoImpl.getInstance();
        boolean isAdded = userDao.saveUser(user);
        req.setAttribute("isAdded", isAdded);

        if (isAdded){
            return "index.jsp";
        }
        return "jsp/registration.jsp";
    }
}
