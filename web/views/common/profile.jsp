<%-- 
    Document   : profile
    Created on : Mar 19, 2025, 12:22:53 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/views/common/header.jsp" %>
        <div style="padding: 20px;">
    <h2>Cập nhật Hồ sơ Cá nhân</h2>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <c:if test="${param.success == '1'}">
        <p style="color: green;">Cập nhật hồ sơ thành công!</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/user/profile" method="post">
        <div class="form-group">
            <label>Họ và Tên:</label>
            <input type="text" name="fullName" value="${user.fullName}" required>
        </div>
        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" value="${user.email}" required>
        </div>
        <div class="form-group">
            <label>Lớp:</label>
            <input type="text" name="className" value="${user.className}">
        </div>
        <div class="form-group">
            <label>Trường:</label>
            <input type="text" name="school" value="${user.school}">
        </div>
        <div class="form-group">
            <label>Số điện thoại:</label>
            <input type="tel" name="phone" value="${user.phone}" required>
        </div>
        <div class="form-group">
            <button type="submit">Cập nhật</button>
        </div>
    </form>
    <style>
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: inline-block;
            width: 100px;
        }
        input {
            padding: 5px;
            width: 200px;
        }
        button {
            padding: 5px 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</div>
    </body>
</html>
