<%-- 
    Document   : user_list
    Created on : Feb 27, 2025, 2:39:18 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý người dùng</title>
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <div class="container mt-5">
        <h2>Quản lý người dùng</h2>
        <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success" role="alert">
                Thao tác thành công!
            </div>
        <% } %>
        <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger" role="alert">
                Thao tác thất bại!
            </div>
        <% } %>
        <a href="${pageContext.request.contextPath}/admin/user-management/create-user" class="btn btn-primary mb-3">Thêm người dùng mới</a>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Họ và tên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.userID}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td>${user.phone}</td>
                        <td>${user.status ? 'Hoạt động' : 'Vô hiệu'}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/user-management/edit-user?userId=${user.userID}" class="btn btn-sm btn-warning">Sửa</a>
                            <form action="${pageContext.request.contextPath}/admin/user-management" method="post" style="display:inline;">
                                <input type="hidden" name="userId" value="${user.userID}">
                                <input type="hidden" name="action" value="${user.status ? 'deactivate' : 'activate'}">
                                <button type="submit" class="btn btn-sm ${user.status ? 'btn-danger' : 'btn-success'}">
                                    ${user.status ? 'Vô hiệu' : 'Kích hoạt'}
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <nav>
            <ul class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/user-management?page=${i}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>
    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    </body>
</html>
