<%-- 
    Document   : Blog
    Created on : Mar 23, 2025, 5:02:37 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/interface/Blog.css">
        <title>Chứng Chỉ Lái Xe - Blog</title>
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
                        <li><a href="${pageContext.request.contextPath}/interface/Blog.jsp" class="link active">Blog</a></li>
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

            <div class="blog-container">
                <header class="blog-header">Blog Lái Xe An Toàn</header>
                <div class="blog-content">
                    <div class="blog-post">
                        <h3>Mẹo Lái Xe An Toàn Trong Mưa</h3>
                        <p>Ngày đăng: 22/03/2025</p>
                        <div class="blog-post-content">
                            <p>Lái xe trong mưa có thể nguy hiểm nếu bạn không chuẩn bị kỹ. Dưới đây là một số mẹo giúp bạn lái xe an toàn:</p>
                            <p>Lái xe trong điều kiện mưa bão đòi hỏi sự cẩn thận và chuẩn bị kỹ lưỡng. Dưới đây là một số mẹo chi tiết để đảm bảo an toàn:</p>
                            <ul>
                                <li><strong>Kiểm tra xe trước khi đi:</strong> Đảm bảo lốp xe có độ bám đường tốt, gạt nước hoạt động hiệu quả, và đèn xe sáng rõ.</li>
                                <li><strong>Giữ khoảng cách an toàn:</strong> Trong mưa, đường trơn trượt làm tăng khoảng cách phanh. Hãy giữ khoảng cách gấp đôi so với bình thường.</li>
                                <li><strong>Giảm tốc độ:</strong> Đi chậm hơn để có thời gian phản ứng với các tình huống bất ngờ, đặc biệt trên đường ngập nước.</li>
                                <li><strong>Tránh phanh gấp:</strong> Phanh gấp có thể khiến xe bị trượt. Hãy phanh từ từ và nhẹ nhàng.</li>
                                <li><strong>Bật đèn pha:</strong> Ngay cả khi trời chỉ mưa nhẹ, việc bật đèn pha giúp các xe khác dễ dàng nhìn thấy bạn.</li>
                            </ul>
                            <p>Với những mẹo trên, bạn sẽ tự tin hơn khi lái xe trong thời tiết xấu. Hãy luôn đặt an toàn lên hàng đầu!</p>
                        </div>
                    </div>
                    <div class="blog-post">
                        <h3>Cách Chuẩn Bị Cho Kỳ Thi Sát Hạch</h3>
                        <p>Ngày đăng: 20/03/2025</p>
                        <div class="blog-post-content">
                            <p>Kỳ thi sát hạch lái xe có thể gây áp lực. Hãy làm theo các bước sau để đảm bảo bạn sẵn sàng:</p>
                            <p>Kỳ thi sát hạch lái xe là một bước quan trọng để lấy chứng chỉ lái xe. Dưới đây là các bước chuẩn bị chi tiết:</p>
                            <ul>
                                <li><strong>Học lý thuyết kỹ lưỡng:</strong> Nắm vững 600 câu hỏi lý thuyết, đặc biệt là các câu điểm liệt. Sử dụng ứng dụng học lý thuyết để luyện tập.</li>
                                <li><strong>Luyện tập thực hành:</strong> Tham gia các buổi học thực hành với giáo viên. Tập trung vào các bài thi sa hình như dừng xe, đỗ xe, và đi qua đường hẹp.</li>
                                <li><strong>Giữ tâm lý thoải mái:</strong> Đừng quá lo lắng. Hãy ngủ đủ giấc trước ngày thi và đến sớm để làm quen với môi trường thi.</li>
                                <li><strong>Kiểm tra xe thi:</strong> Làm quen với xe thi, kiểm tra phanh, côn, và ga để tránh bỡ ngỡ khi vào bài thi.</li>
                                <li><strong>Chuẩn bị giấy tờ:</strong> Mang theo CMND/CCCD, hồ sơ thi, và các giấy tờ cần thiết khác theo yêu cầu của trung tâm sát hạch.</li>
                            </ul>
                            <p>Với sự chuẩn bị kỹ lưỡng, bạn sẽ vượt qua kỳ thi sát hạch một cách dễ dàng. Chúc bạn thi tốt!</p>
                        </div>
                    </div>
                    <!-- Thêm các bài viết khác nếu cần -->
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