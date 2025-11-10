<%-- 
    Document   : login
    Created on : Feb 27, 2025, 2:28:51 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Liên kết CSS -->
<!--        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">-->
        <!-- Font Awesome (nếu cần icon) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/LoginCSS/LoginCSS.css">
        <title>Chứng Chỉ Lái Xe</title>
    </head>
    <body>
        <div class="wrapper">
            <nav class="nav">
                <div class="nav-logo">
                    <p>Group10</p>
                </div>
                <div class="nav-menu">
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/views/common/login.jsp" class="link active">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/Blog.jsp" class="link">Blog</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/Service.jsp" class="link">Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/interface/About.jsp" class="link">About</a></li>
                    </ul>
                </div>
                <div class="nav-button">
                    <button class="btn" id="loginBtn">Sign In</button>
                    <button class="btn" id="registerBtn">Sign Up</button>
                </div>
                <div class="nav-menu-btn"></div>
            </nav>

            <!------ Form Box-------->
            <div class="form-box">
                <!-- role selection form -->
                <div class="login-container" id="role-selection" style="display: flex;">
                    <div class="top">
                        <header>Chọn Vai Trò</header>
                    </div>
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <input type="hidden" name="action" value="selectRole">
                        <div class="input-box">
                            <select name="role" class="input-field" required>
                                <option value="" disabled selected>Chọn vai trò</option>
                                <option value="user">Người dùng (Student/Teacher/Police)</option>
                                <option value="admin">Admin</option>
                            </select>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Tiếp tục">
                        </div>
                        <c:if test="${not empty error}">
                            <div class="error" style="color: red; text-align: center;">${error}</div>
                        </c:if>
                        <c:if test="${not empty success}">
                            <div style="color: green; text-align: center;">${success}</div>
                        </c:if>    
                    </form>
                </div>

                <!-- User registration form -->
                <div class="register-container" id="register" style="display: none;">
                    <div class="top">
                        <span>Have an account? <a href="#" onclick="showRoleSelection()">Login</a></span>
                        <header>Sign Up</header>
                    </div>
                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="two-forms">
                            <div class="input-box">
                                <input type="text" class="input-field" name="firstname" placeholder="Firstname" required>
                                <i class="bx bx-user"></i>
                            </div>
                            <div class="input-box">
                                <input type="text" class="input-field" name="lastname" placeholder="Lastname" required>
                                <i class="bx bx-user"></i>
                            </div>
                        </div>
                        <div class="input-box">
                            <input type="text" class="input-field" name="email" placeholder="Email" required>
                            <i class="bx bx-envelope"></i>
                        </div>
                        <div class="input-box">
                            <input type="password" class="input-field" name="password" placeholder="Password" required>
                            <i class="bx bx-lock-alt"></i>
                        </div>
                        <div class="input-box">
                            <select name="role" class="input-field" required>
                                <option value="" disabled selected>Chọn vai trò</option>
                                <option value="student">Student</option>
                                <option value="teacher">Teacher</option>
                                <option value="trafficpolice">Police</option>
                            </select>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="text" class="input-field" name="class" placeholder="Class (optional)">
                            <i class="bx bx-book"></i>
                        </div>
                        <div class="input-box">
                            <input type="text" class="input-field" name="school" placeholder="School (optional)">
                            <i class="bx bx-building"></i>
                        </div>
                        <div class="input-box">
                            <input type="text" class="input-field" name="phone" placeholder="Phone (optional)">
                            <i class="bx bx-phone"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Register">
                        </div>
                        <div class="two-col">
                            <div class="one">
                                <input type="checkbox" id="register-check" name="rememberMe">
                                <label for="register-check">Remember Me</label>
                            </div>
                            <div class="two">
                                <label><a href="#">Terms & conditions</a></label>
                            </div>
                        </div>
                    </form>
                </div>
                        
                        <!-- Admin registration form -->
                <div class="register-container" id="admin-register" style="display: none;">
                    <div class="top">
                        <span>Back to role selection? <a href="#" onclick="showRoleSelection()">Role Selection</a></span>
                        <header>Admin Sign Up</header>
                    </div>
                    <form action="${pageContext.request.contextPath}/admin/register" method="post">
                        <div class="input-box">
                            <input type="text" class="input-field" name="username" placeholder="Username" required>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="text" class="input-field" name="fullName" placeholder="Full Name" required>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="text" class="input-field" name="email" placeholder="Email" required>
                            <i class="bx bx-envelope"></i>
                        </div>
                        <div class="input-box">
                            <input type="password" class="input-field" name="prePassword" placeholder="Admin Pre-Password" required>
                            <i class="bx bx-lock-alt"></i>
                        </div>
                        <div class="input-box">
                            <input type="password" class="input-field" name="password" placeholder="Password" required>
                            <i class="bx bx-lock-alt"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Register Admin">
                        </div>
                    </form>
                </div>

                <!-- login form -->
                <div class="login-container" id="login" style="display: none;">
                    <div class="top">
                        <span>Don't have an account? <a href="#" onclick="register()">Sign Up</a></span>
                        <span>Or <a href="#" onclick="adminRegister()">Admin Sign Up</a></span>
                        <header>Login</header>
                    </div>
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <input type="hidden" name="action" value="selectRole">
                        
                        <div class="input-box">
                            <select name="role" class="input-field" required>
                                <option value="" disabled selected>Chọn vai trò</option>
                                <option value="user">Người dùng (Student/Teacher/Police)</option>
                                <option value="admin">Admin</option>
                            </select>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Sign In">
                        </div>
                        <div class="two-col">
                            <div class="one">
                                <input type="checkbox" id="login-check" name="rememberMe">
                                <label for="login-check">Remember Me</label>
                            </div>
                            <div class="two">
                                <label><a href="#">Forgot Password?</a></label>
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
            function showRoleSelection() {
                document.getElementById('register').style.display = 'none';
                document.getElementById('login').style.display = 'flex';
                document.getElementById('role-selection').style.display = 'none';
                document.getElementById('admin-register').style.display = 'none';
            }

            function login() {
                document.getElementById('register').style.display = 'none';
                document.getElementById('login').style.display = 'flex';
                document.getElementById('role-selection').style.display = 'none';
                document.getElementById('admin-register').style.display = 'none';
            }

            function register() {
                document.getElementById('login').style.display = 'none';
                document.getElementById('role-selection').style.display = 'none';
                document.getElementById('register').style.display = 'flex';
                document.getElementById('admin-register').style.display = 'none';
            }
            
            function adminRegister() {
                document.getElementById('login').style.display = 'none';
                document.getElementById('register').style.display = 'none';
                document.getElementById('role-selection').style.display = 'none';
                document.getElementById('admin-register').style.display = 'flex';
            }

            // Add event listeners to navigation buttons
            document.getElementById('loginBtn').addEventListener('click', login);
            document.getElementById('registerBtn').addEventListener('click', register);
            // Show role selection by default on page load
            showRoleSelection();
        </script>
    </body>
</html>