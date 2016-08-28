<%-- 
    Document   : login
    Created on : Aug 27, 2016, 4:20:46 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MTiX Log In or Register</title>
    </head>
    <body>
        <h1>MTiX Platform</h1>

        <c:url var="formAction" value="/Controller?action=doLogin" />
        <c:if test="${verification == 'true'}">
            <font color="red">Account has not been verified!</font><br/>
        </c:if>
        <c:if test="${registered == 'true'}">
            <font color="red">Account has been Registered! Please verify it through your email!</font><br/>
        </c:if>

        <form id="loginForm" name="loginForm" action="${formAction}" method="post">
            Email Username: <input id="userName" name="userName" type="text"/><br/>
            Password: <input id="password" name="password" type="password"/><br/>

            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Log In" /> 
        </form>

        <c:url var="linkHref" value="/Controller?action=register" />
    <li><a href="${linkHref}">Register</a></li>
</body>
</html>
