<%-- 
    Document   : profile
    Created on : Mar 23, 2025, 12:31:26 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hồ sơ - Cảnh sát giao thông - Chứng chỉ kỹ năng lái xe an toàn</title>
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
        .form-group {
            margin-bottom: 15px;
        }
        .form-control {
            width: 100%;
            padding: 8px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .btn-save {
            background-color: #28a745;
            border-color: #28a745;
        }
        .btn-save:hover {
            background-color: #218838;
            border-color: #1e7e34;
        }
        .btn-back {
            background-color: #6c757d;
            border-color: #6c757d;
        }
        .btn-back:hover {
            background-color: #5a6268;
            border-color: #545b62;
        }
    </style>
</head>
<body>
    <%-- Include header --%>
    <%-- <jsp:include page="/views/common/header.jsp" /> --%>

    <div class="container-fluid">
        <div class="row">
            <%-- Include sidebar (if applicable) --%>
            <%-- <jsp:include page="/views/common/sidebar.jsp" /> --%>

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Hồ sơ - Cảnh sát giao thông</h1>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Form cập nhật thông tin cá nhân -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Cập nhật thông tin cá nhân</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/UpdateUserProfileServlet" method="post">
                            <!-- Trường ẩn userID -->
                            <input type="hidden" name="userID" value="${user.userID}">

                            <div class="form-group">
                                <label for="fullName"><strong>Họ và tên(required):</strong></label>
                                <input type="text" id="fullName" name="fullName" class="form-control" value="${user.fullName}" required>
                            </div>
                            <div class="form-group">
                                <label for="email"><strong>Email(required):</strong></label>
                                <input type="email" id="email" name="email" class="form-control" value="${user.email}" required>
                            </div>
                            <div class="form-group">
                                <label for="phone"><strong>Số điện thoại(optional):</strong></label>
                                <input type="Solvedtext" id="phone" name="phone" class="form-control" value="${user.phone}" pattern="0\d{9}" title="Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số" >
                            </div>
                            <div class="form-group">
                                <label for="className"><strong>Lớp(optional):</strong></label>
                                <input type="text" id="className" name="className" class="form-control" value="${user.className}" >
                            </div>
                            <div class="form-group">
                                <label for="school"><strong>Trường(optional):</strong></label>
                                <input type="text" id="school" name="school" class="form-control" value="${user.school}" >
                            </div>
                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-sm btn-save">Lưu thay đổi</button>
                                <a href="${pageContext.request.contextPath}/UserdashboardServlet?role=trafficpolice" class="btn btn-sm btn-back">Quay lại</a>
                            </div>
                        </form>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Footer -->
    <%-- <jsp:include page="/views/common/footer.jsp" /> --%>

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>