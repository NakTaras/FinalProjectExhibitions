package com.my.command.impl;

import com.my.command.Command;
import com.my.db.dao.ExhibitionDao;
import com.my.db.dao.impl.ExhibitionDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetExhibitionsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ExhibitionDao exhibitionDao = ExhibitionDaoImpl.getInstance();
        exhibitionDao.getAllExhibitions();
        return null;
    }
}
