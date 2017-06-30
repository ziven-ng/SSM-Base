<%--
  Created by IntelliJ IDEA.
  User: ziven
  Date: 2017/6/30
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${empty item.id?"添加":"修改"}用户</title>
</head>
<body>
<form action="save" method="post">
    <input name="id" value="${item.id}" hidden>
    <label for="name">姓名：</label>
    <input type="text" id="name" name="name" value="${item.name}">
    <br/>
    <label for="name">年龄：</label>
    <input type="text" id="age" name="age" value="${item.age}">
    <br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
