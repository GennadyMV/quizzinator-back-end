<html>
<head>
<title>Registration Page</title>

</head>
 
    <div>

        <h3>Register with Username and Password</h3>
        
        <c:if test="${not empty error}">
                <div class="error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/registration" method="post">
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
                        <td>Confirm password:</td>
                        <td><input type='password' name='confirmPassword' /></td>
                </tr>
                <tr>
                        <input name="submit" type="submit" value="Register" />
                        </td>
                </tr>
           </table>
        </form>
    </div>
 
</body>
</html>