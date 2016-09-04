<%-- 
    Document   : resetPassword
    Created on : Sep 4, 2016, 1:17:28 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
    </head>
    <body>
        <h1>Reset Password</h1>
        
        <c:if test="${absent == 'true'}">
            <font color="red">User account does not exist! Please try again!</font><br/>
        </c:if>
        
        <c:url var="formAction" value="/Controller?action=sendResetPassword" />
        <form id="resetForm" name="resetForm" action="${formAction}" method="post">
            Email Username: <input id="userName" name="userName" type="text"/><br/>

            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Reset Password" /> 
        </form>
    </body>
</html>
