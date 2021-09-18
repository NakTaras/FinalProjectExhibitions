package com.my.servlet;

import com.my.command.Command;
import com.my.command.CommandContainer;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(urlPatterns = {"/controller"})
@MultipartConfig
public class Controller extends HttpServlet {

    public void init() {
        System.out.println("HelloServlet.init");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.doGet");

        HttpSession httpSession = request.getSession();

        String commandName = request.getParameter("command");
        System.out.println("commandName ==> " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        System.out.println("command ==> " + command);

        String address = "error.jsp";

        // (3) do command
        address = command.execute(request, response);

        if (!commandName.equals("getImg") && !commandName.equals("getExhibitions") && !commandName.equals("getDetailedStatistics") ) {
            response.sendRedirect(address);
        }

        if (commandName.equals("getExhibitions") || commandName.equals("getDetailedStatistics") ) {
            request.getRequestDispatcher(address).forward(request, response);
        }

        //request.getRequestDispatcher(address).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession httpSession = request.getSession();

        String commandName = request.getParameter("command");
        System.out.println("commandName ==> " + commandName);

        String userLogin = request.getParameter("login");
        System.out.println("userLogin ==> " + userLogin);

        if (httpSession.isNew()) {
            httpSession.setAttribute("user", null);
        }

        // (2) get command
        Command command = CommandContainer.getCommand(commandName);
        System.out.println("command ==> " + command);

        String address = "error.jsp";

        // (3) do command
        address = command.execute(request, response);


        // (4) go to address
        //request.getRequestDispatcher(address).forward(request, response);
        response.sendRedirect(address);
    }

    public void destroy() {
        System.out.println("HelloServlet.destroy");
    }
}