<%-- 
    Document   : course_list
    Created on : Mar 2, 2025, 6:18:21 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course List - Student</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-container { margin-top: 20px; }
        .error { color: red; }
    </style>
    </head>
    <body>
        <div class="container table-container">
        <h1 class="mt-4">Available Courses</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-danger error">${error}</div>
        </c:if>
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
                            <a href="${pageContext.request.contextPath}/student/courses?action=view&courseId=${course.courseID}" class="btn btn-info btn-sm">View</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="${pageContext.request.contextPath}/student/register" class="btn btn-primary">Register for a Course</a>
    </div>
    </body>
</html>
