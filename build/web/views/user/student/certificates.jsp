<%-- 
    Document   : certificates
    Created on : Mar 2, 2025, 6:19:55 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chứng chỉ - Kỹ năng lái xe an toàn</title>
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
                padding: 8px;
            }
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="/WEB-INF/tags/sidebar.tag" />

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Chứng chỉ của bạn</h1>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Tìm kiếm -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Tìm kiếm chứng chỉ</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/student/certificates" method="get" class="row g-3">
                            <div class="col-md-4">
                                <label for="certificateCode" class="form-label">Mã chứng chỉ</label>
                                <input type="text" class="form-control" id="certificateCode" name="certificateCode" value="${param.certificateCode}">
                            </div>
                            <div class="col-md-3">
                                <label for="startDate" class="form-label">Từ ngày cấp</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}">
                            </div>
                            <div class="col-md-3">
                                <label for="endDate" class="form-label">Đến ngày cấp</label>
                                <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}">
                            </div>
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100">Tìm kiếm</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Danh sách chứng chỉ -->
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Danh sách chứng chỉ</h5>
                        <c:if test="${not empty certificates}">
                            <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                <i class="bi bi-printer"></i> In danh sách
                            </button>
                        </c:if>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty certificates}">
                                <p class="text-muted">Bạn chưa có chứng chỉ nào.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Mã chứng chỉ</th>
                                                <th>Ngày cấp</th>
                                                <th>Ngày hết hạn</th>
                                                <th>Trạng thái</th>
                                                <th>Tùy chọn</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${certificates}" var="certificate" varStatus="loop">
                                                <tr>
                                                    <td>${loop.count}</td>
                                                    <td>${certificate.certificateCode}</td>
                                                    <td><fmt:formatDate value="${certificate.issuedDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td><fmt:formatDate value="${certificate.expirationDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${certificate.expirationDate.after(now)}">
                                                                <span class="badge bg-success">Còn hiệu lực</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger">Hết hiệu lực</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/student/certificates/${certificate.certificateID}" 
                                                           class="btn btn-sm btn-info" target="_blank">
                                                            Xem chi tiết
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Phân trang -->
                                <c:if test="${totalPages > 1}">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/student/certificates?page=${currentPage - 1}&certificateCode=${param.certificateCode}&startDate=${param.startDate}&endDate=${param.endDate}">Trước</a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/student/certificates?page=${i}&certificateCode=${param.certificateCode}&startDate=${param.startDate}&endDate=${param.endDate}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/student/certificates?page=${currentPage + 1}&certificateCode=${param.certificateCode}&startDate=${param.startDate}&endDate=${param.endDate}">Sau</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="/WEB-INF/tags/footer.tag" />

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
    <script>
        // Đặt biến now cho so sánh ngày
        const now = new Date();
        document.querySelector('body').setAttribute('data-now', now.toISOString());
    </script>
</body>
</html>