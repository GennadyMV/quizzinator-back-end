<!doctype html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>kyselyt</title>
        <style>
            table, td, tr {
                border: 1px solid black;
                border-collapse: collapse;
            }
        </style>
    </head>

    <h1>Kyselyt</h1>
    <c:if test="${not empty quizzes}">
    <table>
        <tr><th>id</th><th>kysymys</th></tr>
        <c:forEach var="quiz" items="${quizzes}">
            <tr>
                <td>${quiz.id}</td>
                <td>${quiz.question}</td>
            </tr>
        </c:forEach>
    </table>
    </c:if>
    

    <h2>Lis‰‰ kysely</h2>
    <form method="post" action="/quiz">
        <textarea name="question"></textarea><br>
        <button type="submit">tallenna</button>
    </form>
</html>
