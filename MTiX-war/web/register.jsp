<%-- 
    Document   : register
    Created on : Aug 27, 2016, 11:33:38 PM
    Author     : Student-ID
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>


    </head>
    <body style="background-color: skyblue">
        <%
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i <= 5; i++) {
                sb.append((char) (int) (Math.random() * 79 + 23 + 7));
            }
            String cap = new String(sb);
        %>  

        <h1>Registration</h1>

       

        <c:url var="formAction" value="/Controller?action=doRegistration" />
        <c:if test="${conflict == 'true'}">
            <font color="red">User Email has already been registered!</font><br/>
            </c:if>
            <c:if test="${mismatch == 'true'}">
            <font color="red">Password Mismatch!</font><br/>
            </c:if>
            <c:if test="${captcha == 'true'}">
            <font color="red">Captcha entered incorrectly!</font><br/>
            </c:if>

        <form id="registerForm" name="registerForm" onsubmit="return validation()" action="${formAction}" method="post">
            Email Username: <input id="userName" name="userName" type="text"/><br/>
            Password: <input id="password" name="password" type="password"/><br/>
            Re-enter Password: <input id="password" name="passwordAgain" type="password"/><br/>
            Mobile Number: <input id="mobileNumber" name="mobileNumber" type="text"/><br/><br/>

            <div >
                <h2>Enter the Captcha</h2>  
                <script type ="text/javascript">
                    function validation() {
                        var c = document.forms ["registerForm"]["cap1"].value;
                        if (c == null || c == "")
                        {
                            alert("Please Enter Captcha");
                            return false;
                        }
                    }
                </script> 

                <table border="0">  
                    <tbody>  
                        <tr>  
                            <td>  
                                <div style="background-color: aqua"><h2><s><i><font face="casteller"><%=cap%></font></i></s></h2></div>  
                            </td>  
                            <td><input type="text" name="cap1" value="" /></td>  
                            <td><input type="hidden" name="cap2" value='<%=cap%>' readonly="readonly" </td>  
                        </tr>  
                    </tbody>  
                </table>  

                <c:url var="formAction" value="/Controller" />
                <input type="submit" value="Register" /> 
        </form>




    </body>
</html>
