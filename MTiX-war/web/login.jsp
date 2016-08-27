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
        <form action="loggedIn" method="GET">
            <table>
                <tr>
                    <td align="right">Username:</td>
                    <td align="left"><input type="text" name="name" length="30"/></td>                    
                </tr>
                <tr>
                    <td align="right">Password:</td>
                    <td align="left"><input type="password" name="password" length="30"/></td>                    
                </tr>

            </table>
            <p><input type="submit" value="login"/></p><br><br>
        </form>
    <c:url var="linkHref" value="/Controller?action=register" />
    <li><a href="${linkHref}">Register</a></li>
</body>
</html>
