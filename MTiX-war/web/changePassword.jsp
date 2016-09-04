<%-- 
    Document   : changePassword
    Created on : Sep 4, 2016, 2:51:18 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Resetted Password</title>
    </head>
    <body>
        <c:url var="formAction" value="/Controller?action=change" />
        <h1>Change Resetted Password</h1>
        
        <form id="verifyForm" name="verifyForm" action="${formAction}" method="post">
            New Password: <input id="password" name="newPass" type="password"/><br/>
            Verify New Password Again: <input id="password2" name="newPass2" type="password"/><br/>

          

            <input type="hidden" name="userName" value=<%= request.getAttribute("username")%> readonly="readonly" />
            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Change Password" /> 

        </form>
    </body>
</html>
