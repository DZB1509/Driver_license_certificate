<%-- 
    Document   : create_course
    Created on : 23 Mar 2025, 21:56:30
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tạo khóa học mới - Giáo viên</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
        <!-- Thêm Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <style>
            body {
                font-family: 'Roboto', Arial, sans-serif;
                margin: 0;
                padding: 20px;
                background-color: #f4f4f4;
                color: #333;
            }
            .container-fluid {
                padding: 0 15px;
            }
            .create-course-box {
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 0;
                background-color: #fff;
                margin: 20px auto;
                max-width: 500px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }
            .create-course-box .section-title {
                padding: 15px;
                font-size: 20px;
                font-weight: 500;
                color: #333;
                text-align: center;
            }
            .create-course-box .content {
                padding: 20px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: block;
                font-weight: 500;
                margin-bottom: 5px;
                color: #333;
                font-size: 14px;
            }
            .form-group input,
            .form-group select,
            .form-group textarea {
                width: 100%;
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 14px;
                color: #333;
                background-color: #fff;
                transition: border-color 0.3s;
            }
            .form-group textarea {
                height: 100px;
                resize: vertical;
            }
            .form-group input:focus,
            .form-group select:focus,
            .form-group textarea:focus {
                border-color: #007bff;
                outline: none;
                box-shadow: none;
            }
            .form-group input[type="date"] {
                position: relative;
            }
            .form-group input[type="date"]::-webkit-calendar-picker-indicator {
                opacity: 0.6;
                cursor: pointer;
            }
            .form-group select {
                appearance: none;
                -webkit-appearance: none;
                -moz-appearance: none;
                background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24"><path fill="%23333" d="M7 10l5 5 5-5z"/></svg>') no-repeat right 10px center;
                background-size: 12px;
            }
            .button-group {
                text-align: center;
                margin-top: 20px;
            }
            .btn-primary {
                background-color: #007bff;
                border: none;
                padding: 10px 20px;
                font-size: 14px;
                font-weight: 500;
                border-radius: 4px;
                transition: background-color 0.3s;
            }
            .btn-primary:hover {
                background-color: #0056b3;
            }
            .btn-secondary {
                background-color: #6c757d;
                border: none;
                padding: 10px 20px;
                font-size: 14px;
                font-weight: 500;
                border-radius: 4px;
                margin-left: 10px;
                transition: background-color 0.3s;
            }
            .btn-secondary:hover {
                background-color: #5a6268;
            }
            .alert {
                margin: 20px auto;
                max-width: 500px;
            }
            @media (max-width: 576px) {
                .create-course-box {
                    margin: 10px;
                }
                .form-group input,
                .form-group select,
                .form-group textarea {
                    font-size: 13px;
                    padding: 6px 8px;
                }
                .btn-primary, .btn-secondary {
                    padding: 8px 15px;
                    font-size: 13px;
                }
            }
        </style>
    </head>
    <body>
        <!-- Include header -->
        <jsp:include page="/views/common/header.jsp" />

        <div class="container-fluid">
            <div class="row">
                <main class="col-md-12 px-md-4 py-4">
                    <!-- Thông báo -->
                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-${sessionScope.messageType} alert-dismissible fade show" role="alert">
                            ${sessionScope.message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            <c:remove var="message" scope="session"/>
                            <c:remove var="messageType" scope="session"/>
                        </div>
                    </c:if>

                    <!-- Tạo khóa học -->
                    <div class="create-course-box">
                        <div class="section-title">
                            Tạo khóa học mới
                        </div>
                        <div class="content">
                            <form action="${pageContext.request.contextPath}/CreateCourseServlet" method="post">
                                <div class="form-group">
                                    <label for="courseName">Tên khóa học:</label>
                                    <input type="text" id="courseName" name="courseName" required>
                                </div>
                                <div class="form-group">
                                    <label for="startDate">Ngày bắt đầu:</label>
                                    <input type="date" id="startDate" name="startDate"  required>
                                </div>
                                <div class="form-group">
                                    <label for="endDate">Ngày kết thúc:</label>
                                    <input type="date" id="endDate" name="endDate"  required>
                                </div>
                                <div class="form-group">
                                    <label for="description">Mô tả:</label>
                                    <textarea id="description" name="description" placeholder="Nhập mô tả khóa học (tùy chọn)"></textarea>
                                </div>
                                <div class="form-group">
                                    <label for="status">Trạng thái:</label>
                                    <select id="status" name="status" required>
                                        <option value="Active">Đang diễn ra</option>
                                        <option value="Inactive" selected>Chưa bắt đầu</option>
                                        <option value="Completed">Đã hoàn thành</option>
                                        <option value="Cancelled">Đã hủy</option>
                                    </select>
                                </div>
                                <div class="button-group">
                                    <input type="submit" class="btn btn-primary" value="Tạo khóa học">
                                    <a href="${pageContext.request.contextPath}/user/dashboard?role=teacher" class="btn btn-secondary">Quay lại</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>