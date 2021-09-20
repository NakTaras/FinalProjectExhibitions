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
    <title>Add Exhibition</title>

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

        .add_exhibition_container {
            padding: 50px 30%;
        }

        .add_exhibition_container form {
            border: 3px solid #f1f1f1;
        }

        .add_exhibition_container input[type=text], .add_exhibition_container input[type=date],
        .add_exhibition_container input[type=time], .add_exhibition_container input[type=number],
        .add_exhibition_container input[type=file], .add_exhibition_container select {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .add_exhibition_container option {
            background-color: #04AA6D;
            color: white;
            font-size: 14px;
        }

        .add_exhibition_container button {
            background-color: #04AA6D;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        .add_exhibition_container button:hover {
            opacity: 0.8;
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
                <div class="logged_user"> <fmt:message key='topnav.info.loggedAs'/> ${sessionScope.user.login}</div>
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
                <a class="active" href="">УКР</a>
            </c:when>
            <c:otherwise>
                <a href="../../controller?command=chooseLanguage&language=uk">УКР</a>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<h1><fmt:message key='addExhibition.topic'/></h1>
<hr>
<form action="../../controller" method="post" enctype="multipart/form-data">
    <div class="add_exhibition_container">
        <input name="command" type="hidden" value="addExhibition">

        <label><fmt:message key='addExhibition.form.topic'/></label>
        <input type="text" name="topic" placeholder="<fmt:message key='addExhibition.form.enterTopic'/>">

        <label><fmt:message key='addExhibition.form.description'/></label>
        <input type="text" name="description" placeholder="<fmt:message key='addExhibition.form.enterDescription'/>">

        <label><fmt:message key='addExhibition.form.startDate'/></label>
        <input type="date" name="startDate">

        <label><fmt:message key='addExhibition.form.endDate'/></label>
        <input type="date" name="endDate">

        <label><fmt:message key='addExhibition.form.startTime'/></label>
        <input type="time" name="startTime">

        <label><fmt:message key='addExhibition.form.endTime'/></label>
        <input type="time" name="endTime">

        <label><fmt:message key='addExhibition.form.price'/></label>
        <input type="number" name="price" step="0.01" placeholder="0.00" min="0.01">

        <label><fmt:message key='addExhibition.form.locations'/></label><br>
        <select name="chosenLocations" multiple="multiple">
            <c:forEach items="${locations}" var="location">
                <option value="${location.id}">${location.name}</option>
                <br>
            </c:forEach>

        </select><br>

        <label><fmt:message key='addExhibition.form.poster'/></label>
        <input type="file" name="posterImg" accept="image/*">

        <button type="submit"><fmt:message key='addExhibition.form.createExhibition'/></button>
    </div>
</form>
</body>
</html>
