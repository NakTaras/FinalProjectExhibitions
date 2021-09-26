package com.my.servlet;

import com.my.command.Command;
import com.my.command.CommandContainer;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/controller"})
@MultipartConfig
public class Controller extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(Controller.class);

    public void init() {
        logger.info("HelloServlet.init");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession httpSession = request.getSession();

        String commandName = request.getParameter("command");

        logger.info("Controller.doGet   commandName ==> " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        System.out.println("command ==> " + command);

        String address = "error.jsp";

        // (3) do command
        address = command.execute(request, response);

        if (!commandName.equals("getImg") && !commandName.equals("getExhibitions") && !commandName.equals("getDetailedStatistics") ) {
            logger.info("Controller.doGet   address ==> " + address);
            response.sendRedirect(address);
        }

        if (commandName.equals("getExhibitions") || commandName.equals("getDetailedStatistics") ) {
            logger.info("Controller.doGet   address ==> " + address);
            request.getRequestDispatcher(address).forward(request, response);
        }

        //request.getRequestDispatcher(address).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession httpSession = request.getSession();

        String commandName = request.getParameter("command");
        logger.info("Controller.doPost  commandName ==> " + commandName);

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
        logger.info("Controller.doPost   address ==> " + address);
        response.sendRedirect(address);
    }

    public void destroy() {
        logger.info("HelloServlet.destroy");
    }
}