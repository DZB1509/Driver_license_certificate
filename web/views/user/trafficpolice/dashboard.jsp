<%-- 
    Document   : dashboard
    Created on : Feb 27, 2025, 2:31:33 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bảng điều khiển - Cảnh sát - Chứng chỉ kỹ năng lái xe an toàn</title>
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
                        <h1 class="h2">Bảng điều khiển - Cảnh sát</h1>
                    </div>
                    <div class="content">
                        <div class="card-body">
                            <p><strong>Họ và tên:</strong> ${sessionScope.loggedUser.fullName}</p>
                            <p><strong>Mã Cảnh sát:</strong> ${sessionScope.loggedUser.userID}</p>
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
            <div class="row mb-4">
                <div class="col-md-4 mb-3">
                    <div class="card card-stat border-0 shadow-sm">
                        <div class="card-body text-center">
                            <h5 class="card-title">Chứng chỉ chờ phê duyệt</h5>
                            <p class="card-text display-6">${pendingCertificates}</p>
                            <a href="${pageContext.request.contextPath}/views/user/trafficpolice/certificate_approval.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-3">
                    <div class="card card-stat border-0 shadow-sm">
                        <div class="card-body text-center">
                            <h5 class="card-title">Kỳ thi sắp diễn ra</h5>
                            <p class="card-text display-6">${upcomingExams}</p>
                            <a href="${pageContext.request.contextPath}/views/user/trafficpolice/exam_supervision.jsp" class="btn btn-sm btn-primary">Xem chi tiết</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-3">
                    <div class="card card-stat border-0 shadow-sm">
                        <div class="card-body text-center">
                            <h5 class="card-title">Tổng số vi phạm</h5>
                            <p class="card-text display-6">${totalViolations}</p>
                            <a href="${pageContext.request.contextPath}/trafficpolice/exam/supervision" class="btn btn-sm btn-primary">Xem chi tiết</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Chứng chỉ chờ phê duyệt gần đây -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">Chứng chỉ chờ phê duyệt gần đây</h5>
                    <a href="${pageContext.request.contextPath}/views/user/trafficpolice/certificate_approval.jsp" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty recentCertificates}">
                            <p class="text-muted">Không có chứng chỉ nào chờ phê duyệt.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã chứng chỉ</th>
                                            <th>Học sinh</th>
                                            <th>Ngày cấp</th>
                                            <th>Kết quả thi</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="certificate" items="${recentCertificates}">
                                            <tr>
                                                <td>${certificate.certificateCode}</td>
                                                <td>${certificate.studentName}</td>
                                                <td><fmt:formatDate value="${certificate.issuedDate}" pattern="dd/MM/yyyy"/></td>
                                                <td>
                                                    <span class="badge bg-${certificate.result.passStatus ? 'success' : 'danger'}">
                                                        ${certificate.result.score} - ${certificate.result.passStatus ? 'Đạt' : 'Không đạt'}
                                                    </span>
                                                </td>
                                                <td>
                                                    <form action="${pageContext.request.contextPath}/CertificateApprovalServlet" method="post">
                                                        <input type="hidden" name="action" value="approve">
                                                        <input type="hidden" name="certificateId" value="${certificate.certificateID}">
                                                        <input type="hidden" name="fromDashboard" value="true">
                                                        <button type="submit" class="btn btn-sm btn-success">Phê duyệt ngay</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Kỳ thi sắp diễn ra -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">Kỳ thi sắp diễn ra</h5>
                    <a href="${pageContext.request.contextPath}/views/user/trafficpolice/exam_supervision.jsp" class="btn btn-sm btn-outline-primary">Xem tất cả</a>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty upcomingExamsList}">
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
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="exam" items="${upcomingExamsList}">
                                            <tr>
                                                <td>${exam.examID}</td>
                                                <td>${exam.courseName}</td>
                                                <td><fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/></td>
                                                <td>${exam.room}</td>
                                                <td>
                                                    <form action="${pageContext.request.contextPath}/ExamSupervisionServlet" method="post">
                                                        <input type="hidden" name="action" value="supervise">
                                                        <input type="hidden" name="examId" value="${exam.examID}">
                                                        <input type="hidden" name="fromDashboard" value="true">
                                                        <button type="submit" class="btn btn-sm btn-info">Giám sát</button>
                                                    </form>
                                                </td>
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

<!-- Footer -->

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>