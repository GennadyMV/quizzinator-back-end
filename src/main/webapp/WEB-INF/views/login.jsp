<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>

<head>
    <title>Login</title>
    <link href="<c:url value="/assets/js/vendor/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet"/>
</head>

    <div style="max-width: 1200px; margin: 0px auto; padding: 25px;">
        <div class="page-header">
          <h1>Login with Username and Password</h1>
        </div>

        <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${not empty msg}">
                <div class="alert alert-info">${msg}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
              <label>Username</label>
              <input type='text' name='username' value='' class="form-control">
            </div>

            <div class="form-group">
              <input type='password' name='password' class="form-control">
            </div>

            <div class="form-group">
              <input name="submit" type="submit" class="btn btn-primary" value="Login" />
            </div>
        </form>

        <a class="btn btn-default" href="${pageContext.request.contextPath}/registration">Register</a>
    </div>

</body>
</html>
