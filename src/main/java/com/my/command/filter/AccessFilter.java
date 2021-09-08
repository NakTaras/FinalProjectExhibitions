package com.my.command.filter;

import com.my.db.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/jsp/admin/*")
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("AccessFilter.doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpRequest.getSession();
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            httpResponse.sendRedirect("../../index.jsp");
        }
        else {
            System.out.println("User ==> " + user.getLogin());
            if (user.getRole().equals("client")){
                httpResponse.sendRedirect("../../index.jsp");
            }
        }
        chain.doFilter(request, response);

    }
}
