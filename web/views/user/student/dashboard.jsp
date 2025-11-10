<%-- 
    Document   : dashboard
    Created on : Feb 27, 2025, 2:30:11 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bảng điều khiển - Học sinh - Chứng chỉ kỹ năng lái xe an toàn</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
        <style>
            .table-responsive {
                overflow-x: auto;
            }
            .card-stat {
                transition: transform 0.2s;
            }
            .card-stat:hover {
                transform: translateY(-5px);
            }
            @media (max-width: 768px) {
                .table th, .table td {
                    font-size: 14px;
                    padding: 6px;
                }
            }
        </style>
    </head>
    <body>
        <%-- Include header --%>
        <jsp:include page="/views/common/header.jsp" />

        <div class="container-fluid">
            <div class="row">
                <%-- Include sidebar --%>


                <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2">Bảng điều khiển - Học sinh</h1>
                    </div>

                    <!-- Thông báo -->
                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-${sessionScope.messageType} alert-dismissible fade show" role="alert">
                            ${sessionScope.message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            <%-- Clear the message from session after displaying --%>
                            <c:remove var="message" scope="session"/>
                            <c:remove var="messageType" scope="session"/>
                        </div>
                    </c:if>

                    <!-- Thông tin cá nhân -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Thông tin cá nhân</h5>
                        </div>
                        <div class="card-body">
                            <p><strong>Họ và tên:</strong> ${sessionScope.loggedUser.fullName}</p>
                            <p><strong>Mã học sinh:</strong> ${sessionScope.loggedUser.userID}</p>
                            <p><strong>Lớp:</strong> ${sessionScope.loggedUser.className}</p>
                            <p><strong>Trường:</strong> ${sessionScope.loggedUser.school}</p>
                            <a href="${pageContext.request.contextPath}/views/user/student/profile.jsp" class="btn btn-sm btn-primary">Cập nhật thông tin</a>
                        </div>
                    </div>

                    <!-- Thống kê tổng quan -->
                    <div class="row mb-4">
                        <div class="col-md-4 mb-3">
                            <div class="card card-stat border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <h5 class="card-title">Khóa học đã đăng ký</h5>
                                    <p class="card-text display-6">${registeredCourses}</p>
                                    <a href="${pageContext.request.contextPath}/views/user/student/registered_course.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                                </div>
                            </div>
                        </div>           
                            <div class="col-md-4 mb-3">
                                <div class="card card-stat border-0 shadow-sm">
                                    <div class="card-body text-center">
                                        <h5 class="card-title">Danh sách khóa học</h5>
                                        <p class="card-text display-6">${registeredCourses}</p>
                                        <a href="${pageContext.request.contextPath}/views/user/student/course_list.jsp" class="btn btn-primary">Xem chi tiết</a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <div class="card card-stat border-0 shadow-sm">
                                    <div class="card-body text-center">
                                        <h5 class="card-title">Kỳ thi đã tham gia</h5>
                                        <p class="card-text display-6">${participatedExams}</p>
                                        <a href="${pageContext.request.contextPath}/user/student/exam_history.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <div class="card card-stat border-0 shadow-sm">
                                    <div class="card-body text-center">
                                        <h5 class="card-title">Trạng thái chứng chỉ</h5>
                                        <p class="card-text display-6">${certificateStatus}</p>
                                        <a href="${pageContext.request.contextPath}/user/student/certificate.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <!-- Khóa học đã đăng ký -->
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

                        <!-- Lịch thi sắp tới -->
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="card-title mb-0">Lịch thi sắp tới</h5>
                                <a href="${pageContext.request.contextPath}/user/student/exams" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${empty upcomingExams}">
                                        <p class="text-muted">Không có kỳ thi nào sắp diễn ra.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Mã kỳ thi</th>
                                                        <th>Khóa học</th>
                                                        <th>Ngày thi</th>
                                                        <th>Phòng thi</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="exam" items="${upcomingExams}">
                                                        <tr>
                                                            <td>${exam.examID}</td>
                                                            <td>${exam.courseName}</td>
                                                            <td><fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${exam.room}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                </main>
            </div>
        </div>

        <!-- Footer -->


        <!-- Scripts -->
        <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
    </body>
</html>