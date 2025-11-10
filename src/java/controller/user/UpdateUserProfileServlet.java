/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import model.service.UserService;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class UpdateUserProfileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateUserProfileServlet.class.getName());
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            LOGGER.info("UpdateStudentProfileServlet initialized successfully with userService");
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
            out.println("<title>Servlet UpdateUserProfileServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUserProfileServlet at " + request.getContextPath () + "</h1>");
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
        //processRequest(request, response);
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        request.setAttribute("user", user);

        // Chuyển hướng đến trang profile tương ứng với vai trò
        String role = user.getRole().toLowerCase();
        request.getRequestDispatcher("/views/user/" + role + "/profile.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
        response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("loggedUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    try {
        // Lấy thông tin từ form
        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String className = request.getParameter("className"); 
        String school = request.getParameter("school");       

        // Lấy user từ session
        User user = (User) session.getAttribute("loggedUser");
        String role = user.getRole();

        // Validate input cơ bản cho các trường cần thiết
        if (fullName == null || fullName.trim().isEmpty() ||
        email == null || email.trim().isEmpty() ||
        phone == null || phone.trim().isEmpty()) {
        request.setAttribute("message", "Vui lòng điền đầy đủ các thông tin bắt buộc (Họ tên, Email, Số điện thoại)!");
        request.setAttribute("messageType", "danger");
        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/user/" + role.toLowerCase() + "/profile.jsp").forward(request, response);
        return;
    }
        //Kiểm tra số điện thoại bắt đầu bằng số 0 và có tổng cộng 10 số
        if (!phone.matches("0\\d{9}")) {
    request.setAttribute("message", "Số điện thoại phải có đúng 10 chữ số và chỉ chứa ký tự số!");
    request.setAttribute("messageType", "danger");
    request.setAttribute("user", user);
    request.getRequestDispatcher("/views/user/" + role.toLowerCase() + "/profile.jsp").forward(request, response);
    return;
}

    // Cập nhật thông tin user (áp dụng cho tất cả vai trò)
    user.setFullName(fullName);
    user.setEmail(email);
    user.setPhone(phone);
    user.setClassName(className != null && !className.trim().isEmpty() ? className.trim() : null);
    user.setSchool(school != null && !school.trim().isEmpty() ? school.trim() : null);
        // Cập nhật vào database
        boolean updated = userService.updateUserProfile(user);
        if (!updated) {
            throw new Exception("Không thể cập nhật thông tin trong cơ sở dữ liệu!");
        }

        // Cập nhật lại session
        session.setAttribute("loggedUser", user);

        // Thông báo thành công
        session.setAttribute("message", "Cập nhật thông tin thành công!");
        session.setAttribute("messageType", "success");

        // Chuyển hướng về dashboard theo vai trò
        response.sendRedirect(request.getContextPath() + "/UserdashboardServlet?role=" + role.toLowerCase());

    } catch (Exception e) {
        LOGGER.severe("Error updating user profile: " + e.getMessage());
        User user = (User) session.getAttribute("loggedUser");
        request.setAttribute("message", "Lỗi khi cập nhật thông tin: " + e.getMessage());
        request.setAttribute("messageType", "danger");
        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/user/" + user.getRole().toLowerCase() + "/profile.jsp").forward(request, response);
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
