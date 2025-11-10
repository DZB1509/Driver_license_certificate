/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.admin;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import model.entity.Admin;
import model.service.AdminService;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ADMIN
 */
public class AdminLoginController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminLoginController.class.getName());
    private AdminService adminService;

    // Phương thức init() để khởi tạo AdminService
    @Override
    public void init() throws ServletException {
        try {
            adminService = new AdminService();
            LOGGER.info("AdminLoginController initialized successfully with AdminService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize AdminService: " + e.getMessage());
            throw new ServletException("Failed to initialize AdminService", e);
        }
    }
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/views/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String adminUsername = getCookieValue(request, "adminUsername");
        if (adminUsername != null) {
            request.setAttribute("adminUsername", adminUsername);
        }

        // Hiển thị trang login.jsp dành cho admin
        request.getRequestDispatcher("/views/admin/login.jsp").forward(request, response);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userType = request.getParameter("userType");
    if ("admin".equals(userType)) {
        try {
            // Lấy dữ liệu từ form
            String emailOrUsername = request.getParameter("emailOrUsername");
            String password = request.getParameter("password");
            String rememberMe = request.getParameter("rememberMe"); // "on" nếu được chọn

            // Kiểm tra thông tin đầu vào
            if (emailOrUsername == null || emailOrUsername.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                handleLoginFailure(request, response, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Kiểm tra thông tin đăng nhập
            Admin admin = adminService.getAdminByEmailOrUsername(emailOrUsername);
           
            // Kiểm tra admin tồn tại, mật khẩu khớp và trạng thái hoạt động
            if (admin != null && BCrypt.checkpw(password, admin.getPassword()) && admin.isStatus()) {
                // Đăng nhập thành công
                HttpSession session = request.getSession();
                session.setAttribute("loggedAdmin", admin); // Thống nhất với LoginServlet
                // Lưu userId và role để tích hợp với UserAuthFilter
                session.setAttribute("userId", admin.getAdminID()); // Sửa getAdminId() thành getAdminID()
                session.setAttribute("role", "admin");

                // Xử lý "Remember Me"
                handleRememberMe(response, admin.getUsername(), "on".equals(rememberMe));

                // Chuyển hướng đến trang dashboard của admin
                response.sendRedirect(request.getContextPath() + "/views/admin/dashboard.jsp");
            } else {
                handleLoginFailure(request, response, "Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } catch (Exception e) {
            LOGGER.severe("Error during admin login: " + e.getMessage());
            handleLoginFailure(request, response, "Lỗi hệ thống, vui lòng thử lại sau: " + e.getMessage());
        }
    } else {
        // Quay lại LoginServlet nếu không phải admin
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
    }
    }

    private void handleRememberMe(HttpServletResponse response, String username, boolean rememberMe) {
        Cookie usernameCookie = new Cookie("adminUsername", username);
        usernameCookie.setPath("/");
        usernameCookie.setHttpOnly(true); // Thêm bảo mật
        if (rememberMe) {
            usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
        } else {
            usernameCookie.setMaxAge(0); // Xóa cookie
        }
        response.addCookie(usernameCookie);
    }

    private void handleLoginFailure(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/views/admin/login.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng nhập cho Admin.";
    }

    @Override
    public void destroy() {
        LOGGER.info("AdminLoginController destroyed");
    }
}
