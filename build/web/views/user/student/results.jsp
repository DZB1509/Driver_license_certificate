<%-- 
    Document   : results
    Created on : Mar 2, 2025, 6:19:45 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Kết quả thi - Chứng chỉ kỹ năng lái xe an toàn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <style>
        .table-responsive {
            overflow-x: auto;
        }
        .modal-lg {
            max-width: 90%;
        }
        @media (max-width: 768px) {
            .table th, .table td {
                font-size: 14px;
                padding: 8px;
            }
            .modal-content {
                width: 100%;
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
                    <h1 class="h2">Kết quả thi</h1>
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
                        <h5 class="card-title mb-0">Tìm kiếm kết quả thi</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/student/results" method="get" class="row g-3">
                            <div class="col-md-4">
                                <label for="courseId" class="form-label">Khóa học</label>
                                <select class="form-select" id="courseId" name="courseId">
                                    <option value="">Tất cả khóa học</option>
                                    <c:forEach items="${courses}" var="course">
                                        <option value="${course.courseID}" ${param.courseId eq course.courseID ? 'selected' : ''}>${course.courseName}</option>
                                    </c:forEach>
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
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100">Tìm kiếm</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Danh sách kết quả thi -->
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Danh sách kết quả thi</h5>
                        <c:if test="${not empty results}">
                            <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                <i class="bi bi-printer"></i> In kết quả
                            </button>
                        </c:if>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty results}">
                                <p class="text-muted">Không có kết quả thi nào.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Khóa học</th>
                                                <th>Ngày thi</th>
                                                <th>Phòng thi</th>
                                                <th>Điểm số</th>
                                                <th>Kết quả</th>
                                                <th>Chi tiết</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${results}" var="result" varStatus="status">
                                                <tr>
                                                    <td>${status.count}</td>
                                                    <td>${result.courseName}</td>
                                                    <td><fmt:formatDate value="${result.examDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${result.examRoom}</td>
                                                    <td class="text-center">${result.score}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${result.passStatus}">
                                                                <span class="badge bg-success">Đạt</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger">Không đạt</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <button type="button" class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#resultDetailModal"
                                                                data-result-id="${result.resultID}">
                                                            Chi tiết
                                                        </button>
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
                                                <a class="page-link" href="${pageContext.request.contextPath}/student/results?page=${currentPage - 1}&courseId=${param.courseId}&startDate=${param.startDate}&endDate=${param.endDate}">Trước</a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/student/results?page=${i}&courseId=${param.courseId}&startDate=${param.startDate}&endDate=${param.endDate}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/student/results?page=${currentPage + 1}&courseId=${param.courseId}&startDate=${param.startDate}&endDate=${param.endDate}">Sau</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </c:if>

                                <!-- Thống kê -->
                                <div class="mt-3">
                                    <p>Đạt: ${passedCount} | Không đạt: ${failedCount} | Điểm trung bình: ${averageScore}</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Modal chi tiết kết quả -->
    <div class="modal fade" id="resultDetailModal" tabindex="-1" aria-labelledby="resultDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="resultDetailModalLabel">Chi tiết kết quả thi</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="spinner-border text-primary" role="status" id="resultDetailSpinner">
                        <span class="visually-hidden">Đang tải...</span>
                    </div>
                    <div id="resultDetailContent" style="display: none;">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <p><strong>Khóa học:</strong> <span id="detailCourseName"></span></p>
                                <p><strong>Ngày thi:</strong> <span id="detailExamDate"></span></p>
                                <p><strong>Phòng thi:</strong> <span id="detailExamRoom"></span></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Điểm số:</strong> <span id="detailScore"></span></p>
                                <p><strong>Kết quả:</strong> <span id="detailPassStatus"></span></p>
                                <p><strong>Người chấm điểm:</strong> <span id="detailTeacher"></span></p>
                            </div>
                        </div>
                        <hr>
                        <div class="mb-3">
                            <h6>Nhận xét:</h6>
                            <p id="detailComments"></p>
                        </div>
                        <div id="certificateSection" style="display: none;">
                            <hr>
                            <div class="alert alert-success">
                                <p><strong>Thông tin chứng chỉ:</strong></p>
                                <p>Mã chứng chỉ: <span id="detailCertificateCode"></span></p>
                                <p>Ngày cấp: <span id="detailIssuedDate"></span></p>
                                <p>Ngày hết hạn: <span id="detailExpirationDate"></span></p>
                                <a href="#" id="viewCertificateLink" class="btn btn-primary" target="_blank">Xem chứng chỉ</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="/WEB-INF/tags/footer.tag" />

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#resultDetailModal').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var resultId = button.data('result-id');

                $('#resultDetailSpinner').show();
                $('#resultDetailContent').hide();
                $('#certificateSection').hide();

                // Gửi AJAX để lấy chi tiết kết quả
                $.ajax({
                    url: '${pageContext.request.contextPath}/api/results/' + resultId,
                    method: 'GET',
                    dataType: 'json',
                    success: function(data) {
                        if (data.error) {
                            alert(data.error);
                            $('#resultDetailModal').modal('hide');
                            return;
                        }
                        $('#detailCourseName').text(data.courseName || 'N/A');
                        $('#detailExamDate').text(data.examDate ? new Date(data.examDate).toLocaleDateString('vi-VN') : 'N/A');
                        $('#detailExamRoom').text(data.examRoom || 'N/A');
                        $('#detailScore').text(data.score !== undefined ? data.score : 'N/A');
                        $('#detailPassStatus').html(data.passStatus ?
                            '<span class="badge bg-success">Đạt</span>' :
                            '<span class="badge bg-danger">Không đạt</span>');
                        $('#detailTeacher').text(data.teacherName || 'N/A');
                        $('#detailComments').text(data.comments || 'Không có nhận xét');

                        // Hiển thị thông tin chứng chỉ nếu có
                        if (data.certificateInfo && data.certificateInfo.certificateID) {
                            $('#detailCertificateCode').text(data.certificateInfo.certificateCode || 'N/A');
                            $('#detailIssuedDate').text(data.certificateInfo.issuedDate ? new Date(data.certificateInfo.issuedDate).toLocaleDateString('vi-VN') : 'N/A');
                            $('#detailExpirationDate').text(data.certificateInfo.expirationDate ? new Date(data.certificateInfo.expirationDate).toLocaleDateString('vi-VN') : 'N/A');
                            $('#viewCertificateLink').attr('href', '${pageContext.request.contextPath}/student/certificates/' + data.certificateInfo.certificateID);
                            $('#certificateSection').show();
                        } else {
                            $('#certificateSection').hide();
                        }

                        $('#resultDetailSpinner').hide();
                        $('#resultDetailContent').show();
                    },
                    error: function(xhr, status, error) {
                        alert('Có lỗi xảy ra khi tải thông tin kết quả thi: ' + error);
                        $('#resultDetailModal').modal('hide');
                    }
                });
            });
        });
    </script>
</body>
</html>