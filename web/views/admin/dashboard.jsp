<%-- 
    Document   : dashboard
    Created on : Feb 27, 2025, 2:32:58 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Bảng điều khiển - Quản trị viên - Chứng chỉ kỹ năng lái xe an toàn</title>
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
                    <h1 class="h2">Bảng điều khiển - Quản trị viên</h1>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Thống kê tổng quan -->
                <div class="row mb-4">
                    <div class="col-md-3 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Tổng số người dùng</h5>
                                <p class="card-text display-6">${totalUsers}</p>
                                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Số khóa học</h5>
                                <p class="card-text display-6">${totalCourses}</p>
                                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Số kỳ thi</h5>
                                <p class="card-text display-6">${totalExams}</p>
                                <a href="${pageContext.request.contextPath}/admin/exams" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="card card-stat border-0 shadow-sm">
                            <div class="card-body text-center">
                                <h5 class="card-title">Số chứng chỉ</h5>
                                <p class="card-text display-6">${totalCertificates}</p>
                                <a href="${pageContext.request.contextPath}/views/admin/certificate_reports.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Danh sách người dùng gần đây -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Người dùng gần đây</h5>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty recentUsers}">
                                <p class="text-muted">Không có người dùng nào gần đây.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Mã người dùng</th>
                                                <th>Họ và tên</th>
                                                <th>Vai trò</th>
                                                <th>Ngày đăng ký</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="user" items="${recentUsers}">
                                                <tr>
                                                    <td>${user.userID}</td>
                                                    <td>${user.fullName}</td>
                                                    <td>${user.role}</td>
                                                    <td><fmt:formatDate value="${user.registrationDate}" pattern="dd/MM/yyyy"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Hoạt động hệ thống gần đây -->
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Hoạt động hệ thống gần đây</h5>
                        <a href="${pageContext.request.contextPath}/admin/logs" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty systemLogs}">
                                <p class="text-muted">Không có hoạt động nào gần đây.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Thời gian</th>
                                                <th>Người thực hiện</th>
                                                <th>Hành động</th>
                                                <th>Chi tiết</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="log" items="${systemLogs}">
                                                <tr>
                                                    <td><fmt:formatDate value="${log.timestamp}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                    <td>${log.userName}</td>
                                                    <td>${log.action}</td>
                                                    <td>${log.detail}</td>
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