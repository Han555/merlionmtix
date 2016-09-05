<%-- 
    Document   : home
    Created on : Aug 28, 2016, 12:55:08 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <% response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1 
            response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
            response.setDateHeader("Expires", 0); //prevents caching at the proxy server  
        %>

        <META HTTP-EQUIV="refresh" CONTENT="<%= session.getMaxInactiveInterval()%>; URL=http://localhost:8080/MTiX-war/Controller?action=logout" />
        <title>Home Page</title>
    </head>
    <body>
        <h1>Welcome Home!</h1>

        <c:url var="formAction" value="/Controller?action=message" />
        <form id="resetForm" name="resetForm" action="${formAction}" method="post">
            Email Username: <input id="userName" name="userName" type="text"/><br/>

            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Reset Password" /> 
        </form>
    </body>
</html>
