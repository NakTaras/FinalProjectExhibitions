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
<html lang="">
<head>
    <meta charset="utf-8">
    <title>Exhibitions</title>

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

        .change-language {
            float: right;
        }

        .topnav .login-container {
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

        .exhibitions_container button {
            background-color: #04AA6D;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        .exhibitions_container {
            padding: 50px 20%;
        }


        .exhibitions td {
            font-size: 13pt;
            padding: 5px 10px;

            border: 10px groove;
            border-color: #404040 #919191 #404040 #919191;
        }

        .exhibitions table {
            caption-side: top;

        }

        #email1 {
            display: none;
        }

        #email2 {
            display: none;
        }

        .pagination {
            position: relative;
            margin-bottom: 100px;
        }

        .pagination-inner {
            position: absolute;
            padding-left: 50%;
        }

        .pagination a {
            background-color: #d9d9d9;
            float: left;
            margin-right: 5px;
            display: block;
            color: black;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
            font-size: 17px;
        }

        .pagination a:hover {
            background-color: #b0b0b0;
            color: black;
        }

        .pagination a.active {
            background-color: green;
            color: white;
        }
    </style>

</head>
<body>

<div class="topnav">
    <a class="active" href="controller?command=getExhibitions&pageNum=1&sortType=default"><fmt:message key='topnav.menu.home'/></a>
    <a href="jsp/registration.jsp"><fmt:message key='topnav.menu.registration'/></a>

    <c:if test="${user.role == 'administrator'}">
        <a href="controller?command=getLocations"><fmt:message key='topnav.menu.addExhibition'/></a>
        <a href="jsp/admin/addLocation.jsp"><fmt:message key='topnav.menu.addLocation'/></a>
        <a href="controller?command=getExhibitionsStatistics"><fmt:message key='topnav.menu.exhibitionStatistics'/></a>
    </c:if>


    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <div class="login-container">
                <form action="controller" method="get">
                    <input name="command" type="hidden" value="logIn">
                    <input type="text" placeholder="<fmt:message key='topnav.input.login'/>" name="login">
                    <input type="password" placeholder="<fmt:message key='topnav.input.password'/>" name="password">
                    <button type="submit"><fmt:message key='topnav.button.login'/></button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-container">
                <form action="controller" method="get">
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
                <a href="controller?command=chooseLanguage&language=en">ENG</a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'] eq 'uk'}">
                <a class="active" href="">УКР</a>
            </c:when>
            <c:otherwise>
                <a href="controller?command=chooseLanguage&language=uk">УКР</a>
            </c:otherwise>
        </c:choose>

    </div>

</div>

