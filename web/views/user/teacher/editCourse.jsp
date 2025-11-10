<%-- 
    Document   : editCourse
    Created on : Mar 17, 2025, 5:55:45 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course Management - ${course.courseID == 0 ? 'Add' : 'Edit'} Course</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-container { max-width: 600px; margin: 20px auto; }
        .error { color: red; }
    </style>
    </head>
    <body>
        <div class="container form-container">
        <h1 class="mt-4">${course.courseID == 0 ? 'Add New' : 'Edit'} Course</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-danger error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/Teacher/courses" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="action" value="${course.courseID == 0 ? 'create' : 'update'}">
            <input type="hidden" name="courseId" value="${course.courseID}">
            
            <div class="mb-3">
                <label for="courseName" class="form-label">Course Name</label>
                <input type="text" class="form-control" id="courseName" name="courseName" value="${course.courseName}" required>
                <div class="invalid-feedback">Please enter a course name.</div>
            </div>
            
            <div class="mb-3">
                <label for="teacherId" class="form-label">Teacher ID</label>
                <input type="number" class="form-control" id="teacherId" name="teacherId" value="${course.teacherID}" required>
                <div class="invalid-feedback">Please enter a valid teacher ID.</div>
            </div>
            
            <div class="mb-3">
                <label for="startDate" class="form-label">Start Date</label>
                <input type="date" class="form-control" id="startDate" name="startDate" value="${course.startDate}" required>
                <div class="invalid-feedback">Please enter a start date.</div>
            </div>
            
            <div class="mb-3">
                <label for="endDate" class="form-label">End Date</label>
                <input type="date" class="form-control" id="endDate" name="endDate" value="${course.endDate}" required>
                <div class="invalid-feedback">Please enter an end date.</div>
            </div>
            
            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description" rows="3">${course.description}</textarea>
            </div>
            
            <div class="mb-3">
                <label for="status" class="form-label">Status</label>
                <select class="form-select" id="status" name="status" required>
                    <option value="">-- Select Status --</option>
                    <option value="Active" ${course.status == 'Active' ? 'selected' : ''}>Active</option>
                    <option value="Inactive" ${course.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    <option value="Completed" ${course.status == 'Completed' ? 'selected' : ''}>Completed</option>
                    <option value="Cancelled" ${course.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                </select>
                <div class="invalid-feedback">Please select a status.</div>
            </div>
            
            <button type="submit" class="btn btn-primary">Save</button>
            <a href="${pageContext.request.contextPath}/teacher/courses?action=list" class="btn btn-secondary">Cancel</a>
        </form>
    </div>

    <script>
        (function () {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
    </body>
</html>
