/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;
import model.entity.User;
import model.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ADMIN
 */
public class UserLoginController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserLoginController.class.getName());
    private UserService userService;
    
    
    // Phương thức init() để khởi tạo UserService
    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            LOGGER.info("UserLoginController initialized successfully with userService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize UserService: " + e.getMessage());
            throw new ServletException("Failed to initialize UserService", e);
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserLoginController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserLoginController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
    
    private void handleRememberMe(HttpServletResponse response, String email, boolean rememberMe) {
        Cookie emailCookie = new Cookie("userEmail", email); // Đổi tên cookie thành "userEmail"
        emailCookie.setPath("/");
        emailCookie.setHttpOnly(true); // Thêm bảo mật
        if (rememberMe) {
            emailCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
        } else {
            emailCookie.setMaxAge(0); // Xóa cookie
        }
        response.addCookie(emailCookie);
    }
    private void handleLoginFailure(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
        String userType = request.getParameter("userType");
        if ("user".equals(userType)) {
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
                User user = userService.getUserByEmail(emailOrUsername);
               

                // Kiểm tra user tồn tại, mật khẩu khớp và trạng thái hoạt động
                if (user != null && BCrypt.checkpw(password, user.getPassword()) && user.isStatus() ) {
                    // Đăng nhập thành công
                    HttpSession session = request.getSession();
                    session.setAttribute("loggedUser", user); // Thống nhất với LoginServlet
                    // Lưu userId và role để tích hợp với UserAuthFilter
                    session.setAttribute("userId", user.getUserID()); 
                    session.setAttribute("role", user.getRole());

                    // Xử lý "Remember Me"
                    handleRememberMe(response, user.getEmail(), "on".equals(rememberMe));

                    // Chuyển hướng đến trang dashboard của user
                    request.getRequestDispatcher("views/user/dashboard.jsp").forward(request, response);
                } else {
                    handleLoginFailure(request, response, "Tên đăng nhập hoặc mật khẩu không đúng!");
                }
            } catch (Exception e) {
                LOGGER.severe("Error during user login: " + e.getMessage());
                handleLoginFailure(request, response, "Lỗi hệ thống, vui lòng thử lại sau: " + e.getMessage());
            }
        } else {
            // Quay lại LoginServlet nếu không phải user
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
