<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Storage Application</title>
</head>
<body>
<center>

</center>
<div align="center">

  <form action="insert" method="post">
    <table border="1" cellpadding="5">
      <caption>
        <h2>
            Add New Item
        </h2>
      </caption>

      <tr>
        <th>Title: </th>
        <td>
          <input type="text" name="title" size="45"
                 value="<c:out value='${item.title}' />"
                  />
        </td>
      </tr>
      <tr>
        <th>Quantity: </th>
        <td>
          <input type="text" name="quantity" size="10"
                 value="<c:out value='${item.quantity}' />"
                  />
        </td>
      </tr>
      <tr>
        <th>Price: </th>
        <td>
          <input type="text" name="price" size="10"
                 value="<c:out value='${item.price}' />"
                  />
        </td>
      </tr>
      <tr>
        <th>Location: </th>
        <td>
          <input type="text" name="location" size="45"
                 value="<c:out value='${item.location}' />"
                  />
        </td>
      </tr>
      <tr>
        <td colspan="2" align="center">
          <input type="submit" value="Save" />
        </td>
      </tr>
    </table>
  </form>
  <a href="/list">List of Items</a>

</div>
</body>
</html>
