package controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;
import model.dao.UserDAO;

public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private UserDAO userDAO;

    // Phương thức init() để khởi tạo UserDAO
    @Override
    public void init() throws ServletException {
        try {
            userDAO = new UserDAO();
            LOGGER.info("LoginServlet initialized successfully with UserDAO");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize UserDAO: " + e.getMessage());
            throw new ServletException("Failed to initialize UserDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("loggedUser") != null) {
        response.sendRedirect(request.getContextPath() + "/user/dashboard");
        return;
    } else if (session != null && session.getAttribute("loggedAdmin") != null) {
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        return;
    }
    String success = request.getParameter("success");
    if (success != null) {
        request.setAttribute("success", success);
    }
    request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("selectRole".equals(action)) {
            // Xử lý lựa chọn vai trò từ views/common/login.jsp
            String role = request.getParameter("role");
            if ("user".equals(role)) {
                // Chuyển đến form đăng nhập user
                request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            } else if ("admin".equals(role)) {
                // Chuyển đến AdminLoginController
                response.sendRedirect(request.getContextPath() + "/AdminLoginController");
            } else {
                request.setAttribute("error", "Vui lòng chọn vai trò hợp lệ!");
                request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Hành động không hợp lệ!");
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "LoginServlet for handling role selection and redirection";
    }

    // Đóng tài nguyên khi servlet bị hủy (nếu cần)
    @Override
    public void destroy() {
        // Không cần đóng UserDAO vì nó không giữ tài nguyên (DBContext quản lý kết nối)
        LOGGER.info("LoginServlet destroyed");
    }
}