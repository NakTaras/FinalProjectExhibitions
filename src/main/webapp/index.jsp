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
            padding: 50px 30%;
        }

        @media screen and (max-width: 600px) {
            .topnav .login-container {
                float: none;
            }

            .topnav a, .topnav input[type=text], .topnav .login-container button {
                float: none;
                display: block;
                text-align: left;
                width: 100%;
                margin: 0;
                padding: 14px;
            }

            .topnav a, .topnav input[type=password], .topnav .login-container button {
                float: none;
                display: block;
                text-align: left;
                width: 100%;
                margin: 0;
                padding: 14px;
            }

            .topnav input[type=text] {
                border: 1px solid #ccc;
            }

            .topnav input[type=password] {
                border: 1px solid #ccc;
            }
        }

        .events td {
            font-size: 13pt;
            padding: 5px 10px;

            border: 10px groove;
            border-color: #404040 #919191 #404040 #919191;
        }

        .events table {
            caption-side: top;

        }
    </style>

</head>
<body>
<div class="topnav">
    <a class="active" href="index.jsp">Home</a>
    <a href="jsp/registration.jsp">Registration</a>
    <a href="controller?command=getLocations">Add Exhibition</a>
    <a href="jsp/addLocation.jsp">Add Location</a>
    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <div class="login-container">
                <form action="controller" method="post">
                    <input name="command" type="hidden" value="logIn">
                    <input type="text" placeholder="Login" name="login">
                    <input type="password" placeholder="Password" name="password">
                    <button type="submit">Login</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-container">
                <form action="controller" method="post">
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

    <form action="controller" method="get">
        <input name="command" type="hidden" value="getExhibitions">
        <button type="submit">Get Exhibitions</button>
    </form>

</div>

<%--<table align="center" class="events" cellspacing="9">--%>

<%--    <tr>--%>
<%--        <td><img src="Liter.png" width=316px height=448px></td>--%>
<%--        <td width=500px>Ви чекали, ми теж, і ось, цей день настав! Сьогодні "Лампа" анонсує свою першу подію</br>--%>
<%--            Так, нарешті, після довгих очікувань та багатогодинної праці ми нарешті готові вам розповісти!</br>--%>
<%--            І нашою першою самостійною подією буде літературний вечір.</br>--%>
<%--            На цьому ламповому літературнику ви зможете насолодитись творами талановитих львівських поетів, а також, за--%>
<%--            бажанням, зможете і самі виступити.</br>--%>
<%--            В цей холодний осінній вечір ми зможемо подарувати вам клаптик світла і тепла від лампи і дати змогу--%>
<%--            відпочити після навчання чи роботи. Тому ми чекаємо саме тебе на нашому вечорі літературних читань, приходь--%>
<%--            і надихнись атмосферою та мистецтвом</br>--%>
<%--            </br></br><big>Коли? 17-го вересня 19:00</br>--%>
<%--                Де? МолодоЗелено вул. Джерельна, 20</br>--%>
<%--                Столик можна забронювати за телефоном: +380673783728</br>--%>
<%--                Посилання для реєстрації поетів в описі профілю</big>--%>
<%--        </td>--%>
<%--    </tr>--%>

<%--</table>--%>
</body>
</html>