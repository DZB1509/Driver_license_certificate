<%-- 
    Document   : exam_registration
    Created on : Mar 2, 2025, 6:19:30 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký thi - Chứng chỉ kỹ năng lái xe an toàn</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
        <style>
            .table-responsive {
                overflow-x: auto;
            }
            .pagination {
                margin-top: 20px;
                justify-content: center;
            }
            .filter-form {
                margin-bottom: 20px;
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
                        <h1 class="h2">Đăng ký kỳ thi</h1>
                    </div>

                    <!-- Thông báo -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                            ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Khóa học đã đăng ký -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Khóa học đã đăng ký</h5>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty registeredCourses}">
                                    <p class="text-muted">Bạn chưa đăng ký khóa học nào. Vui lòng đăng ký khóa học trước khi đăng ký thi.</p>
                                    <a href="${pageContext.request.contextPath}/student/course/register" class="btn btn-primary">Đăng ký khóa học</a>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Khóa học</th>
                                                    <th>Giảng viên</th>
                                                    <th>Thời gian</th>
                                                    <th>Trạng thái</th>
                                                    <th>Tùy chọn</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${registeredCourses}" var="course">
                                                    <tr>
                                                        <td>${course.courseName}</td>
                                                        <td>${course.teacherName}</td>
                                                        <td>
                                                            <fmt:formatDate value="${course.startDate}" pattern="dd/MM/yyyy"/> - 
                                                            <fmt:formatDate value="${course.endDate}" pattern="dd/MM/yyyy"/>
                                                        </td>
                                                        <td>
                                                            <span class="badge bg-${course.status eq 'Completed' ? 'success' : course.status eq 'Active' ? 'primary' : 'secondary'}">
                                                                ${course.status}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${course.status eq 'Completed' && not course.examRegistered}">
                                                                    <button type="button" class="btn btn-sm btn-primary" 
                                                                            data-bs-toggle="modal" data-bs-target="#registerExamModal" 
                                                                            data-course-id="${course.courseID}">
                                                                        Đăng ký thi
                                                                    </button>
                                                                </c:when>
                                                                <c:when test="${course.examRegistered}">
                                                                    <span class="badge bg-info">Đã đăng ký thi</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-muted">Chưa hoàn thành khóa học</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <!-- Phân trang cho khóa học -->
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination">
                                            <c:if test="${currentPage > 1}">
                                                <li class="page-item">
                                                    <a class="page-link" href="?page=${currentPage - 1}">Trước</a>
                                                </li>
                                            </c:if>
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage eq i ? 'active' : ''}">
                                                    <a class="page-link" href="?page=${i}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <c:if test="${currentPage < totalPages}">
                                                <li class="page-item">
                                                    <a class="page-link" href="?page=${currentPage + 1}">Tiếp</a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </nav>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Danh sách kỳ thi -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Lịch thi sắp tới</h5>
                        </div>
                        <div class="card-body">
                            <!-- Form lọc kỳ thi -->
                            <form class="filter-form" method="get" action="${pageContext.request.contextPath}/student/exam-registration">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="startDate" class="form-label">Từ ngày</label>
                                        <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}">
                                    </div>
                                    <div class="col-md-4">
                                        <label for="endDate" class="form-label">Đến ngày</label>
                                        <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}">
                                    </div>
                                    <div class="col-md-4 d-flex align-items-end">
                                        <button type="submit" class="btn btn-primary">Lọc</button>
                                    </div>
                                </div>
                            </form>

                            <c:choose>
                                <c:when test="${empty upcomingExams}">
                                    <p class="text-muted">Không có kỳ thi nào sắp diễn ra.</p>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th>STT</th>
                                                    <th>Ngày thi</th>
                                                    <th>Phòng thi</th>
                                                    <th>Trạng thái</th>
                                                    <th>Khóa học liên quan</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${upcomingExams}" var="exam" varStatus="loop">
                                                    <tr>
                                                        <td>${loop.count}</td>
                                                        <td><fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/></td>
                                                        <td>${exam.room}</td>
                                                        <td>
                                                            <span class="badge bg-${exam.status eq 'Pending' ? 'warning' : exam.status eq 'Approved' ? 'success' : 'secondary'}">
                                                                ${exam.status}
                                                            </span>
                                                        </td>
                                                        <td>${exam.courseName}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <!-- Phân trang cho kỳ thi -->
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination">
                                            <c:if test="${currentPage > 1}">
                                                <li class="page-item">
                                                    <a class="page-link" href="?page=${currentPage - 1}&startDate=${param.startDate}&endDate=${param.endDate}">Trước</a>
                                                </li>
                                            </c:if>
                                            <c:forEach begin="1" end="${totalExamPages}" var="i">
                                                <li class="page-item ${currentPage eq i ? 'active' : ''}">
                                                    <a class="page-link" href="?page=${i}&startDate=${param.startDate}&endDate=${param.endDate}">${i}</a>
                                                </li>
                                            </c:forEach>
                                            <c:if test="${currentPage < totalExamPages}">
                                                <li class="page-item">
                                                    <a class="page-link" href="?page=${currentPage + 1}&startDate=${param.startDate}&endDate=${param.endDate}">Tiếp</a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </nav>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <!-- Modal đăng ký kỳ thi -->
        <div class="modal fade" id="registerExamModal" tabindex="-1" aria-labelledby="registerExamModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="registerExamModalLabel">Đăng ký kỳ thi</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="registerExamForm" method="post" action="${pageContext.request.contextPath}/student/exam-registration">
                            <input type="hidden" name="action" value="register">
                            <input type="hidden" name="courseId" id="courseId">
                            <div class="mb-3">
                                <label for="examId" class="form-label">Chọn kỳ thi</label>
                                <select class="form-select" id="examId" name="examId" required>
                                    <option value="">-- Chọn kỳ thi --</option>
                                    <c:forEach items="${upcomingExams}" var="exam">
                                        <option value="${exam.examID}">
                                            Ngày thi: <fmt:formatDate value="${exam.date}" pattern="dd/MM/yyyy"/> - Phòng: ${exam.room}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Đăng ký</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- JavaScript -->
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
        <script>
            // Gán courseId vào form khi mở modal
            const registerExamModal = document.getElementById('registerExamModal');
            registerExamModal.addEventListener('show.bs.modal', function (event) {
                const button = event.relatedTarget;
                const courseId = button.getAttribute('data-course-id');
                const courseIdInput = document.getElementById('courseId');
                courseIdInput.value = courseId;
            });
        </script>
    </body>
</html>