<%-- 
    Document   : Service
    Created on : Mar 23, 2025, 5:04:37 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/interface/Service.css">
        <title>Chứng Chỉ Lái Xe - Dịch Vụ</title>
    </head>
    <body>
        <div class="wrapper">
            <nav class="nav">
                <div class="nav-logo">
                    <p>LOGO</p>
                </div>
                <div class="nav-menu">
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/views/common/login.jsp" class="link">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/Blog.jsp" class="link">Blog</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/Service.jsp" class="link active">Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/About.jsp" class="link">About</a></li>
                    </ul>
                </div>
                <div class="nav-button">
                    <button class="btn" id="loginBtn">Sign In</button>
                    <button class="btn" id="registerBtn">Sign Up</button>
                </div>
                <div class="nav-menu-btn"></div>
            </nav>

            <div class="services-container">
                <header class="services-header">Dịch Vụ Của Chúng Tôi</header>
                <div class="services-content">
                    <div class="service-item">
                        <h3>Khóa Học Lái Xe</h3>
                        <p>Các khóa học trực tuyến và thực hành cho mọi trình độ, từ cơ bản đến nâng cao.</p>
                    </div>
                    <div class="service-item">
                        <h3>Thi Sát Hạch</h3>
                        <p>Bài thi mô phỏng thực tế, giúp bạn tự tin đạt chứng chỉ lái xe.</p>
                    </div>
                    <div class="service-item">
                        <h3>Quản Lý Chứng Chỉ</h3>
                        <p>Hệ thống quản lý chứng chỉ minh bạch, dễ sử dụng, và an toàn.</p>
                    </div>
                    <div class="service-item">
                        <h3>Hỗ Trợ 24/7</h3>
                        <p>Đội ngũ hỗ trợ luôn sẵn sàng giải đáp mọi thắc mắc của bạn.</p>
                    </div>
                </div>
            </div>

            <div class="form-box">
                <div class="forgot-password-container" id="forgot-password" style="display: none;">
                    <div class="top">
                        <span>Quay lại? <a href="#" onclick="showRoleSelection()">Đăng nhập</a></span>
                        <header>Quên Mật Khẩu</header>
                    </div>
                    <form action="${pageContext.request.contextPath}/forgotPassword" method="post">
                        <div class="input-box">
                            <input type="email" class="input-field" name="email" placeholder="Nhập email của bạn" required>
                            <i class="bx bx-envelope"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Gửi Yêu Cầu">
                        </div>
                        <c:if test="${not empty error}">
                            <div class="error" style="color: red; text-align: center;">${error}</div>
                        </c:if>
                    </form>
                </div>
            </div>
        </div>

        <script>
            function showForgotPassword() {
                document.getElementById('forgot-password').style.display = 'flex';
            }

            function showRoleSelection() {
                window.location.href = '${pageContext.request.contextPath}/login.jsp';
            }

            function login() {
                window.location.href = '${pageContext.request.contextPath}/login.jsp';
            }

            function register() {
                window.location.href = '${pageContext.request.contextPath}/login.jsp';
            }

            document.getElementById('loginBtn').addEventListener('click', login);
            document.getElementById('registerBtn').addEventListener('click', register);
        </script>
    </body>
</html>
