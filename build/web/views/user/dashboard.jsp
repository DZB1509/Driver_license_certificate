<%-- 
    Document   : dashboard
    Created on : Mar 23, 2025, 4:55:08 PM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .welcome-message {
            font-size: 18px;
            margin-bottom: 20px;
            text-align: center;
        }
        .input-box {
            margin-top: 20px;
            text-align: center;
        }
        .input-field {
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 200px;
        }
        input[type="submit"] {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .logout-link {
            display: block;
            margin-top: 20px;
            color: #dc3545;
            text-align: center;
            text-decoration: none;
        }
        .logout-link:hover {
            text-decoration: underline;
        }
        .error-message {
            color: #dc3545;
            margin-top: 10px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div>
        <h1>Chào mừng đến với User Dashboard</h1>
        <p class="welcome-message">Hello, ${loggedUser.fullName}!</p>

        
        <!-- Form gửi yêu cầu đến UserDashboardServlet -->
        <form action="${pageContext.request.contextPath}/UserdashboardServlet" method="get">
            <div class="input-box">
                <select name="role" class="input-field" required>
                    <option value="" disabled selected>Chọn vai trò</option>
                    <option value="student">Student</option>
                    <option value="teacher">Teacher</option>
                    <option value="trafficpolice">Traffic Police</option>
                </select><br>
                <input type="submit" value="Chọn">
            </div>
            <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty error}">
            <p class="error-message">${error}</p>
        </c:if>

        </form>
        <a style="color: black" class="logout-link" href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</body>
</html>