<%-- 
    Document   : register
    Created on : Aug 27, 2016, 11:33:38 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>


    </head>
    <body>
        <h1>Registration</h1>

        <c:url var="formAction" value="/Controller?action=doRegistration" />
        <c:if test="${conflict == 'true'}">
            <font color="red">User Email has already been registered!</font><br/>
        </c:if>
        <c:if test="${mismatch == 'true'}">
            <font color="red">Password Mismatch!</font><br/>
        </c:if>

        <form id="registerForm" name="registerForm" action="${formAction}" method="post">
            Email Username: <input id="userName" name="userName" type="text"/><br/>
            Password: <input id="password" name="password" type="password"/><br/>
            Re-enter Password: <input id="password" name="passwordAgain" type="password"/><br/>
            Mobile Number: <input id="mobileNumber" name="mobileNumber" type="text"/><br/>

            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Register" /> <input type="button" value="Cancel" onclick="document.getElementById('loginForm').action = '${formAction}';
                    document.getElementById('loginForm').submit();" />
        </form>
    </body>
</html>
