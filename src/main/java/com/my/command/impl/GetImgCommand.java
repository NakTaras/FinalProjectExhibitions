package com.my.command.impl;

import com.my.command.Command;
import com.my.db.entity.Exhibition;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GetImgCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

            HttpSession httpSession = req.getSession();
            long exhibitionId = Long.parseLong(req.getParameter("img"));

            List<Exhibition> exhibitions = (List<Exhibition>) httpSession.getAttribute("exhibitions");
            ServletOutputStream servletOutputStream = null;
            try {
                servletOutputStream = resp.getOutputStream();
                for (Exhibition exhibition : exhibitions) {
                    if (exhibition.getId() == exhibitionId) {
                        servletOutputStream.write(exhibition.getPosterImg());
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return "error.jsp";
            }
            return "index.jsp";
    }
}
