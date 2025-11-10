<%-- 
    Document   : edit_user
    Created on : Mar 2, 2025, 6:28:15 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chỉnh sửa người dùng</title>
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <div class="container mt-5">
        <h2>Chỉnh sửa người dùng</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        <form action="${pageContext.request.contextPath}/admin/user-management/edit-user" method="post">
            <input type="hidden" name="userId" value="${user.userID}">
            <div class="mb-3">
                <label for="fullName" class="form-label">Họ và tên</label>
                <input type="text" class="form-control" id="fullName" name="fullName" value="${user.fullName}" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
            </div>
            <div class="mb-3">
                <label for="role" class="form-label">Vai trò</label>
                <select class="form-select" id="role" name="role" required>
                    <option value="student" ${user.role == 'student' ? 'selected' : ''}>Học sinh</option>
                    <option value="teacher" ${user.role == 'teacher' ? 'selected' : ''}>Giảng viên</option>
                    <option value="police" ${user.role == 'police' ? 'selected' : ''}>CSGT</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="phone" class="form-label">Số điện thoại</label>
                <input type="text" class="form-control" id="phone" name="phone" value="${user.phone}" required>
            </div>
            <div class="mb-3">
                <label for="status" class="form-label">Trạng thái</label>
                <select class="form-select" id="status" name="status">
                    <option value="true" ${user.status ? 'selected' : ''}>Hoạt động</option>
                    <option value="false" ${!user.status ? 'selected' : ''}>Vô hiệu</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Cập nhật</button>
        </form>
    </div>
    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    </body>
</html>
