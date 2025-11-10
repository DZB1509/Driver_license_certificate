<%-- 
    Document   : maintenance
    Created on : Mar 2, 2025, 6:28:42 PM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>System Maintenance Mode</title>
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
        .form-check-label {
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center mb-4">System Maintenance Mode</h1>

        <!-- Display success or error messages -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-${sessionScope.messageType} message" role="alert">
                ${sessionScope.message}
                <!-- Clear the message after displaying -->
                <c:remove var="message" scope="session"/>
                <c:remove var="messageType" scope="session"/>
            </div>
        </c:if>

        <!-- Form to toggle maintenance mode -->
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Maintenance Mode Settings</h5>
                <form action="${pageContext.request.contextPath}/admin/system_config/maintenance" method="post">
                    <div class="mb-3 form-check form-switch">
                        <input class="form-check-input" type="checkbox" id="maintenanceMode" name="maintenanceMode" value="on" 
                               <c:if test="${maintenanceMode}">checked</c:if>>
                        <label class="form-check-label" for="maintenanceMode">
                            Enable Maintenance Mode
                        </label>
                    </div>
                    <button type="submit" class="btn btn-primary">Update</button>
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