<%-- 
    Document   : View_course
    Created on : Mar 17, 2025, 6:25:06 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course Details - Student</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .details-container { max-width: 600px; margin: 20px auto; }
        .error { color: red; }
    </style>
    </head>
    <body>
        <div class="container details-container">
        <h1 class="mt-4">Course Details</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-danger error">${error}</div>
        </c:if>
        <div class="card">
            <div class="card-header">
                <h5>Course ID: ${course.courseID}</h5>
            </div>
            <div class="card-body">
                <p><strong>Course Name:</strong> ${course.courseName}</p>
                <p><strong>Teacher ID:</strong> ${course.teacherID}</p>
                <p><strong>Start Date:</strong> ${course.startDate}</p>
                <p><strong>End Date:</strong> ${course.endDate}</p>
                <p><strong>Description:</strong> ${course.description != null ? course.description : 'N/A'}</p>
                <p><strong>Status:</strong> ${course.status}</p>
            </div>
            <div class="card-footer">
                <a href="${pageContext.request.contextPath}/student/register" class="btn btn-primary">Register for this Course</a>
                <a href="${pageContext.request.contextPath}/student/courses?action=list" class="btn btn-secondary">Back to List</a>
            </div>
        </div>
    </div>
    </body>
</html>
