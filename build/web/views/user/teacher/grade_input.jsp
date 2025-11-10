<%-- 
    Document   : grade_input
    Created on : Mar 2, 2025, 6:25:32 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nhập điểm thi - Chứng chỉ kỹ năng lái xe an toàn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <style>
        .table-responsive {
            overflow-x: auto;
        }
        .score-input:invalid {
            border-color: red;
        }
        @media (max-width: 768px) {
            .table th, .table td {
                font-size: 14px;
                padding: 6px;
            }
            .form-control-sm {
                font-size: 12px;
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
                    <h1 class="h2">Nhập điểm thi</h1>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Lựa chọn kỳ thi -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Chọn kỳ thi</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/teacher/grade/input" method="get" class="row g-3">
                            <div class="col-md-6">
                                <label for="examId" class="form-label">Kỳ thi</label>
                                <select class="form-select" id="examId" name="examId" required>
                                    <option value="">-- Chọn kỳ thi --</option>
                                    <c:forEach items="${exams}" var="exam">
                                        <option value="${exam.examID}" ${param.examId eq exam.examID ? 'selected' : ''}>
                                            ${exam.courseName} - <fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/> - Phòng: ${exam.room}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary">Xem danh sách</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Danh sách học sinh -->
                <c:if test="${not empty selectedExam}">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">
                                Danh sách học sinh - ${selectedExam.courseName} 
                                (<fmt:formatDate value="${selectedExam.date}" pattern="dd/MM/yyyy"/>, Phòng: ${selectedExam.room})
                            </h5>
                            <c:if test="${not empty students}">
                                <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                    <i class="bi bi-printer"></i> In danh sách
                                </button>
                            </c:if>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty students}">
                                    <p class="text-muted">Không có học sinh tham gia kỳ thi này.</p>
                                </c:when>
                                <c:otherwise>
                                    <!-- Bộ lọc học sinh -->
                                    <div class="mb-3">
                                        <input type="text" class="form-control" id="studentFilter" placeholder="Tìm kiếm theo mã hoặc tên học sinh">
                                    </div>

                                    <form action="${pageContext.request.contextPath}/teacher/grade/save" method="post" id="gradeForm">
                                        <input type="hidden" name="examId" value="${selectedExam.examID}">

                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover" id="studentTable">
                                                <thead>
                                                    <tr>
                                                        <th>STT</th>
                                                        <th>Mã học sinh</th>
                                                        <th>Họ và tên</th>
                                                        <th>Lớp</th>
                                                        <th>Trường</th>
                                                        <th>Điểm số</th>
                                                        <th>Kết quả</th>
                                                        <th>Nhận xét</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${students}" var="student" varStatus="status">
                                                        <tr class="student-row">
                                                            <td>${status.count + (currentPage - 1) * pageSize}</td>
                                                            <td class="student-id">${student.userId}</td>
                                                            <td class="student-name">${student.fullName}</td>
                                                            <td>${student.className}</td>
                                                            <td>${student.school}</td>
                                                            <td>
                                                                <input type="hidden" name="studentId" value="${student.userId}">
                                                                <input type="number" class="form-control form-control-sm score-input" 
                                                                       name="score_${student.userId}" id="score_${student.userId}"
                                                                       value="${student.score}" min="0" max="10" step="0.1" 
                                                                       ${student.hasScore ? 'readonly' : ''} required>
                                                            </td>
                                                            <td>
                                                                <div class="form-check form-switch">
                                                                    <input class="form-check-input pass-status" type="checkbox" 
                                                                           id="passStatus_${student.userId}" name="passStatus_${student.userId}"
                                                                           ${student.passStatus ? 'checked' : ''} 
                                                                           ${student.hasScore ? 'disabled' : ''}>
                                                                    <label class="form-check-label pass-status-label" for="passStatus_${student.userId}">
                                                                        ${student.passStatus ? 'Đạt' : 'Không đạt'}
                                                                    </label>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-sm btn-info comment-btn" 
                                                                        data-bs-toggle="modal" data-bs-target="#commentModal" 
                                                                        data-student-id="${student.userId}" 
                                                                        data-student-name="${student.fullName}"
                                                                        data-comment="${student.comment}"
                                                                        ${student.hasScore ? 'disabled' : ''}>
                                                                    Nhận xét
                                                                </button>
                                                                <input type="hidden" id="comment_${student.userId}" name="comment_${student.userId}" 
                                                                       value="${student.comment}">
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
                                                        <a class="page-link" href="${pageContext.request.contextPath}/teacher/grade/input?examId=${selectedExam.examID}&page=${currentPage - 1}">Trước</a>
                                                    </li>
                                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                            <a class="page-link" href="${pageContext.request.contextPath}/teacher/grade/input?examId=${selectedExam.examID}&page=${i}">${i}</a>
                                                        </li>
                                                    </c:forEach>
                                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                                        <a class="page-link" href="${pageContext.request.contextPath}/teacher/grade/input?examId=${selectedExam.examID}&page=${currentPage + 1}">Sau</a>
                                                    </li>
                                                </ul>
                                            </nav>
                                        </c:if>

                                        <!-- Thống kê -->
                                        <div class="mt-3">
                                            <p>Tổng số học sinh: ${totalStudents} | Đạt: <span id="passCount">0</span> | Điểm trung bình: <span id="averageScore">0</span></p>
                                        </div>

                                        <div class="mt-3 d-flex justify-content-between">
                                            <button type="button" class="btn btn-secondary" onclick="window.history.back()">Quay lại</button>
                                            <c:if test="${!selectedExam.gradesSubmitted}">
                                                <button type="submit" class="btn btn-primary">Lưu điểm</button>
                                            </c:if>
                                        </div>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </main>
        </div>
    </div>

    <!-- Modal nhận xét -->
    <div class="modal fade" id="commentModal" tabindex="-1" aria-labelledby="commentModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="commentModalLabel">Nhận xét đánh giá</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="modalStudentId">
                    <div class="mb-3">
                        <label class="form-label">Học sinh: <span id="modalStudentName"></span></label>
                    </div>
                    <div class="mb-3">
                        <label for="modalComment" class="form-label">Nhận xét:</label>
                        <textarea class="form-control" id="modalComment" rows="5"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary" id="saveCommentBtn">Lưu nhận xét</button>
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
            // Cập nhật trạng thái đạt/không đạt khi thay đổi điểm
            $('.score-input').on('input', function() {
                var studentId = $(this).attr('name').split('_')[1];
                var score = parseFloat($(this).val());
                var passCheckbox = $('#passStatus_' + studentId);

                // Kiểm tra điểm hợp lệ
                if (isNaN(score) || score < 0 || score > 10) {
                    $(this).addClass('is-invalid');
                    passCheckbox.prop('checked', false);
                    passCheckbox.next('.pass-status-label').text('Không đạt');
                    return;
                }

                $(this).removeClass('is-invalid');
                // Đạt khi điểm >= 5, không đạt khi điểm < 5
                if (score >= 5) {
                    passCheckbox.prop('checked', true);
                    passCheckbox.next('.pass-status-label').text('Đạt');
                } else {
                    passCheckbox.prop('checked', false);
                    passCheckbox.next('.pass-status-label').text('Không đạt');
                }

                updateStats();
            });

            // Cập nhật hiển thị trạng thái khi thay đổi checkbox
            $('.pass-status').on('change', function() {
                var isChecked = $(this).is(':checked');
                $(this).next('.pass-status-label').text(isChecked ? 'Đạt' : 'Không đạt');
                updateStats();
            });

            // Xử lý nút nhận xét
            $('.comment-btn').on('click', function() {
                var studentId = $(this).data('student-id');
                var studentName = $(this).data('student-name');
                var comment = $(this).data('comment');

                $('#modalStudentId').val(studentId);
                $('#modalStudentName').text(studentName);
                $('#modalComment').val(comment || '');
            });

            // Lưu nhận xét
            $('#saveCommentBtn').on('click', function() {
                var studentId = $('#modalStudentId').val();
                var comment = $('#modalComment').val();

                // Cập nhật giá trị vào input hidden
                $('#comment_' + studentId).val(comment);

                // Cập nhật dữ liệu cho nút nhận xét
                $('button[data-student-id="' + studentId + '"]').data('comment', comment);

                // Đóng modal và thông báo
                $('#commentModal').modal('hide');
                alert('Nhận xét đã được lưu tạm thời. Nhấn "Lưu điểm" để lưu hoàn tất.');
            });

            // Kiểm tra và xác nhận trước khi submit form
            $('#gradeForm').on('submit', function(e) {
                var invalidScores = $('.score-input').filter(function() {
                    var score = parseFloat($(this).val());
                    return isNaN(score) || score < 0 || score > 10;
                });

                if (invalidScores.length > 0) {
                    e.preventDefault();
                    alert('Vui lòng kiểm tra lại các điểm số. Điểm phải nằm trong khoảng từ 0 đến 10.');
                    invalidScores.first().focus();
                    return;
                }

                if (!confirm('Bạn có chắc chắn muốn lưu điểm cho các học sinh này không?')) {
                    e.preventDefault();
                }
            });

            // Lọc học sinh theo mã hoặc tên
            $('#studentFilter').on('input', function() {
                var filter = $(this).val().toLowerCase();
                $('#studentTable tbody tr').each(function() {
                    var studentId = $(this).find('.student-id').text().toLowerCase();
                    var studentName = $(this).find('.student-name').text().toLowerCase();
                    if (studentId.includes(filter) || studentName.includes(filter)) {
                        $(this).show();
                    } else {
                        $(this).hide();
                    }
                });
                updateStats();
            });

            // Hàm cập nhật thống kê
            function updateStats() {
                var passCount = $('.pass-status:checked').length;
                var scores = $('.score-input').map(function() {
                    var score = parseFloat($(this).val());
                    return isNaN(score) ? 0 : score;
                }).get();
                var totalScores = scores.reduce((a, b) => a + b, 0);
                var visibleRows = $('#studentTable tbody tr:visible').length;
                var average = visibleRows > 0 ? (totalScores / visibleRows).toFixed(2) : 0;

                $('#passCount').text(passCount);
                $('#averageScore').text(average);
            }

            // Cập nhật thống kê ban đầu
            updateStats();
        });
    </script>
</body>
</html>