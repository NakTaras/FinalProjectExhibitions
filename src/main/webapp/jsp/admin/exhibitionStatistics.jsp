<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty language}">
    <c:set var="language" scope="session" value="${pageContext.request.locale.language}"/>
</c:if>
<c:if test="${!empty language}">
    <fmt:setLocale value="${language}" scope="session"/>
</c:if>

<fmt:setBundle basename="resources"/>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Exhibition Statistics</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
        }

        h1 {
            text-align: center;
        }


        .topnav .logged_user {
            padding: 6px;
            margin-top: 8px;
            font-size: 17px;
            border: none;
            width: 400px;
        }

        .topnav {
            overflow: hidden;
            background-color: #e9e9e9;
        }

        .topnav a {
            float: left;
            display: block;
            color: black;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
            font-size: 17px;
        }

        .topnav a:hover {
            background-color: #ddd;
            color: black;
        }

        .topnav a.active {
            background-color: green;
            color: white;
        }

        .topnav .login-container {
            float: right;
        }

        .change-language {
            float: right;
        }

        .topnav input[type=text] {
            padding: 6px;
            margin-top: 8px;
            font-size: 17px;
            border: none;
            width: 120px;
        }

        .topnav input[type=password] {
            padding: 6px;
            margin-top: 8px;
            font-size: 17px;
            border: none;
            width: 120px;
        }

        .topnav .login-container button {
            float: right;
            padding: 6px 10px;
            margin-top: 8px;
            margin-right: 16px;
            background-color: #555;
            color: white;
            font-size: 17px;
            border: none;
            cursor: pointer;
        }

        .topnav .login-container button:hover {
            background-color: green;
        }

        .statistic {
            font-family: Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        .statistic td, .statistic th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        .statistic tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .statistic tr:hover {
            background-color: #ddd;
        }

        .statistic th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #04AA6D;
            color: white;
        }

        .statistic a {
            border: 1px solid #ddd;
            padding: 8px;
            text-decoration: none;
            background-color: green;
            color: white;
        }

    </style>

</head>
<body>
<div class="topnav">
    <a class="active" href="../../controller?command=getExhibitions&pageNum=1&sortType=default"><fmt:message key='topnav.menu.home'/></a>
    <a href="../registration.jsp"><fmt:message key='topnav.menu.registration'/></a>

    <c:if test="${user.role == 'administrator'}">
        <a href="../../controller?command=getLocations"><fmt:message key='topnav.menu.addExhibition'/></a>
        <a href="addLocation.jsp"><fmt:message key='topnav.menu.addLocation'/></a>
        <a href="../../controller?command=getExhibitionsStatistics"><fmt:message key='topnav.menu.exhibitionStatistics'/></a>
    </c:if>

    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <div class="login-container">
                <form action="../../controller" method="get">
                    <input name="command" type="hidden" value="logIn">
                    <input type="text" placeholder="<fmt:message key='topnav.input.login'/>" name="login">
                    <input type="password" placeholder="<fmt:message key='topnav.input.password'/>" name="password">
                    <button type="submit"><fmt:message key='topnav.button.login'/></button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-container">
                <form action="../../controller" method="get">
                    <input name="command" type="hidden" value="logOut">
                    <button type="submit"><fmt:message key='topnav.button.logOut'/></button>
                </form>
                <div class="logged_user"><fmt:message key='topnav.info.loggedAs'/> ${sessionScope.user.login}</div>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="change-language">
        <c:choose>
            <c:when test="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'] eq 'en'}">
                <a class="active" href="">ENG</a>
            </c:when>
            <c:otherwise>
                <a href="../../controller?command=chooseLanguage&language=en">ENG</a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'] eq 'uk'}">
                <a class="active" href="">??????</a>
            </c:when>
            <c:otherwise>
                <a href="../../controller?command=chooseLanguage&language=uk">??????</a>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<h1><fmt:message key='exhibitionStatistics.topic'/></h1>
<hr>

<table class="statistic">
    <tr>
        <th><fmt:message key='exhibitionStatistics.table.topic'/></th>
        <th><fmt:message key='exhibitionStatistics.table.datesOfWork'/></th>
        <th><fmt:message key='exhibitionStatistics.table.locations'/></th>
        <th><fmt:message key='exhibitionStatistics.table.ticketPrice'/></th>
        <th><fmt:message key='exhibitionStatistics.table.numberOfTicketsSold'/></th>
    </tr>
    <c:forEach items="${exhibitionsStatistics}" var="exhibitionStatistics">
        <tr>
            <td>${exhibitionStatistics.key.topic}</td>
            <td>From ${exhibitionStatistics.key.startDate} To ${exhibitionStatistics.key.endDate}</td>
            <td>
                <c:forEach items="${exhibitionStatistics.key.locations}" var="location">
                    ${location.name}</br>
                </c:forEach>
            </td>
            <td>${exhibitionStatistics.key.price}</td>
            <td>${exhibitionStatistics.value}</td>
            <td><a href="../../controller?command=getDetailedStatistics&exhibitionId=${exhibitionStatistics.key.id}&exhibitionTopic=${exhibitionStatistics.key.topic}" class="active"><fmt:message key='exhibitionStatistics.table.button.getDetailedStatistics'/></a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
