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
                                                <tr class="unread">


                                                    <td class="view-message">
                                                        <form id="verifyForm" name="verifyForm" action="${formAction}" method="post">

                                                            <input type="hidden" name="messageid" value=<%= ((ArrayList) ((ArrayList) request.getAttribute("board")).get(0)).get(0)%> readonly="readonly" />
                                                            <c:url var="formAction" value="/BackController" />
                                                            <input type="submit" value="Read" /> 
                                                        </form></td>
                                                    <td class="view-message"></td>
                                                    <td class="view-message"><%= ((ArrayList) ((ArrayList) request.getAttribute("board")).get(0)).get(1)%></td>
                                                    <td class="view-message"><span class="label label-danger pull-right">unread</span> </td>
                                                </tr>

                                                <tr class="unread">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Google Webmaster </td>
                                                    <td class="view-message">Improve the search presence of WebSite</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">March 15</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">JW Player</td>
                                                    <td class="view-message">Last Chance: Upgrade to Pro for </td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">March 15</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Tim Reid, S P N</td>
                                                    <td class="view-message">Boost Your Website Traffic</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">April 01</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="view-message dont-show">Freelancer.com <span class="label label-danger pull-right">unread</span></td>
                                                    <td class="view-message">Stop wasting your visitors </td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">May 23</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="view-message dont-show">WOW Slider </td>
                                                    <td class="view-message">New WOW Slider v7.8 - 67% off</td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">March 14</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="view-message dont-show">LinkedIn Pulse</td>
                                                    <td class="view-message">The One Sign Your Co-Worker Will Stab</td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">Feb 19</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Drupal Community<span class="label label-success pull-right">megazine</span></td>
                                                    <td class="view-message view-message">Welcome to the Drupal Community</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">March 04</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Facebook</td>
                                                    <td class="view-message view-message">Somebody requested a new password </td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">June 13</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Skype <span class="label label-info pull-right">family</span></td>
                                                    <td class="view-message view-message">Password successfully changed</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">March 24</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="view-message dont-show">Google+</td>
                                                    <td class="view-message">alireza, do you know</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">March 09</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="dont-show">Zoosk </td>
                                                    <td class="view-message">7 new singles we think you'll like</td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">May 14</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">LinkedIn </td>
                                                    <td class="view-message">Alireza: Nokia Networks, System Group and </td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">February 25</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="dont-show">Facebook</td>
                                                    <td class="view-message view-message">Your account was recently logged into</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">March 14</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Twitter</td>
                                                    <td class="view-message">Your Twitter password has been changed</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">April 07</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">InternetSeer Website Monitoring</td>
                                                    <td class="view-message">http://golddesigner.org/ Performance Report</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">July 14</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="view-message dont-show">AddMe.com</td>
                                                    <td class="view-message">Submit Your Website to the AddMe Business Directory</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">August 10</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Terri Rexer, S P N</td>
                                                    <td class="view-message view-message">Forget Google AdWords: Un-Limited Clicks fo</td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">April 14</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Bertina </td>
                                                    <td class="view-message">IMPORTANT: Don't lose your domains!</td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">June 16</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star inbox-started"></i></td>
                                                    <td class="view-message dont-show">Laura Gaffin, S P N </td>
                                                    <td class="view-message">Your Website On Google (Higher Rankings Are Better)</td>
                                                    <td class="view-message inbox-small-cells"></td>
                                                    <td class="view-message text-right">August 10</td>
                                                </tr>
                                                <tr class="">
                                                    <td class="inbox-small-cells">
                                                        <input type="checkbox" class="mail-checkbox">
                                                    </td>
                                                    <td class="inbox-small-cells"><i class="fa fa-star"></i></td>
                                                    <td class="view-message dont-show">Facebook</td>
                                                    <td class="view-message view-message">Alireza Zare Login faild</td>
                                                    <td class="view-message inbox-small-cells"><i class="fa fa-paperclip"></i></td>
                                                    <td class="view-message text-right">feb 14</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <div class="footer">
                                            <div class="legend">
                                                <i class="fa fa-circle text-info"></i> Open
                                                <i class="fa fa-circle text-danger"></i> Click
                                                <i class="fa fa-circle text-warning"></i> Click Second Time
                                            </div>
                                            <hr>
                                            <div class="stats">
                                                <i class="fa fa-history"></i> Updated 3 minutes ago
                                            </div>
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
