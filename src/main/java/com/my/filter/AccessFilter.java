package com.my.filter;

import com.my.db.entity.User;
import com.my.servlet.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/jsp/admin/*")
public class AccessFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AccessFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpRequest.getSession();
        User user = (User) httpSession.getAttribute("user");
        logger.info("AccessFilter.doFilter  User ==> " + user);
        if (user == null) {
            httpResponse.sendRedirect("../../index.jsp");
        }
        else {
            if (user.getRole().equals("client")){
                httpResponse.sendRedirect("../../index.jsp");
            }
        }
        chain.doFilter(request, response);

    }
}
