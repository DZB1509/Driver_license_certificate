<%-- 
    Document   : dashboard
    Created on : Feb 27, 2025, 2:30:57 PM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Bảng điều khiển - Giáo viên - Chứng chỉ kỹ năng lái xe an toàn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <style>
        /* Giữ nguyên style cũ */
        body {
            font-family: 'Roboto', Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
            color: #333;
        }
        .table-responsive {
            overflow-x: auto;
        }
        @media (max-width: 768px) {
            .table th, .table td {
                font-size: 14px;
                padding: 6px;
            }
        }
        .personal-info-box {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 0;
            background-color: #fff;
            margin-bottom: 20px;
        }
        .personal-info-box .section-title {
            background-color: #f5f5f5;
            padding: 10px 15px;
            border-bottom: 1px solid #ddd;
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }
        .personal-info-box .content {
            padding: 15px;
        }
        .personal-info-box p {
            margin: 10px 0;
            font-size: 14px;
        }
        .personal-info-box p strong {
            font-weight: bold;
            display: inline-block;
            width: 120px;
        }
        .personal-info-box .btn-primary {
            background-color: #007bff;
            border: none;
            padding: 8px 15px;
            font-size: 14px;
            border-radius: 3px;
        }
        .personal-info-box .btn-primary:hover {
            background-color: #0056b3;
        }
        /* Style cho form tạo kỳ thi */
        .create-exam-form .form-group {
            margin-bottom: 15px;
        }
        .create-exam-form label {
            font-weight: bold;
            margin-right: 10px;
        }
        .create-exam-form select, .create-exam-form input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .create-exam-form .btn-primary {
            width: 100%;
        }
    </style>
</head>
<body>
    <jsp:include page="/views/common/header.jsp" />

    <div class="container-fluid">
        <div class="row">
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Bảng điều khiển - Giáo viên</h1>
                </div>

                <!-- Thông tin cá nhân -->
                <div class="personal-info-box">
                    <div class="section-title">Thông tin cá nhân</div>
                    <div class="content">
                        <div class="card-body">
                            <p><strong>Họ và tên:</strong> ${sessionScope.loggedUser.fullName}</p>
                            <p><strong>Mã Giáo Viên:</strong> ${sessionScope.loggedUser.userID}</p>
                            <a href="${pageContext.request.contextPath}/UpdateUserProfileServlet" class="btn btn-sm btn-primary">Cập nhật thông tin</a>
                        </div>
                    </div>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Thống kê tổng quan -->
                <h3>Thống kê tổng quan</h3>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Khóa học đang giảng dạy</h5>
                                <p class="card-text display-6">${teachingCoursesCount}</p>
                                <a href="${pageContext.request.contextPath}/user/teacher/courses" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Số khóa học</h5>
                                <p class="card-text display-6">${totalCourses}</p>
                                <a href="${pageContext.request.contextPath}/views/user/teacher/course_list.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Tạo khóa học mới</h5>
                                <a href="${pageContext.request.contextPath}/views/user/teacher/create_course.jsp" class="btn btn-sm btn-primary">Tạo ngay</a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Khóa học đang giảng dạy -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Khóa học đang giảng dạy</h5>
                        <a href="${pageContext.request.contextPath}/user/teacher/courses" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty teachingCoursesList}">
                                <p class="text-muted">Bạn chưa giảng dạy khóa học nào.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Mã khóa học</th>
                                                <th>Tên khóa học</th>
                                                <th>Ngày bắt đầu</th>
                                                <th>Trạng thái</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="course" items="${teachingCoursesList}">
                                                <tr>
                                                    <td>${course.courseID}</td>
                                                    <td>${course.courseName}</td>
                                                    <td><fmt:formatDate value="${course.startDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td><span class="badge bg-${course.status eq 'Active' ? 'success' : 'warning'}">${course.status eq 'Active' ? 'Đang diễn ra' : 'Đã kết thúc'}</span></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                        <!-- Form tạo kỳ thi -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="card-title mb-0">Tạo kỳ thi mới</h5>
                            </div>
                            <div class="card-body create-exam-form">
                                <form action="${pageContext.request.contextPath}/CreateExamServlet" method="post">
                                    <div class="form-group">
                                        <label for="courseID">Khóa học:</label>
                                        <select name="courseID" id="courseID" required>
                                            <option value="">-- Chọn khóa học --</option>
                                            <c:forEach var="course" items="${teachingCoursesList}">
                                                <option value="${course.courseID}">${course.courseName} (ID: ${course.courseID})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="examDate">Ngày thi:</label>
                                        <input type="date" name="examDate" id="examDate" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="room">Phòng thi:</label>
                                        <input type="text" name="room" id="room" required>
                                    </div>
                                    <input type="hidden" name="status" value="Pending">
                                    <button type="submit" class="btn btn-primary">Tạo kỳ thi</button>
                                </form>
                            </div>
                        </div>

                <!-- Liên kết nhanh -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Liên kết nhanh</h5>
                    </div>
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/views/user/teacher/course_management.jsp" class="btn btn-primary me-2">Quản lý khóa học</a>
                        <a href="${pageContext.request.contextPath}/views/user/police/exam_supervision.jsp" class="btn btn-primary">Quản lý kỳ thi</a>
                    </div>
                </div>

                <!-- Danh sách học sinh đang học -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Học sinh đang học</h5>
                        <a href="${pageContext.request.contextPath}/views/user/teacher/students_list.jsp" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty currentStudents}">
                                <p class="text-muted">Không có học sinh nào đang học.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Mã học sinh</th>
                                                <th>Họ và tên</th>
                                                <th>Lớp</th>
                                                <th>Khóa học</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="student" items="${currentStudents}">
                                                <tr>
                                                    <td>${student.userID}</td>
                                                    <td>${student.fullName}</td>
                                                    <td>${student.className}</td>
                                                    <td>${student.courseName}</td>
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
                        <a href="${pageContext.request.contextPath}/user/teacher/exams" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
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

    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>