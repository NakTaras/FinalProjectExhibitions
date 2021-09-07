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

        .registration_container {
            padding: 50px 30%;
        }

        .registration_container form {
            border: 3px solid #f1f1f1;
        }

        .registration_container input[type=text], .registration_container input[type=password] {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .registration_container button {
            background-color: #04AA6D;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        .registration_container button:hover {
            opacity: 0.8;
        }

    </style>

</head>
<body>
<div class="topnav">
    <a class="active" href="../index.jsp">Home</a>
    <a href="registration.jsp">Registration</a>
    <a href="../controller?command=getLocations">Add Exhibition</a>
    <a href="addLocation.jsp">Add Location</a>
    <c:choose>
        <c:when test="${sessionScope.user == null}">
            <div class="login-container">
                <form action="../controller" method="post">
                    <input name="command" type="hidden" value="logIn">
                    <input type="text" placeholder="Login" name="login">
                    <input type="password" placeholder="Password" name="password">
                    <button type="submit">Login</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-container">
                <form action="../controller" method="post">
                    <input name="command" type="hidden" value="logOut">
                    <button type="submit">Log out</button>
                </form>
                <div class="logged_user"> You are logged as ${sessionScope.user.role}</div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<h1>Current User: ${sessionScope.user.login}
</h1>
${isAdded == null ? "" : "<h1>User was not added</h1>"}
<h1>Users registration</h1>
<hr>
<form action="../controller" method="post">
    <div class="registration_container">
        <input name="command" type="hidden" value="registration">
        <input name="role" type="hidden" value="client">
        <input type="text" name="login" placeholder="Enter Username">
        <input type="password" name="password" placeholder="Enter Password">
        <button type="submit">Log up</button>
    </div>
</form>
</body>
</html>
