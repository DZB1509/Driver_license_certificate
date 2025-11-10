<%-- 
    Document   : About
    Created on : Mar 23, 2025, 5:05:31 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/interface/About.css">
        <title>Chứng Chỉ Lái Xe - Về Chúng Tôi</title>
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
                        <li><a href="${pageContext.request.contextPath}/interface/Service.jsp" class="link">Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/About.jsp" class="link active">About</a></li>
                    </ul>
                </div>
                <div class="nav-button">
                    <button class="btn" id="loginBtn">Sign In</button>
                    <button class="btn" id="registerBtn">Sign Up</button>
                </div>
                <div class="nav-menu-btn"></div>
            </nav>

            <div class="about-container">
                <header class="about-header">Về Chúng Tôi</header>
                <div class="about-content">
                    <h3>Chào Mừng Đến Với Hệ Thống Chứng Chỉ Lái Xe</h3>
                    <p>Chúng tôi là một nền tảng trực tuyến cung cấp các dịch vụ liên quan đến học và thi chứng chỉ lái xe. Với sứ mệnh giúp người dùng học lái xe an toàn và hiệu quả, chúng tôi mang đến các khóa học chất lượng, bài thi sát hạch thực tế và hệ thống quản lý chứng chỉ minh bạch.</p>
                    <h3>Tầm Nhìn</h3>
                    <p>Trở thành nền tảng hàng đầu hỗ trợ người học lái xe tại Việt Nam, đảm bảo an toàn giao thông và nâng cao ý thức lái xe.</p>
                    <h3>Đội Ngũ</h3>
                    <p>Đội ngũ của chúng tôi bao gồm các chuyên gia giao thông, giáo viên dạy lái xe giàu kinh nghiệm và kỹ sư công nghệ tận tâm.</p>
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