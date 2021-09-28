package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.UserDao;
import com.my.db.dao.impl.UserDaoImpl;
import com.my.db.entity.User;
import com.my.exception.DaoException;
import com.my.util.DataSourceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        user.setRole(req.getParameter("role"));

        UserDao userDao = UserDaoImpl.getInstance(DataSourceUtil.getDataSource());

        try {
            userDao.saveUser(user);
        } catch (DaoException e) {
            e.printStackTrace();
            return "jsp/registration.jsp";
        }

        return "index.jsp";

    }
}
