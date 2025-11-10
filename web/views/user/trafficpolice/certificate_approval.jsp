<%-- 
    Document   : certificate_approval
    Created on : Mar 2, 2025, 6:26:45 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Phê duyệt Chứng chỉ - Chứng chỉ kỹ năng lái xe an toàn</title>
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

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Phê duyệt Chứng chỉ</h1>
                </div>

                <div class="card mb-4">
                    <div class="card-header">
                        <div class="row">
                            <div class="col-md-6">
                                <h5 class="card-title mb-0">Danh sách chờ phê duyệt</h5>
                            </div>
                            <div class="col-md-6">
                                <form class="d-flex" action="CertificateApprovalServlet" method="get">
                                    <select name="status" class="form-select me-2">
                                        <option value="all" ${param.status == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
                                        <option value="pending" ${param.status == 'pending' || param.status == null ? 'selected' : ''}>Chờ phê duyệt</option>
                                        <option value="approved" ${param.status == 'approved' ? 'selected' : ''}>Đã phê duyệt</option>
                                        <option value="rejected" ${param.status == 'rejected' ? 'selected' : ''}>Đã từ chối</option>
                                    </select>
                                    <input type="text" name="search" class="form-control me-2" placeholder="Tìm kiếm học sinh..." value="${param.search}">
                                    <button type="submit" class="btn btn-primary">Tìm</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${successMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <!-- Thống kê -->
                        <div class="mb-3">
                            <p>Tổng chứng chỉ: ${totalCertificates} | Chờ phê duyệt: ${pendingCount} | Đã phê duyệt: ${approvedCount} | Đã từ chối: ${rejectedCount}</p>
                        </div>

                        <c:choose>
                            <c:when test="${empty certificateList}">
                                <div class="alert alert-info">
                                    Không có chứng chỉ nào ${param.status == 'pending' ? 'chờ phê duyệt' : param.status == 'approved' ? 'đã phê duyệt' : param.status == 'rejected' ? 'đã từ chối' : ''} vào lúc này.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead class="table-dark">
                                            <tr>
                                                <th scope="col">Mã chứng chỉ</th>
                                                <th scope="col">Học sinh</th>
                                                <th scope="col">Ngày cấp</th>
                                                <th scope="col">Ngày hết hạn</th>
                                                <th scope="col">Kết quả thi</th>
                                                <th scope="col">Trạng thái</th>
                                                <th scope="col">Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="certificate" items="${certificateList}" varStatus="loop">
                                                <tr>
                                                    <td>${certificate.certificateCode}</td>
                                                    <td>
                                                        <a href="#" data-bs-toggle="modal" data-bs-target="#studentModal${certificate.userID}">
                                                            ${certificate.studentName}
                                                        </a>
                                                    </td>
                                                    <td><fmt:formatDate value="${certificate.issuedDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td><fmt:formatDate value="${certificate.expirationDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td>
                                                        <span class="badge bg-${certificate.result.passStatus ? 'success' : 'danger'}">
                                                            ${certificate.result.score} - ${certificate.result.passStatus ? 'Đạt' : 'Không đạt'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${certificate.status == 'pending'}">
                                                                <span class="badge bg-warning text-dark">Chờ phê duyệt</span>
                                                            </c:when>
                                                            <c:when test="${certificate.status == 'approved'}">
                                                                <span class="badge bg-success">Đã phê duyệt</span>
                                                            </c:when>
                                                            <c:when test="${certificate.status == 'rejected'}">
                                                                <span class="badge bg-danger">Đã từ chối</span>
                                                            </c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${certificate.status == 'pending'}">
                                                            <div class="btn-group" role="group">
                                                                <form action="${pageContext.request.contextPath}/police/certificate-approval" method="post" class="me-2">
                                                                    <input type="hidden" name="action" value="approve">
                                                                    <input type="hidden" name="certificateId" value="${certificate.certificateID}">
                                                                    <button type="submit" class="btn btn-sm btn-success approve-btn">Phê duyệt</button>
                                                                </form>
                                                                <button type="button" class="btn btn-sm btn-danger reject-btn" 
                                                                        data-bs-toggle="modal" 
                                                                        data-bs-target="#rejectModal${certificate.certificateID}">
                                                                    Từ chối
                                                                </button>
                                                            </div>
                                                        </c:if>
                                                    </td>
                                                </tr>

                                                <!-- Modal từ chối -->
                                                <div class="modal fade" id="rejectModal${certificate.certificateID}" tabindex="-1" aria-labelledby="rejectModalLabel${certificate.certificateID}" aria-hidden="true">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="rejectModalLabel${certificate.certificateID}">Từ chối chứng chỉ</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <form action="${pageContext.request.contextPath}/police/certificate-approval" method="post">
                                                                    <input type="hidden" name="action" value="reject">
                                                                    <input type="hidden" name="certificateId" value="${certificate.certificateID}">
                                                                    <div class="mb-3">
                                                                        <label for="reason" class="form-label">Lý do từ chối:</label>
                                                                        <textarea class="form-control" id="reason" name="reason" rows="3" required></textarea>
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                                        <button type="submit" class="btn btn-danger">Xác nhận từ chối</button>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <!-- Modal thông tin học sinh -->
                                                <div class="modal fade" id="studentModal${certificate.userID}" tabindex="-1" aria-labelledby="studentModalLabel${certificate.userID}" aria-hidden="true">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="studentModalLabel${certificate.userID}">Thông tin học sinh</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <p><strong>Họ và tên:</strong> ${certificate.studentName}</p>
                                                                <p><strong>Mã học sinh:</strong> ${certificate.userID}</p>
                                                                <p><strong>Lớp:</strong> ${certificate.studentClass}</p>
                                                                <p><strong>Trường:</strong> ${certificate.studentSchool}</p>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Phân trang -->
                                <c:if test="${totalPages > 1}">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/police/certificate-approval?page=${currentPage - 1}&status=${param.status}&search=${param.search}">Trước</a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/police/certificate-approval?page=${i}&status=${param.status}&search=${param.search}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/police/certificate-approval?page=${currentPage + 1}&status=${param.status}&search=${param.search}">Sau</a>
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
        $(document).ready(function() {
            // Xác nhận trước khi phê duyệt
            $('.approve-btn').on('click', function(e) {
                if (!confirm('Bạn có chắc chắn muốn phê duyệt chứng chỉ này không?')) {
                    e.preventDefault();
                }
            });

            // Xác nhận trong modal từ chối
            $('.reject-btn').on('click', function() {
                var certificateId = $(this).closest('form').find('input[name="certificateId"]').val();
                $('#rejectModal' + certificateId).modal('show');
            });
        });
    </script>
</body>
</html>