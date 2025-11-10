<%-- 
    Document   : course_management
    Created on : Mar 2, 2025, 6:25:03 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course Management - List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-container { margin-top: 20px; }
        .error { color: red; }
    </style>
    </head>
    <body>
        <div class="container table-container">
        <h1 class="mt-4">Course Management</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-danger error">${error}</div>
        </c:if>
        <a href="${pageContext.request.contextPath}/teacher/courses?action=edit&courseId=0" class="btn btn-primary mb-3">Add New Course</a>
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Course Name</th>
                    <th>Teacher ID</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${courses}">
                    <tr>
                        <td>${course.courseID}</td>
                        <td>${course.courseName}</td>
                        <td>${course.teacherID}</td>
                        <td>${course.startDate}</td>
                        <td>${course.endDate}</td>
                        <td>${course.status}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/teacher/courses?action=view&courseId=${course.courseID}" class="btn btn-info btn-sm">View</a>
                            <a href="${pageContext.request.contextPath}/teacher/courses?action=edit&courseId=${course.courseID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="${pageContext.request.contextPath}/teacher/courses?action=delete&courseId=${course.courseID}" 
                               class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this course?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    </body>
</html>
