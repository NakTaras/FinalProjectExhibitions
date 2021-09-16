package com.my.command.impl;

import com.my.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChooseLanguageCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String language = req.getParameter("language");

        HttpSession httpSession = req.getSession();
        // place in request scope
        httpSession.setAttribute("language", language);

        return "index.jsp";
    }
}
