package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class UserdashboardServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserdashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy vai trò từ session (được lưu khi đăng nhập)
        String actualRole = (String) session.getAttribute("role");
        if (actualRole == null || actualRole.trim().isEmpty()) {
            request.setAttribute("error", "Không tìm thấy vai trò của người dùng!");
            request.getRequestDispatcher("/views/user/dashboard.jsp").forward(request, response);
            return;
        }

        // Lấy vai trò đã chọn từ tham số, nếu không có thì dùng actualRole
        String selectedRole = request.getParameter("role");
        if (selectedRole == null || selectedRole.trim().isEmpty()) {
            selectedRole = actualRole;
        }
        LOGGER.info("Role check - actualRole: " + actualRole + ", selectedRole: " + selectedRole);

        // Kiểm tra vai trò đã chọn có khớp với vai trò trong session không
        if (!selectedRole.equalsIgnoreCase(actualRole)) {
            LOGGER.info("Role mismatch - actualRole: " + actualRole + ", selectedRole: " + selectedRole);
            request.setAttribute("error", "Bạn không được phép vào vai trò này!");
            request.getRequestDispatcher("/views/user/dashboard.jsp").forward(request, response);
            return;
        }

        // Điều hướng dựa trên vai trò
        switch (selectedRole.toLowerCase()) {
            case "student":
                LOGGER.info("Forwarding to student dashboard");
                request.getRequestDispatcher("/views/user/student/dashboard.jsp").forward(request, response);
                break;
            case "teacher":
                LOGGER.info("Forwarding to teacher dashboard");
                request.getRequestDispatcher("/views/user/teacher/dashboard.jsp").forward(request, response);
                break;
            case "trafficpolice":
                LOGGER.info("Forwarding to trafficpolice dashboard");
                request.getRequestDispatcher("/views/user/trafficpolice/dashboard.jsp").forward(request, response);
                break;
            default:
                LOGGER.info("Invalid role: " + selectedRole);
                request.setAttribute("error", "Vai trò không hợp lệ: " + selectedRole);
                request.getRequestDispatcher("/views/user/dashboard.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "User Dashboard Servlet";
    }
}