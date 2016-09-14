<%-- 
    Document   : readMessage
    Created on : Sep 10, 2016, 9:21:42 PM
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

        <title>Read Message</title>

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


        <!--     Fonts and icons     -->
        <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
        <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />

    </head>
    <body>

        <c:url var="formAction" value="/BackController?action=replyMessage" />

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


                            <ul class="nav navbar-nav navbar-right">


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

                            <c:if test="${missend == 'true'}">
                                <font color="red">Account to which message is to be sent does not exist!</font><br/>
                            </c:if>
                            <c:if test="${sent == 'true'}">
                                <font color="red">Message successfully sent!</font><br/>
                            </c:if>
                            <div class="col-md-8">
                                <div class="card">
                                    <div class="header">
                                        <h4 class="title">Message</h4>   
                                    </div>
                                    <div class="content">             
                                        <form id="contact_form" action="${formAction}" method="POST">
                                            <div class="row">
                                                <label for="name">From:</label><br />
                                                <p><%= ((ArrayList) request.getAttribute("message")).get(0)%> </p>
                                            </div>
                                            <div class="row">
                                                <label for="email">Subject:</label><br />
                                                <p><%= ((ArrayList) request.getAttribute("message")).get(1)%> </p>
                                            </div>
                                            <div class="row">
                                                <label for="message">Message:</label><br />
                                                <p><%= ((ArrayList) request.getAttribute("message")).get(2)%> </p>
                                            </div>
                                            <input type="hidden" name="username" value=<%= request.getAttribute("username")%> readonly="readonly" />
                                            <input type="hidden" name="receiver" value=<%= ((ArrayList) request.getAttribute("message")).get(0)%> readonly="readonly" />
                                            <input type="hidden" name="messageid" value=<%= ((ArrayList) request.getAttribute("message")).get(3)%> readonly="readonly" />
                                            <c:url var="formAction" value="/BackController" />
                                            <input type="submit" value="Reply message" />
                                        </form>						

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
