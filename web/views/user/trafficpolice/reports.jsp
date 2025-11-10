<%-- 
    Document   : reports
    Created on : Mar 2, 2025, 6:27:09 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Báo cáo - Cảnh sát - Chứng chỉ kỹ năng lái xe an toàn</title>
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
                    <h1 class="h2">Báo cáo - Cảnh sát</h1>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Form chọn loại báo cáo -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Chọn loại báo cáo</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/user/police/reports" method="get" class="row g-3">
                            <div class="col-md-3">
                                <select class="form-select" name="reportType" onchange="this.form.submit()">
                                    <option value="" ${empty param.reportType ? 'selected' : ''}>-- Chọn loại báo cáo --</option>
                                    <option value="certificates" ${param.reportType == 'certificates' ? 'selected' : ''}>Chứng chỉ</option>
                                    <option value="exams" ${param.reportType == 'exams' ? 'selected' : ''}>Kỳ thi</option>
                                    <option value="violations" ${param.reportType == 'violations' ? 'selected' : ''}>Vi phạm</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label for="startDate" class="form-label">Từ ngày</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}">
                            </div>
                            <div class="col-md-3">
                                <label for="endDate" class="form-label">Đến ngày</label>
                                <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}">
                            </div>
                            <div class="col-md-3 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100">Lọc</button>
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
                                    <c:when test="${reportType == 'certificates'}">Báo cáo chứng chỉ</c:when>
                                    <c:when test="${reportType == 'exams'}">Báo cáo kỳ thi</c:when>
                                    <c:when test="${reportType == 'violations'}">Báo cáo vi phạm</c:when>
                                </c:choose>
                            </h5>
                            <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                <i class="bi bi-printer"></i> In báo cáo
                            </button>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${reportType == 'certificates'}">
                                    <div class="mb-3">
                                        <p><strong>Tổng số chứng chỉ:</strong> ${totalCertificates}</p>
                                        <p><strong>Chứng chỉ chờ phê duyệt:</strong> ${pendingCertificates}</p>
                                        <p><strong>Chứng chỉ đã phê duyệt:</strong> ${approvedCertificates}</p>
                                        <p><strong>Chứng chỉ đã từ chối:</strong> ${rejectedCertificates}</p>
                                    </div>
                                </c:when>
                                <c:when test="${reportType == 'exams'}">
                                    <c:if test="${not empty exams}">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Mã kỳ thi</th>
                                                        <th>Khóa học</th>
                                                        <th>Ngày thi</th>
                                                        <th>Số lượng tham gia</th>
                                                        <th>Số vi phạm</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="exam" items="${exams}">
                                                        <tr>
                                                            <td>${exam.examID}</td>
                                                            <td>${exam.courseName}</td>
                                                            <td><fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${examParticipants[exam.examID]}</td>
                                                            <td>${examViolations[exam.examID]}</td>
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
                                <c:when test="${reportType == 'violations'}">
                                    <c:if test="${not empty violations}">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Mã vi phạm</th>
                                                        <th>Học sinh</th>
                                                        <th>Kỳ thi</th>
                                                        <th>Ngày vi phạm</th>
                                                        <th>Chi tiết</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="violation" items="${violations}">
                                                        <tr>
                                                            <td>${violation.violationID}</td>
                                                            <td>${violation.studentName}</td>
                                                            <td>${violation.examID}</td>
                                                            <td><fmt:formatDate value="${violation.date}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${violation.detail}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <p class="mt-3"><strong>Tổng số vi phạm:</strong> ${totalViolations}</p>
                                    </c:if>
                                    <c:if test="${empty violations}">
                                        <p class="text-muted">Không có vi phạm nào để hiển thị.</p>
                                    </c:if>
                                </c:when>
                            </c:choose>

                            <!-- Phân trang -->
                            <c:if test="${totalPages > 1}">
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/user/police/reports?reportType=${reportType}&page=${currentPage - 1}&startDate=${param.startDate}&endDate=${param.endDate}">Trước</a>
                                        </li>
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/user/police/reports?reportType=${reportType}&page=${i}&startDate=${param.startDate}&endDate=${param.endDate}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/user/police/reports?reportType=${reportType}&page=${currentPage + 1}&startDate=${param.startDate}&endDate=${param.endDate}">Sau</a>
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