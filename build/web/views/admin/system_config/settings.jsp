<%-- 
    Document   : settings
    Created on : Feb 27, 2025, 2:40:00 PM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>System Configuration Settings</title>
    <!-- Include Bootstrap CSS for styling -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 600px;
            margin-top: 50px;
        }
        .message {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center mb-4">System Configuration Settings</h1>

        <!-- Display success or error messages -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-${sessionScope.messageType} message" role="alert">
                ${sessionScope.message}
                <!-- Clear the message after displaying -->
                <c:remove var="message" scope="session"/>
                <c:remove var="messageType" scope="session"/>
            </div>
        </c:if>

        <!-- Form to display and update settings -->
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Update Setting</h5>
                <form action="${pageContext.request.contextPath}/admin/system_config" method="post">
                    <div class="mb-3">
                        <label for="settingName" class="form-label">Setting Name</label>
                        <input type="text" class="form-control" id="settingName" name="settingName" value="${settingName}" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="settingValue" class="form-label">Setting Value</label>
                        <input type="text" class="form-control" id="settingValue" name="settingValue" value="${settingValue}" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Update Setting</button>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">Back to Dashboard</a>
                </form>
            </div>
        </div>
    </div>

    <!-- Include jQuery -->
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <!-- Include Bootstrap JS -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <!-- Include custom main.js (if needed) -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>