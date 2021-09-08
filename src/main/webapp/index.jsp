<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
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
    </style>

</head>
<body>
<div class="topnav">
    <a class="active" href="index.jsp">Home</a>
    <a href="jsp/registration.jsp">Registration</a>

    <c:if test="${user.role == 'administrator'}">
        <a href="controller?command=getLocations">Add Exhibition</a>
        <a href="jsp/admin/addLocation.jsp">Add Location</a>
    </c:if>

    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <div class="login-container">
                <form action="controller" method="get">
                    <input name="command" type="hidden" value="logIn">
                    <input type="text" placeholder="Login" name="login">
                    <input type="password" placeholder="Password" name="password">
                    <button type="submit">Login</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-container">
                <form action="controller" method="get">
                    <input name="command" type="hidden" value="logOut">
                    <button type="submit">Log out</button>
                </form>
                <div class="logged_user"> You are logged as ${sessionScope.user.role}</div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<div class="exhibitions_container">
    <h1>Exhibitions</h1>

    <c:choose>
        <c:when test="${sessionScope.exhibitions == null}">
            <form action="controller" method="get">
                <input name="command" type="hidden" value="getExhibitions">
                <button type="submit">Get Exhibitions</button>
            </form>
        </c:when>
        <c:otherwise>
            <form action="controller" method="get">
                <input name="command" type="hidden" value="getExhibitions">
                <button type="submit">Reload Exhibitions</button>
            </form>

            <table align="center" class="exhibitions" cellspacing="9">
                <c:forEach items="${exhibitions}" var="exhibition">

                    <c:if test="${exhibition.status == 1}">

                    <tr>
                        <td><img src="controller?command=getImg&img=${exhibition.id}" alt="no img" width=316px
                                 height=448px></td>
                        <td><b>${exhibition.topic}</b></br>
                                ${exhibition.description} </br>

                            </br>When? From ${exhibition.startDate}
                            to ${exhibition.endDate} ${exhibition.startTime}-${exhibition.endTime} </br>
                            Where?<c:forEach items="${exhibition.locations}"
                                             var="location">   ${location.name}    ${location.address} </br>
                            </c:forEach>
                            Price? ${exhibition.price} UAH
                            <c:if test="${exhibition.status == 1}">
                                <c:if test="${user != null}">
                                    </br></br>
                                    <form action="controller" method="post">
                                        <input name="command" type="hidden" value="buyTickets">
                                        <label>How many tickets do you want to buy?</label></br>
                                        <input type="number" name="amountOfTickets" step="1" placeholder="0">
                                        <button type="submit">Buy Tickets</button>
                                    </form>
                                </c:if>

                                <c:if test="${user.role == 'administrator'}">
                                    <form action="controller" method="post">
                                        <input name="command" type="hidden" value="cancelExhibition">
                                        <input name="canceledExhibitionId" type="hidden" value="${exhibition.id}">
                                        <button type="submit">Cancel Exhibitions</button>
                                    </form>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>

                    </c:if>

                </c:forEach>


            </table>
        </c:otherwise>
    </c:choose>


</div>

</body>
</html>