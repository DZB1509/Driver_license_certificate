<%-- 
    Document   : course_registration
    Created on : Mar 2, 2025, 6:18:48 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Course Registrations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f7fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #333;
        }

        .container {
            max-width: 1200px;
            margin-top: 40px;
            background-color: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }

        h1, h2 {
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 20px;
        }

        h1 {
            font-size: 2rem;
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
        }

        h2 {
            font-size: 1.5rem;
            margin-top: 40px;
        }

        .alert {
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .table {
            background-color: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        .table thead {
            background-color: #3498db;
            color: #fff;
        }

        .table th, .table td {
            padding: 15px;
            vertical-align: middle;
            text-align: center;
        }

        .table tbody tr {
            transition: background-color 0.3s ease;
        }

        .table tbody tr:hover {
            background-color: #f1f5f9;
        }

        .table .status-pending {
            color: #e67e22;
            font-weight: 500;
        }

        .table .status-approved {
            color: #27ae60;
            font-weight: 500;
        }

        .table .status-rejected {
            color: #c0392b;
            font-weight: 500;
        }

        .form-label {
            font-weight: 500;
            color: #2c3e50;
        }

        .form-select, .form-control {
            border-radius: 6px;
            border: 1px solid #ced4da;
            padding: 10px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-select:focus, .form-control:focus {
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
            outline: none;
        }

        .btn-primary {
            background-color: #3498db;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            font-weight: 500;
            transition: background-color 0.3s ease, transform 0.1s ease;
        }

        .btn-primary:hover {
            background-color: #2980b9;
            transform: translateY(-2px);
        }

        .btn-primary:active {
            transform: translateY(0);
        }

        .mb-3 {
            margin-bottom: 1.5rem !important;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
                margin-top: 20px;
            }

            h1 {
                font-size: 1.5rem;
            }

            h2 {
                font-size: 1.2rem;
            }

            .table th, .table td {
                font-size: 0.9rem;
                padding: 10px;
            }

            .btn-primary {
                padding: 8px 16px;
                font-size: 0.9rem;
            }
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Your Course Registrations</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Registration ID</th>
                    <th>Course ID</th>
                    <th>Status</th>
                    <th>Comments</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="reg" items="${registrations}">
                    <tr>
                        <td>${reg.registrationID}</td>
                        <td>${reg.courseID}</td>
                        <td class="status-${reg.status.toLowerCase()}">${reg.status}</td>
                        <td>${reg.comments}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h2>Register for a Course</h2>
        <form action="register" method="post">
            <input type="hidden" name="action" value="register">
            <div class="mb-3">
                <label for="courseId" class="form-label">Select Course</label>
                <select name="courseId" id="courseId" class="form-select" required>
                    <option value="">-- Select a Course --</option>
                    <c:forEach var="course" items="${courses}">
                        <option value="${course.courseID}">${course.courseName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label for="comments" class="form-label">Comments (Optional)</label>
                <textarea name="comments" id="comments" class="form-control" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Register</button>
        </form>
    </div>

    <!-- Bootstrap JS (optional, for form interactions) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>