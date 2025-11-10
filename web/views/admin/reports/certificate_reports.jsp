<%-- 
    Document   : certificate_reports
    Created on : Mar 2, 2025, 6:30:08 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Certificate Report</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
        <h1 class="mt-4">Certificate Report</h1>
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>Registration ID</th>
                    <th>Student ID</th>
                    <th>Course ID</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="registration" items="${registrations}">
                    <tr>
                        <td>${registration.registrationID}</td>
                        <td>${registration.studentID}</td>
                        <td>${registration.courseID}</td>
                        <td>${registration.status}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="${pageContext.request.contextPath}/admin/reports?type=users" class="btn btn-secondary">View User Report</a>
        <a href="${pageContext.request.contextPath}/admin/reports?type=courses" class="btn btn-secondary">View Course Report</a>
    </div>
    </body>
</html>
