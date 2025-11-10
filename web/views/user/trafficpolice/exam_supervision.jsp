<%-- 
    Document   : exam_supervision
    Created on : Mar 2, 2025, 6:26:13 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Giám sát kỳ thi - Chứng chỉ kỹ năng lái xe an toàn</title>
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
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="/WEB-INF/tags/sidebar.tag" />

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Giám sát kỳ thi</h1>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Lọc kỳ thi -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Lọc kỳ thi</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/police/exam/supervision" method="get" class="row g-3">
                            <div class="col-md-3">
                                <label for="startDate" class="form-label">Từ ngày</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}">
                            </div>
                            <div class="col-md-3">
                                <label for="endDate" class="form-label">Đến ngày</label>
                                <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}">
                            </div>
                            <div class="col-md-3">
                                <label for="status" class="form-label">Trạng thái</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="">Tất cả</option>
                                    <option value="Pending" ${param.status eq 'Pending' ? 'selected' : ''}>Chưa bắt đầu</option>
                                    <option value="In Progress" ${param.status eq 'In Progress' ? 'selected' : ''}>Đang diễn ra</option>
                                    <option value="Completed" ${param.status eq 'Completed' ? 'selected' : ''}>Đã hoàn thành</option>
                                </select>
                            </div>
                            <div class="col-md-3 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100">Lọc</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Danh sách kỳ thi -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Danh sách kỳ thi</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty exams}">
                                <p class="text-muted">Không có kỳ thi nào phù hợp với tiêu chí lọc.</p>
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
                                                <th>Trạng thái</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${exams}" var="exam" varStatus="loop">
                                                <tr>
                                                    <td>${loop.count + (currentPage - 1) * pageSize}</td>
                                                    <td>${exam.courseName}</td>
                                                    <td><fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${exam.room}</td>
                                                    <td>
                                                        <span class="badge bg-${exam.status eq 'Pending' ? 'warning' : exam.status eq 'In Progress' ? 'primary' : 'success'}">
                                                            ${exam.status eq 'Pending' ? 'Chưa bắt đầu' : exam.status eq 'In Progress' ? 'Đang diễn ra' : 'Đã hoàn thành'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/police/exam/supervision?examId=${exam.examID}" class="btn btn-sm btn-info">
                                                            Giám sát
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
                                                <a class="page-link" href="${pageContext.request.contextPath}/police/exam/supervision?page=${currentPage - 1}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">Trước</a>
                                            </li>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/police/exam/supervision?page=${i}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                <a class="page-link" href="${pageContext.request.contextPath}/police/exam/supervision?page=${currentPage + 1}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">Sau</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Giám sát kỳ thi chi tiết -->
                <c:if test="${not empty selectedExam}">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">
                                Giám sát kỳ thi - ${selectedExam.courseName} 
                                (<fmt:formatDate value="${selectedExam.date}" pattern="dd/MM/yyyy"/>, Phòng: ${selectedExam.room})
                            </h5>
                            <div>
                                <c:if test="${not empty students}">
                                    <button class="btn btn-sm btn-outline-secondary me-2" onclick="window.print()">
                                        <i class="bi bi-printer"></i> In danh sách
                                    </button>
                                </c:if>
                                <c:if test="${selectedExam.status eq 'Pending'}">
                                    <form action="${pageContext.request.contextPath}/police/exam/update-status" method="post" style="display:inline;">
                                        <input type="hidden" name="examId" value="${selectedExam.examID}">
                                        <input type="hidden" name="status" value="In Progress">
                                        <button type="submit" class="btn btn-sm btn-primary" onclick="return confirm('Bạn có chắc chắn muốn bắt đầu kỳ thi này không?')">Bắt đầu kỳ thi</button>
                                    </form>
                                </c:if>
                                <c:if test="${selectedExam.status eq 'In Progress'}">
                                    <form action="${pageContext.request.contextPath}/police/exam/update-status" method="post" style="display:inline;">
                                        <input type="hidden" name="examId" value="${selectedExam.examID}">
                                        <input type="hidden" name="status" value="Completed">
                                        <button type="submit" class="btn btn-sm btn-success" onclick="return confirm('Bạn có chắc chắn muốn kết thúc kỳ thi này không?')">Kết thúc kỳ thi</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty students}">
                                    <p class="text-muted">Không có học sinh tham gia kỳ thi này.</p>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th>STT</th>
                                                    <th>Mã học sinh</th>
                                                    <th>Họ và tên</th>
                                                    <th>Lớp</th>
                                                    <th>Trường</th>
                                                    <th>Vi phạm</th>
                                                    <th>Hành động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${students}" var="student" varStatus="loop">
                                                    <tr>
                                                        <td>${loop.count + (currentStudentPage - 1) * studentPageSize}</td>
                                                        <td>${student.userId}</td>
                                                        <td>${student.fullName}</td>
                                                        <td>${student.className}</td>
                                                        <td>${student.school}</td>
                                                        <td>
                                                            <span class="badge bg-${student.hasViolation ? 'danger' : 'success'}">
                                                                ${student.hasViolation ? 'Có vi phạm' : 'Không vi phạm'}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <c:if test="${!student.hasViolation && selectedExam.status eq 'In Progress'}">
                                                                <button type="button" class="btn btn-sm btn-warning violation-btn" 
                                                                        data-bs-toggle="modal" data-bs-target="#violationModal"
                                                                        data-student-id="${student.userId}" 
                                                                        data-student-name="${student.fullName}">
                                                                    Ghi nhận vi phạm
                                                                </button>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <!-- Thống kê -->
                                    <div class="mt-3">
                                        <p>Tổng số học sinh: ${totalStudents} | Số vi phạm: ${violationCount}</p>
                                    </div>

                                    <!-- Phân trang học sinh -->
                                    <c:if test="${totalStudentPages > 1}">
                                        <nav aria-label="Student page navigation">
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item ${currentStudentPage == 1 ? 'disabled' : ''}">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/police/exam/supervision?examId=${selectedExam.examID}&studentPage=${currentStudentPage - 1}&page=${currentPage}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">Trước</a>
                                                </li>
                                                <c:forEach begin="1" end="${totalStudentPages}" var="i">
                                                    <li class="page-item ${currentStudentPage == i ? 'active' : ''}">
                                                        <a class="page-link" href="${pageContext.request.contextPath}/police/exam/supervision?examId=${selectedExam.examID}&studentPage=${i}&page=${currentPage}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                                <li class="page-item ${currentStudentPage == totalStudentPages ? 'disabled' : ''}">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/police/exam/supervision?examId=${selectedExam.examID}&studentPage=${currentStudentPage + 1}&page=${currentPage}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">Sau</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </main>
        </div>
    </div>

    <!-- Modal ghi nhận vi phạm -->
    <div class="modal fade" id="violationModal" tabindex="-1" aria-labelledby="violationModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="violationModalLabel">Ghi nhận vi phạm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="violationForm" action="${pageContext.request.contextPath}/police/exam/record-violation" method="post">
                        <input type="hidden" name="examId" value="${selectedExam.examID}">
                        <input type="hidden" id="modalStudentId" name="studentId">
                        <div class="mb-3">
                            <label class="form-label">Học sinh: <span id="modalStudentName"></span></label>
                        </div>
                        <div class="mb-3">
                            <label for="violationDetail" class="form-label">Chi tiết vi phạm:</label>
                            <textarea class="form-control" id="violationDetail" name="violationDetail" rows="5" required></textarea>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-warning">Ghi nhận</button>
                        </div>
                    </form>
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
            // Xử lý nút ghi nhận vi phạm
            $('.violation-btn').on('click', function() {
                var studentId = $(this).data('student-id');
                var studentName = $(this).data('student-name');

                $('#modalStudentId').val(studentId);
                $('#modalStudentName').text(studentName);
            });

            // Xác nhận trước khi ghi nhận vi phạm
            $('#violationForm').on('submit', function(e) {
                if (!confirm('Bạn có chắc chắn muốn ghi nhận vi phạm cho học sinh này không?')) {
                    e.preventDefault();
                }
            });
        });
    </script>
</body>
</html>