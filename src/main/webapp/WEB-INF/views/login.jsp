<html>
    
<head>
    <title>Login Page</title>
</head>

    <div>
        <h3>Login with Username and Password</h3>
        
        <c:if test="${not empty error}">
                <div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty msg}">
                <div class="msg">${msg}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <table>
                <tr>
                        <td>User:</td>
                        <td><input type='text' name='username' value=''></td>
                </tr>
                <tr>
                        <td>Password:</td>
                        <td><input type='password' name='password' /></td>
                </tr>
                <tr>
                        <td>
                        <input name="submit" type="submit" value="Login" />
                        </td>
                </tr>
           </table>
        </form>
        
        <a href="${pageContext.request.contextPath}/registration">Register</a>
    </div>
 
</body>
</html>