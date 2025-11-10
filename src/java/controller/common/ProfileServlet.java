package controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;
import model.service.UserService;
import model.entity.User;

public class ProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    private UserService userService;

    // Phương thức init() để khởi tạo UserService
    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            LOGGER.info("ProfileServlet initialized successfully with UserService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize UserService: " + e.getMessage());
            throw new ServletException("Failed to initialize UserService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedUser = (User) session.getAttribute("loggedUser");
        request.setAttribute("user", loggedUser);
        request.getRequestDispatcher("views/common/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedUser = (User) session.getAttribute("loggedUser");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String className = request.getParameter("className");
        String school = request.getParameter("school");
        String phone = request.getParameter("phone");

        // Kiểm tra dữ liệu đầu vào
        if (fullName == null || fullName.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.setAttribute("user", loggedUser);
            request.getRequestDispatcher("views/common/profile.jsp").forward(request, response);
            return;
        }

        try {
            // Cập nhật thông tin người dùng
            loggedUser.setFullName(fullName);
            loggedUser.setEmail(email);
            loggedUser.setClassName(className != null ? className.trim() : null);
            loggedUser.setSchool(school != null ? school.trim() : null);
            loggedUser.setPhone(phone);

            if (userService.updateUserProfile(loggedUser)) {
                session.setAttribute("loggedUser", loggedUser); // Cập nhật session
                response.sendRedirect(request.getContextPath() + "/user/profile?success=1");
            } else {
                request.setAttribute("error", "Cập nhật hồ sơ thất bại! Email có thể đã tồn tại.");
                request.setAttribute("user", loggedUser);
                request.getRequestDispatcher("views/common/profile.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.severe("Error updating user profile: " + e.getMessage());
            request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại sau!");
            request.setAttribute("user", loggedUser);
            request.getRequestDispatcher("views/common/profile.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "ProfileServlet for handling user profile updates";
    }

    // Đóng tài nguyên khi servlet bị hủy (nếu cần)
    @Override
    public void destroy() {
        // Không cần đóng UserService vì nó không giữ tài nguyên (DBContext quản lý kết nối)
        LOGGER.info("ProfileServlet destroyed");
    }
}
