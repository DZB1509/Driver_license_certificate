<%-- 
    Document   : reports
    Created on : Mar 2, 2025, 6:25:44 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Báo cáo - Chứng chỉ kỹ năng lái xe an toàn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <style>
        .table-responsive {
            overflow-x: auto;
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
            <jsp:include page="/WEB-INF/tags/sidebar.tag" />

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Báo cáo</h1>
                </div>

                <!-- Thông báo lỗi -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Form chọn loại báo cáo -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Chọn loại báo cáo</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/user/teacher/reports" method="get" class="row g-3">
                            <div class="col-md-4">
                                <select class="form-select" name="reportType" onchange="this.form.submit()">
                                    <option value="" ${empty param.reportType ? 'selected' : ''}>-- Chọn loại báo cáo --</option>
                                    <option value="summary" ${param.reportType == 'summary' ? 'selected' : ''}>Tóm tắt</option>
                                    <option value="exams" ${param.reportType == 'exams' ? 'selected' : ''}>Kỳ thi</option>
                                    <option value="students" ${param.reportType == 'students' ? 'selected' : ''}>Học sinh</option>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Nội dung báo cáo -->
                <c:if test="${not empty reportType}">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">
                                <c:choose>
                                    <c:when test="${reportType == 'summary'}">Báo cáo tóm tắt</c:when>
                                    <c:when test="${reportType == 'exams'}">Báo cáo kỳ thi</c:when>
                                    <c:when test="${reportType == 'students'}">Báo cáo học sinh</c:when>
                                </c:choose>
                            </h5>
                            <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                <i class="bi bi-printer"></i> In báo cáo
                            </button>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${reportType == 'summary'}">
                                    <div class="mb-3">
                                        <p><strong>Tổng số chứng chỉ:</strong> ${totalCertificates}</p>
                                        <p><strong>Chứng chỉ trong tháng này:</strong> ${certificatesThisMonth}</p>
                                        <p><strong>Chứng chỉ tháng trước:</strong> ${certificatesLastMonth}</p>
                                        <p><strong>Tỷ lệ đạt:</strong> <fmt:formatNumber value="${passRate}" maxFractionDigits="2"/>%</p>
                                        <c:if test="${not empty examStatusCount}">
                                            <h6>Trạng thái kỳ thi:</h6>
                                            <ul>
                                                <c:forEach var="entry" items="${examStatusCount}">
                                                    <li>${entry.key}: ${entry.value}</li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:when test="${reportType == 'exams'}">
                                    <c:if test="${not empty exams}">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Mã kỳ thi</th>
                                                        <th>Ngày thi</th>
                                                        <th>Số lượng tham gia</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="exam" items="${exams}">
                                                        <tr>
                                                            <td>${exam.examID}</td>
                                                            <td><fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${examParticipants[exam.examID]}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <p class="mt-3"><strong>Tổng số kỳ thi:</strong> ${totalExams}</p>
                                    </c:if>
                                    <c:if test="${empty exams}">
                                        <p class="text-muted">Không có kỳ thi nào để hiển thị.</p>
                                    </c:if>
                                </c:when>
                                <c:when test="${reportType == 'students'}">
                                    <c:if test="${not empty students}">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Mã học sinh</th>
                                                        <th>Họ và tên</th>
                                                        <th>Có chứng chỉ</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="student" items="${students}">
                                                        <tr>
                                                            <td>${student.userID}</td>
                                                            <td>${student.fullName}</td>
                                                            <td><span class="badge bg-${studentCertStatus[student.userID] ? 'success' : 'danger'}">${studentCertStatus[student.userID] ? 'Có' : 'Không'}</span></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <p class="mt-3"><strong>Tổng số học sinh:</strong> ${totalStudents}</p>
                                    </c:if>
                                    <c:if test="${empty students}">
                                        <p class="text-muted">Không có học sinh nào để hiển thị.</p>
                                    </c:if>
                                </c:when>
                            </c:choose>

                            <!-- Phân trang -->
                            <c:if test="${totalPages > 1}">
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/user/teacher/reports?reportType=${reportType}&page=${currentPage - 1}">Trước</a>
                                        </li>
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/user/teacher/reports?reportType=${reportType}&page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/user/teacher/reports?reportType=${reportType}&page=${currentPage + 1}">Sau</a>
                                        </li>
                                    </ul>
                                </nav>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </main>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="/WEB-INF/tags/footer.tag" />

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>