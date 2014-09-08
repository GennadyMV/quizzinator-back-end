<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <script src="<c:url value="/assets/js/vendor/jquery.min.js"/>"></script>

        <!-- Handlebars -->
        <script src="<c:url value="/assets/js/vendor/handlebars.js"/>"></script>

        <!-- Bootstrap -->
        
        <script src="<c:url value="/assets/js/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
        <link href="<c:url value="/assets/js/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet"/>

        <!-- Application files -->
        <script src="<c:url value="/assets/js/lib/modules/api_module.js"/>"></script>
        <script src="<c:url value="/assets/js/lib/modules/template_module.js"/>"></script>
        <script src="<c:url value="/assets/js/lib/quiz.js"/>"></script>

        <!-- Font awesome -->
        <link href="<c:url value="/assets/js/vendor/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet"/>
    </head>
    <body>
        <div class="quiz-container" data-quizId="1"></div>

        <script>
            $('.quiz-container').quiz();
        </script>
    </body>
</html>
