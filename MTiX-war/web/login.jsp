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
        <% response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1 
            response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
            response.setDateHeader("Expires", 0); //prevents caching at the proxy server  
        %>
    </head>
    <body>

        <h1>MTiX Platform</h1>

        <c:url var="formAction" value="/Controller?action=doLogin" />
        <c:if test="${verification == 'true'}">
            <font color="red">Account has not been verified!</font><br/>
        </c:if>
        <c:if test="${wronguser == 'true'}">
            <font color="red">Account does not exist!</font><br/>
        </c:if>
        <c:if test="${registered == 'true'}">
            <font color="red">Account has been Registered! Please verify it through your email!</font><br/>
        </c:if>
        <c:if test="${accountverified == 'true'}">
            <font color="red">Account has been verified!</font><br/>
        </c:if>
        <c:if test="${halflock == 'true'}">
            <font color="red">Wrong password!</font><br/>
        </c:if>
        <c:if test="${locked == 'true'}">
            <font color="red">Account has been locked. Please refer to email to unlock it.</font><br/>
        </c:if>
        <c:if test="${unlock == 'true'}">
            <font color="red">Account has been unlocked. Try resetting password if necessary.</font><br/>
        </c:if>
        <c:if test="${reset == 'true'}">
            <font color="red">Account password has been reset. Refer to email sent for new password.</font><br/>
        </c:if>
        <c:if test="${lockreset == 'true'}">
            <font color="red">Password cannot be reset as account has been locked. Refer to email to unlock account first.</font><br/>
        </c:if>
        <c:if test="${change == 'true'}">
            <font color="red">Account password has been reset. Login with new password.</font><br/>
        </c:if>
        <c:if test="${error == 'true'}">
            <font color="red">Session has ended.  Please login.</font><br/>
        </c:if>

        <form id="loginForm" name="loginForm" action="${formAction}" method="post">
            Email Username: <input id="userName" name="userName" type="text"/><br/>
            Password: <input id="password" name="password" type="password"/><br/>

            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Log In" /> 
        </form>

        <c:url var="linkHref" value="/Controller?action=register" />
    <li><a href="${linkHref}">Register</a></li>
        <c:url var="linkHref2" value="/Controller?action=resetPassword" />
    <li><a href="${linkHref2}">Reset Password</a></li>


</body>
</html>
