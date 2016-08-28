<%-- 
    Document   : verification
    Created on : Aug 28, 2016, 4:14:12 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Verification</title>
    </head>
    <body>

        <h1>Account Verification</h1>

        <c:url var="formAction" value="/Controller?action=verify" />
        <c:if test="${oldpass == 'true'}">
            <font color="red">Old Password mismatch!</font><br/>
        </c:if>
        <c:if test="${matchpass == 'true'}">
            <font color="red">New Passwords mismatch!</font><br/>
        </c:if>


        <form id="verifyForm" name="verifyForm" action="${formAction}" method="post">
            Old Password: <input id="userName" name="oldPass" type="password"/><br/>
            New Password: <input id="password" name="newPass" type="password"/><br/>
            Verify New Password: <input id="password" name="newPass2" type="password"/><br/>

            <c:if test="${oldpass == 'true'}">
                <input type="hidden" name="userName" value=<%= request.getAttribute("verifyUser")%> readonly="readonly" />
            </c:if>
            <c:if test="${matchpass == 'true'}">
                <input type="hidden" name="userName" value=<%= request.getAttribute("verifyUser")%> readonly="readonly" />
            </c:if>

            <input type="hidden" name="userName" value=<%= request.getParameter("name")%> readonly="readonly" />
            <c:url var="formAction" value="/Controller" />
            <input type="submit" value="Verify Account" /> 

        </form>

    </body>
</html>
