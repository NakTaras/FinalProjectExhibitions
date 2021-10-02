<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
    <title>Something go wrong</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100%;
        }

        body {
            font-family: Sans-Serif;
            background-color: #04AA6D;
            color: #fff;
        }

        .error-container {
            padding-top: 15%;
            text-align: center;
            height: 100%;
        }


        .error-container h1 {
            margin: 0;
            font-size: 70px;
            font-weight: 300;
        }


        .return {
            color: #FFFFFF99;
            font-weight: 400;
            letter-spacing: -0.04em;
            margin: 0;
        }

        .return a {
            padding-bottom: 1px;
            color: #fff;
            text-decoration: none;
            border-bottom: 1px solid #FFFFFF99;
            -webkit-transition: border-color 0.1s ease-in;
            transition: border-color 0.1s ease-in;
        }

        .return a:hover {
            border-bottom-color: #fff;
        }
    </style>
</head>

<body>

<div class="error-container">
    <h1><fmt:message key='error.somethingGoWrong'/></h1>
    <p class="return"><fmt:message key='error.returnTo'/> <a href="http://localhost:8080/FinalProjectExhibitions_war_exploded/controller?command=getExhibitions&pageNum=1&sortType=default"><fmt:message key='error.homePage'/></a></p>
</div>

</body>
</html>
