<%-- 
    Document   : editMain
    Created on : Sep 20, 2016, 10:07:16 AM
    Author     : Student-ID
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<jsp:include page="header.jsp" />
<!-- Main Content -->
<!-- Main Content -->
<div class="container-fluid">
    <%
        List<ArrayList> data = (List<ArrayList>) request.getAttribute("data");
    %>
    <c:url var="formAction" value="/BackController?action=editSelectSessions" />
    <div class="side-body padding-top">

        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="header">
                        <h4 class="title">Edit Session</h4>   
                    </div>                        
                    <c:if test="${success == 'true'}">
                        <font color="red">Session edited Successfully!</font><br/>
                    </c:if>
                    <c:if test="${error == 'true'}">
                        <font color="red">Error: Time Overlap! Please enter the correct date!</font><br/>
                    </c:if>
                    <form id="contact_form" action="${formAction}" method="POST"> 
                        <table align="center">
                            <tr>
                                <td>&nbsp</td> 
                            </tr>
                            <tr>
                                <td>&nbsp</td> 
                            </tr>
                        </table><br>
                        <%
                            if (data.size() == 0) {
                        %>
                        <div align="center"><h3>No Event Found!</h3></div><br><br>
                        <%} else {
                        %>
                        <table id="table" class="table table-hover">
                            <thead>
                                <tr>
                                    <th data-field="state" data-radio="true"></th>
                                    <th data-field="type">Event Type</th>
                                    <th data-field="name">Name</th>
                                    <th data-field="start">Start Date</th>
                                    <th data-field="end">End Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (int i = 0; i < data.size(); i++) {
                                %>
                                <tr>
                                    <td><input type="radio" name="id" required="true" value="<%= data.get(i).get(0) + " " + data.get(i).get(4)%>"></td>
                                    <td><%= data.get(i).get(4)%></td>
                                    <td><%= data.get(i).get(1)%></td>
                                    <td><%= data.get(i).get(2)%></td>
                                    <td><%= data.get(i).get(3)%></td></tr>
                                    <% }%>
                            </tbody>

                        </table>
                        <table align="center">
                            <tr>
                                <td>&nbsp</td> 
                            </tr>
                            <tr>
                                <td>&nbsp</td> 
                                <td>&nbsp</td> 
                                <td><c:url var="formAction" value="/BackController" /><input type="submit" value="Go"/></td>
                            </tr>
                        </table>
                        <%}%>


                    </form> 
                </div>
            </div>
        </div> 
    </div>
</div>

<jsp:include page="footer.jsp" />


