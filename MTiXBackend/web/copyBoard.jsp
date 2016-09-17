<%-- 
    Document   : bulletinBoard
    Created on : Sep 12, 2016, 12:41:38 PM
    Author     : Student-ID
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="icon" type="image/png" href="assets/img/favicon.ico">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

        <title>Bulletin Board</title>

        <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
        <meta name="viewport" content="width=device-width" />


        <!-- Bootstrap core CSS     -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" />

        <!-- Animation library for notifications   -->
        <link href="assets/css/animate.min.css" rel="stylesheet"/>

        <!--  Light Bootstrap Table core CSS    -->
        <link href="assets/css/light-bootstrap-dashboard.css" rel="stylesheet"/>


        <!--  CSS for Demo Purpose, don't include it in your project     -->
        <link href="assets/css/demo.css" rel="stylesheet" />

        <link rel='stylesheet prefetch' href='http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css'>


        <!--     Fonts and icons     -->
        <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
        <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />

    </head>
    <body>
        <c:url var="formAction" value="/BackController?action=readBulletin" />


        <div class="wrapper">
            <div class="sidebar" data-color="orange" data-image="assets/img/sidebar-5.jpg">

                <!--
            
                    Tip 1: you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple"
                    Tip 2: you can also add an image using data-image tag
            
                -->

                <div class="sidebar-wrapper">
                    <div class="logo">
                        <p ><%= request.getAttribute("username")%> </p>
                    </div>

                    <ul class="nav">

                        <li>
                            <a href="user.html">
                                <i class="pe-7s-user"></i>
                                <p>User Profile</p>
                            </a>
                        </li>
                        <li>
                            <c:if test="${role == 'super administrator'}">

                                <c:url var="linkHref" value="/BackController?action=createAccount" />
                                <a  href="${linkHref}">
                                    <i class="pe-7s-user"></i>
                                    <p>Create Account</p>
                                </a>
                            </c:if>
                        </li>

                    </ul>
                </div>
            </div>

            <div class="main-panel">
                <nav class="navbar navbar-default navbar-fixed">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-example-2">
                                <span class="sr-only">Toggle navigation</span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>
                            <c:url var="linkHref" value="/BackController?action=home" />
                            <a class="navbar-brand" href="${linkHref}">Home</a>
                        </div>
                        <div class="collapse navbar-collapse">
                            <ul class="nav navbar-nav navbar-left">

                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-globe"></i>
                                        <b class="caret"></b>
                                        <span class="notification">5</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a href="#">Notification 1</a></li>
                                        <li><a href="#">Notification 2</a></li>
                                        <li><a href="#">Notification 3</a></li>
                                        <li><a href="#">Notification 4</a></li>
                                        <li><a href="#">Another notification</a></li>
                                    </ul>
                                </li>

                            </ul>

                            <ul class="nav navbar-nav navbar-right">
                                <li>
                                    <c:if test="${role == 'super administrator'}">
                                        <c:url var="linkHref" value="/BackController?action=composeBulletin" />
                                        <a href="${linkHref}">
                                            New Bulletin
                                        </a>
                                    </c:if>
                                </li>

                                <li>
                                    <a href="#">
                                        Log out
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>


                <div class="content">
                    <div class="container-fluid">
                        <div class="row">


                            <c:if test="${created == 'true'}">
                                <font color="red">Bulletin message successfully broadcast!</font><br/>
                            </c:if>


                            <div class="col-md-8">
                                <div class="card">
                                    <div class="header">
                                        <h4 class="title">Bulletin Board</h4>

                                    </div>
                                    <c:if test="${created == 'true'}">
                                        <font color="red">Bulletin message successfully broadcast!</font><br/>
                                    </c:if>
                                    <div class="content">
                                        <table class="table table-inbox table-hover">
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th></th>
                                                    <th>Subject</th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    int size = (int) Integer.parseInt((String) request.getAttribute("recordSize"));
                                                    ArrayList<ArrayList<String>> boardPage = (ArrayList<ArrayList<String>>) request.getAttribute("board");
                                                    for (int i = 0; i < size; i++) {
                                                        String id = boardPage.get(i).get(0);
                                                        String subject = boardPage.get(i).get(1);
                                                %>

                                                <c:url var="formAction" value="/BackController?action=readBulletin" />

                                                <tr class="unread">


                                                    <td class="view-message">
                                                        <form id="verifyForm" name="verifyForm" action="${formAction}" method="post">

                                                            <input type="hidden" name="messageid" value=<%= id%> readonly="readonly" />
                                                            <c:url var="formAction" value="/BackController" />
                                                            <input type="submit" value="Read" /> 
                                                        </form></td>
                                                    <td class="view-message"></td>
                                                    <td class="view-message"><%= subject%></td>
                                                    <td class="view-message"> </td>
                                                </tr>
                                                <%
                                                    }
                                                %>

                                            </tbody>
                                        </table>
                                        <table border="1" cellpadding="5" cellspacing="5">
                                            <tr>
                                                <c:forEach begin="1" end="${noOfPages}" var="i">
                                                    <c:choose>
                                                        <c:when test="${currentPage eq i}">
                                                            <td>${i}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td>
                                                                <c:url var="linkHref" value="/BackController?action=bulletinBoard&page=${i}" />

                                                                <a href="${linkHref}">${i}</a></td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                            </tr>
                                        </table>
                                        <div class="footer">

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>




                    </div>
                </div>


                <footer class="footer">
                    <div class="container-fluid">
                        <nav class="pull-left">
                            <ul>

                                <li>
                                    <a href="#">
                                        Company
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        Portfolio
                                    </a>
                                </li>

                            </ul>
                        </nav>
                        <p class="copyright pull-right">
                            &copy; 2016 MTIC
                        </p>
                    </div>
                </footer>

            </div>
        </div>


    </body>

    <!--   Core JS Files   -->
    <script src="assets/js/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="assets/js/bootstrap.min.js" type="text/javascript"></script>

    <!--  Checkbox, Radio & Switch Plugins -->
    <script src="assets/js/bootstrap-checkbox-radio-switch.js"></script>

    <!--  Charts Plugin -->
    <script src="assets/js/chartist.min.js"></script>

    <!--  Notifications Plugin    -->
    <script src="assets/js/bootstrap-notify.js"></script>

    <!--  Google Maps Plugin    -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

    <!-- Light Bootstrap Table Core javascript and methods for Demo purpose -->
    <script src="assets/js/light-bootstrap-dashboard.js"></script>

    <!-- Light Bootstrap Table DEMO methods, don't include it in your project! -->
    <script src="assets/js/demo.js"></script>



</html>
