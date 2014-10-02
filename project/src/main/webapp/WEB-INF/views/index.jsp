<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quiznator</title>
        <!-- Site -->
        <link href="<c:url value="/assets/css/site.min.css"/>" rel="stylesheet"/>
        <script src="<c:url value="/assets/js/quiznator.min.js"/>"></script>

        <!-- Bootstrap -->
        <link href="<c:url value="/assets/js/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <script src="<c:url value="/assets/js/vendor/bootstrap/js/bootstrap.min.js"/>"></script>

        <!-- Font awesome -->
        <link href="<c:url value="/assets/js/vendor/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet"/>
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
                    <ul class="nav navbar-nav">
                        <li><a href="#/quiz/all">Quizes</a></li>
                    </ul>
                    <a href="#/quiz/new" class="btn btn-success navbar-btn navbar-right"><i class="fa fa-plus"></i> Create a quiz</a>
                </div>
            </div>
        </nav>

        <div id="main-container">
            <div class="wrapper" ng-view></div>
        </div>
    </body>
</html>
