<%-- 
    Document   : login
    Created on : Feb 27, 2025, 2:32:31 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LoginCSS/LoginCSS.css">
        <title>Chứng Chỉ Lái Xe - Đăng Nhập Admin</title>
    </head>
    <body>
        <div class="wrapper">
            <nav class="nav">
                <div class="nav-logo">
                    <p>Group 10</p>
                </div>
                <div class="nav-menu">
                    <ul>
                        <li><a href="#" class="link active">Home</a></li>
                        <li><a href="#" class="link">Blog</a></li>
                        <li><a href="#" class="link">Services</a></li>
                        <li><a href="#" class="link">About</a></li>
                    </ul>
                </div>
                <div class="nav-button">
                    <button class="btn" id="loginBtn">Sign In</button>
                </div>
                <div class="nav-menu-btn"></div>
            </nav>

            <!------ Form Box-------->
            <div class="form-box">
                <div class="login-container" id="login">
                    <div class="top">
                        <span>Quay lại? <a href="${pageContext.request.contextPath}/login">Chọn vai trò</a></span>
                        <header>Đăng Nhập Admin</header>
                    </div>
                    <form action="${pageContext.request.contextPath}/AdminLoginController" method="post">
                        <input type="hidden" name="userType" value="admin">
                        <div class="input-box">
                            <input type="text" class="input-field" name="emailOrUsername" placeholder="Email" required>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="password" class="input-field" name="password" placeholder="Mật khẩu" required>
                            <i class="bx bx-lock-alt"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Đăng Nhập">
                        </div>
                        <div class="two-col">
                            <div class="one">
                                <input type="checkbox" id="login-check" name="rememberMe">
                                <label for="login-check">Ghi nhớ</label>
                            </div>
                            <div class="two">
                                <label><a href="#">Quên mật khẩu?</a></label>
                            </div>
                        </div>
                        <c:if test="${not empty error}">
                            <div class="error" style="color: red; text-align: center;">${error}</div>
                        </c:if>
                    </form>
                </div>
            </div>
        </div>

        <script>
            document.getElementById('loginBtn').addEventListener('click', function() {
                document.getElementById('login').style.display = 'flex';
            });
        </script>
    </body>
</html>
