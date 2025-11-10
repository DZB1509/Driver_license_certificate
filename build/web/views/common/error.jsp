<%-- 
    Document   : error
    Created on : Mar 2, 2025, 6:32:39 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lỗi - Hệ thống Quản lý Chứng chỉ Lái xe An toàn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css">
    <style>
        .error-container {
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
        }
        .error-icon {
            font-size: 3rem;
            color: #dc3545;
            margin-bottom: 20px;
        }
        .error-code {
            font-size: 1.5rem;
            font-weight: bold;
            color: #dc3545;
        }
        .error-message {
            font-size: 1.1rem;
            margin-bottom: 20px;
        }
        .error-time {
            font-size: 0.9rem;
            color: #6c757d;
        }
        @media (max-width: 768px) {
            .error-container {
                margin: 20px;
            }
            .error-icon {
                font-size: 2rem;
            }
            .error-code {
                font-size: 1.2rem;
            }
            .error-message {
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>
    <%-- Include header --%>
    <jsp:include page="/WEB-INF/tags/navbar.tag" />

    <div class="container-fluid">
        <div class="row">
            <%-- Include sidebar --%>
            <jsp:include page="/WEB-INF/tags/sidebar.tag">
                <jsp:param name="active" value="dashboard" />
            </jsp:include>

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="error-container">
                    <i class="bi bi-exclamation-triangle-fill error-icon"></i>
                    <h1 class="mt-4">Đã xảy ra lỗi</h1>
                    <c:if test="${not empty status}">
                        <p class="error-code">Mã lỗi: ${status}</p>
                    </c:if>
                    <div class="alert alert-danger error-message">
                        <c:choose>
                            <c:when test="${not empty error}">
                                ${error}
                            </c:when>
                            <c:when test="${not empty exception}">
                                ${exception.message}
                            </c:when>
                            <c:otherwise>
                                Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <p class="error-time">Thời gian xảy ra lỗi: <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'Student'}">
                            <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-primary">Quay lại Trang chủ</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Teacher'}">
                            <a href="${pageContext.request.contextPath}/teacher/dashboard" class="btn btn-primary">Quay lại Trang chủ</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Admin'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary">Quay lại Trang chủ</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Police'}">
                            <a href="${pageContext.request.contextPath}/police/dashboard" class="btn btn-primary">Quay lại Trang chủ</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Quay lại Trang chủ</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>
    </div>

    <%-- Include footer --%>
    <jsp:include page="/WEB-INF/tags/footer.tag" />

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>