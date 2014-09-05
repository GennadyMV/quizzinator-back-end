<%-- 
    Document   : index
    Created on : 5.9.2014, 12:59:09
    Author     : Lenovo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="<c:url value="/assets/js/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/assets/css/site.css"/>" rel="stylesheet">
        <!-- jQuery -->
        <script src="<c:url value="/assets/js/vendor/jquery.min.js"/>"></script>
        <script src="<c:url value="/assets/js/vendor/jquery-ui.min.js"/>"></script>

        <!-- Angular files -->
        <script src="<c:url value="/assets/js/vendor/angular.min.js"/>"></script>
        <script src="<c:url value="/assets/js/vendor/angular-route.js"/>"></script>

        <!-- Application files -->
        <script src="<c:url value="/assets/js/app/app.js"/>"></script>
        <script src="<c:url value="/assets/js/app/services/quiz_api_service.js"/>"></script>
        <script src="<c:url value="/assets/js/app/controllers/create_quiz_controller.js"/>"></script>
        <script src="<c:url value="/assets/js/app/directives/sortable.js"/>"></script>

        <!-- Bootstrap -->
        <script src="<c:url value="/assets/js/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
        <link href="<c:url value="/assets/js/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet"/>

        <!-- Font awesome -->
        <link href="<c:url value="/assets/js/vendor/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet"/>

        <!-- Additional CSS files -->
        <link href="<c:url value="/assets/css/site.css"/>" rel="stylesheet"/>

    </head>

    <body ng-app="QuizApp">
        <nav class="navbar navbar-default" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <i class="fa fa-bars"></i>
                    </button>
                    <a class="navbar-brand" href="#">Quiznator</a>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <!--<ul class="nav navbar-nav">
                      
                    </ul>-->
                    <a href="#/quiz/new" class="btn btn-success navbar-btn navbar-right"><i class="fa fa-plus"></i> Create a quiz</a>
                </div>
            </div>
        </nav>

        <div id="main-container">
            <div class="wrapper" ng-view></div>
        </div>
    </body>
</html>
