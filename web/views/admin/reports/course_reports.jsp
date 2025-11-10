<%-- 
    Document   : course_reports
    Created on : Mar 2, 2025, 6:29:03 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course Report</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
        <h1 class="mt-4">Course Report</h1>
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Course Name</th>
                    <th>Teacher ID</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${courses}">
                    <tr>
                        <td>${course.courseID}</td>
                        <td>${course.courseName}</td>
                        <td>${course.teacherID}</td>
                        <td>${course.status}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="${pageContext.request.contextPath}/admin/reports?type=users" class="btn btn-secondary">View User Report</a>
        <a href="${pageContext.request.contextPath}/admin/reports?type=certificates" class="btn btn-secondary">View Certificate Report</a>
    </div>
    </body>
</html>
