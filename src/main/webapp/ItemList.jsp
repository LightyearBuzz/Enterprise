<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Storage Application</title>

</head>
<body>
<center>
  <h1>List of Items</h1>
</center>
<div align="center">
  <table border="1" cellpadding="5">
    <tr>
      <th>ID</th>
      <th>Title</th>
      <th>Quantity</th>
      <th>Price</th>
      <th>Location</th>
      <th>Actions</th>
    </tr>
    <c:forEach var="item" items="${requestScope.listItems}">
      <tr>
        <td><c:out value="${item.id}" /></td>
        <td><c:out value="${item.title}" /></td>
        <td><c:out value="${item.quantity}" /></td>
        <td><c:out value="${item.price}" /></td>
        <td><c:out value="${item.location}" /></td>
        <td>
          <a href="/edit?id=<c:out value='${item.id}' />">Edit</a>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <a href="/delete?id=<c:out value='${item.id}' />">Delete</a>
        </td>
      </tr>
    </c:forEach>
    <tr>
      <td colspan="6" align="right">
        <a href="/new">Add New Item</a>
      </td>
    </tr>

  </table>
  <br>
  <br>
  <br>

</div>
</body>
</html>