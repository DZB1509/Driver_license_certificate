<%-- 
    Document   : header
    Created on : Feb 27, 2025, 2:43:08 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Header</title>
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Logo" height="30">
                Hệ thống quản lý chứng chỉ lái xe an toàn
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <c:if test="${loggedUser != null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/user/student/dashboard">Bảng điều khiển</a>
                        </li>
                        <li class="nav-item">
                            <span class="nav-link text-light">Chào, ${loggedUser.fullName}</span>
                        </li>
                    </c:if>
                    <c:if test="${loggedAdmin != null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Bảng điều khiển</a>
                        </li>
                        <li class="nav-item">
                            <span class="nav-link text-light">Quản trị viên: ${loggedAdmin.fullName}</span>
                        </li>
                    </c:if>
                    <c:if test="${loggedUser != null || loggedAdmin != null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
    </body>
</html>