<div class="exhibitions_container">
    <h1><fmt:message key='index.topic'/></h1>


    <form action="controller" method="get">
        <input name="command" type="hidden" value="getExhibitions">
        <input name="pageNum" type="hidden" value="1">
        <input name="sortType" type="hidden" value="default">
        <button type="submit"><fmt:message key='index.exhibition.byDefault'/></button>
    </form>

    <form action="controller" method="get">
        <input name="command" type="hidden" value="getExhibitions">
        <input name="pageNum" type="hidden" value="1">
        <input name="sortType" type="hidden" value="topic">
        <button type="submit"><fmt:message key='index.exhibition.byTopic'/></button>
    </form>

    <form action="controller" method="get">
        <input name="command" type="hidden" value="getExhibitions">
        <input name="pageNum" type="hidden" value="1">
        <input name="sortType" type="hidden" value="price">
        <button type="submit"><fmt:message key='index.exhibition.byPrice'/></button>
    </form>

    <form action="controller" method="get">
        <input name="command" type="hidden" value="getExhibitions">
        <input name="pageNum" type="hidden" value="1">
        <input name="sortType" type="hidden" value="date">
        <label><fmt:message key='index.exhibition.chooseDate'/></label>
        <input type="date" name="chosenDate" <c:if test="${param.chosenDate != null}"> value="${param.chosenDate}" </c:if>>
        <button type="submit"><fmt:message key='index.exhibition.byDate'/></button>
    </form>

    <c:if test="${exhibitions != null}">

        <c:set var="language" scope="page" value="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session']}" />

        <c:set value="1" var="i"/>

        <table align="center" class="exhibitions" cellspacing="9">
            <c:forEach items="${exhibitions}" var="exhibition">

                <tr>
                    <td><img src="controller?command=getImg&img=${exhibition.id}" alt="no img" width=300px
                             height=400px></td>
                    <td><b>${exhibition.topic}</b></br>
                            ${exhibition.description} </br>

                        </br><b><fmt:message key='index.exhibition.when'/>? <fmt:message
                                key='index.exhibition.from'/></b> ${exhibition.startDate}
                        <b><fmt:message
                                key='index.exhibition.to'/></b> ${exhibition.endDate} ${exhibition.startTime}-${exhibition.endTime} </br>
                        <b><fmt:message key='index.exhibition.where'/>?</b>
                        <c:forEach items="${exhibition.locations}" var="location">
                            ${location.name}

                            <c:choose>
                                <c:when test="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'] eq 'en'}">
                                    ${location.address['en']}
                                </c:when>
                                <c:when test="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'] eq 'uk'}">
                                    ${location.address['uk']}
                                </c:when>
                                <c:otherwise>
                                    ${location.address['uk']}
                                </c:otherwise>
                            </c:choose>

                            </br>
                        </c:forEach>
                        <b><fmt:message key='index.exhibition.price'/>?</b> ${exhibition.price} <fmt:message
                                key='index.exhibition.uah'/>
                        <c:if test="${exhibition.status == 1}">
                            <c:if test="${user != null and exhibition.endDate.compareTo(currentDate) >= 0}">
                                </br></br>
                                <form action="controller" method="post">
                                    <input name="command" type="hidden" value="buyTickets">
                                    <label><b><fmt:message key='index.exhibition.howMany'/></b></label></br>
                                    <input type="number" name="amountOfTickets" step="1" value="1" min="1"></br>
                                    <label><b>Do you want to get confirmation on email?</b></label>
                                    <input type="checkbox" id="hasEmail${i}" onclick="showEmailInput(${i})"></br>
                                    <div id="email${i}">
                                        <label><b>Input your email</b></label></br>
                                        <input type="text" placeholder="Email" name="email">
                                        <input name="exhibitionTopic" type="hidden" value="${exhibition.topic}">
                                    </div>
                                    <input name="exhibitionId" type="hidden" value="${exhibition.id}">
                                    <button type="submit"><fmt:message key='index.exhibition.buyTickets'/></button>
                                </form>
                            </c:if>

                            <c:if test="${user.role == 'administrator'}">
                                <form action="controller" method="post">
                                    <input name="command" type="hidden" value="cancelExhibition">
                                    <input name="canceledExhibitionId" type="hidden" value="${exhibition.id}">
                                    <button type="submit"><fmt:message
                                            key='index.exhibition.cancelExhibition'/></button>
                                </form>
                            </c:if>
                        </c:if>
                    </td>
                </tr>

                <c:set value="${i+1}" var="i"/>

            </c:forEach>


        </table>
    </c:if>

    <div class="pagination">
        <div class="pagination-inner">
            <c:forEach begin="1" end="${amountOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <a class="active" href="controller?command=getExhibitions&pageNum=${i}&sortType=${param.sortType}<c:if test="${param.sortType eq 'date'}">&chosenDate=${param.chosenDate}</c:if>">${i}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="controller?command=getExhibitions&pageNum=${i}&sortType=${param.sortType}<c:if test="${param.sortType eq 'date'}">&chosenDate=${param.chosenDate}</c:if>">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </div>

</div>


<script>

    function showEmailInput(i) {
        var checkBox = document.getElementById("hasEmail" + i);
        var text = document.getElementById("email" + i);
        if (checkBox.checked == true){
            text.style.display = "block";
        } else {
            text.style.display = "none";
        }
    }
</script>


</body>
</html>