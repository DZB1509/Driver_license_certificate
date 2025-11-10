/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.common;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.logging.Logger;
import model.entity.User;
import model.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ADMIN
 */
public class RegistrationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class.getName());
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            LOGGER.info("RegistrationServlet initialized successfully with UserService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize services: " + e.getMessage());
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
            out.println("<title>Servlet RegistrationServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegistrationServlet at " + request.getContextPath () + "</h1>");
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
        request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
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
        
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String className = request.getParameter("class");
        String school = request.getParameter("school");
        String phone = request.getParameter("phone");

        // Kiểm tra dữ liệu đầu vào
        if (firstname == null || firstname.trim().isEmpty() ||
                lastname == null || lastname.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ các thông tin bắt buộc!");
            request.setAttribute("showRegister", true);
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            return;
        }

        // Kiểm tra role hợp lệ
        if (!role.equals("student") && !role.equals("teacher") && !role.equals("trafficpolice")) {
            request.setAttribute("error", "Vai trò không hợp lệ!");
            request.setAttribute("showRegister", true);
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra số điện thoại: phải bắt đầu bằng 0 và có đúng 10 chữ số
    if (!phone.matches("0\\d{9}")) {
        request.setAttribute("error", "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số!");
        request.setAttribute("showRegister", true);
        request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
        return;
    }

        // Tạo đối tượng User
        User newUser = new User();
        newUser.setFullName(firstname + " " + lastname);
        newUser.setEmail(email);
        newUser.setPassword(password); // Mật khẩu sẽ được mã hóa trong UserService
        newUser.setRole(role);
        newUser.setClassName(className != null && !className.trim().isEmpty() ? className : null);
        newUser.setSchool(school != null && !school.trim().isEmpty() ? school : null);
        newUser.setPhone(phone != null && !phone.trim().isEmpty() ? phone : null);
        newUser.setCreatedBy(null); // Gán CreatedBy là null vì người dùng tự đăng ký
        newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newUser.setStatus(true);

        // Đăng ký người dùng
        try {
            boolean success = userService.registerUser(newUser);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/login?success=Registration successful! Please login.");
            } else {
                request.setAttribute("error", "Đăng ký thất bại! Email có thể đã tồn tại hoặc lỗi hệ thống.");
                request.setAttribute("showRegister", true);
                request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.severe("Error during registration: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi đăng ký: " + e.getMessage());
            request.setAttribute("showRegister", true);
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for user registration";
    }// </editor-fold>
    @Override
    public void destroy() {
        LOGGER.info("RegistrationServlet destroyed");
    }

}
