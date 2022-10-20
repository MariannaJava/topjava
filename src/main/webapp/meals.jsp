<%--
  Author: Marianna Dorohova.
  Date: 18.10.22.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%--
<%@ page contentType="text/html;charset=UTF-8" %>
--%>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<a href="MealController?action=insert">Add meal</a>

<table border="1" style="border-collapse:collapse;">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsTo}"
               var="operation">
    <tr style="color: ${operation.excess ? 'red' : 'green'}">
        <td>

            <fmt:parseDate value="${ operation.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                           type="both"/>
            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/>


        </td>
        <td><c:out value="${operation.description}"></c:out></td>
        <td><c:out value="${operation.calories}"></c:out></td>
            <%--  <td><a href="MealsServlet?action=delete&mealId=<c:out value="${operation.id}"/>">Delete</a></td>--%>
        <td><a href="MealController?action=edit&mealId=<c:out value="${operation.id}"/>">Update</a></td>
        <td><a href="MealController?action=delete&mealId=<c:out value="${operation.id}"/>">Delete</a></td>
        <td><c:out value="${operation.id}"></c:out>

        </c:forEach>
    </tr>


</table>
</body>
</html>