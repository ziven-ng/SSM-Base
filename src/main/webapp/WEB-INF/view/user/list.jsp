<%--
  Created by IntelliJ IDEA.
  User: ziven
  Date: 2017/6/29
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户列表</title>
</head>
<body>
<button onclick="location.href='edit'">添加用户</button>
<table border="1">
    <tr>
        <th>ID</th>
        <th>名字</th>
        <th>年龄</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${userList}" var="item">
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.age}</td>
            <td>
                <a href="${pageContext.request.contextPath}/user/edit?id=${item.id}">修改</a>
                <a href="${pageContext.request.contextPath}/user/delete?id=${item.id}">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
