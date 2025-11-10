<%-- 
    Document   : course_registra
    Created on : 23 Mar 2025, 21:07:43
    Author     : exorc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
       <div class="card mb-4">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="card-title mb-0">Khóa học đã đăng ký</h5>
        <a href="${pageContext.request.contextPath}/user/student/courses" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${empty registeredCoursesList}">
                <p class="text-muted">Bạn chưa đăng ký khóa học nào.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Mã khóa học</th>
                                <th>Tên khóa học</th>
                                <th>Ngày đăng ký</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="course" items="${registeredCoursesList}">
                                <tr>
                                    <td>${course.courseID}</td>
                                    <td>${course.courseName}</td>
                                    <td><fmt:formatDate value="${course.registrationDate}" pattern="dd/MM/yyyy"/></td>
                                    <td><span class="badge bg-${course.status eq 'completed' ? 'success' : 'warning'}">${course.status eq 'completed' ? 'Hoàn thành' : 'Đang học'}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
    </body>
</html>
