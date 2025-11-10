/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.util.logging.Logger;
import model.entity.Admin;
import model.service.AdminService;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "AdminRegistrationServlet", urlPatterns = {"/admin/register"})
public class AdminRegistrationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminRegistrationServlet.class.getName());
    private AdminService adminService;
    private static final String ADMIN_PRE_PASSWORD = "admin123"; // Mật khẩu kiểm tra cố định

    @Override
    public void init() throws ServletException {
        try {
            adminService = new AdminService();
            LOGGER.info("AdminRegistrationServlet initialized successfully with AdminService");
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminRegistrationServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminRegistrationServlet at " + request.getContextPath () + "</h1>");
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
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String prePassword = request.getParameter("prePassword");
        String password = request.getParameter("password");

        // Kiểm tra dữ liệu đầu vào
        if (username == null || username.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            prePassword == null || prePassword.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.setAttribute("adminRegister", true);
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu prePassword
        if (!prePassword.equals(ADMIN_PRE_PASSWORD)) {
            request.setAttribute("error", "Mật khẩu kiểm tra không đúng!");
            request.setAttribute("adminRegister", true);
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng Admin
        Admin newAdmin = new Admin();
        newAdmin.setUsername(username);
        newAdmin.setFullName(fullName);
        newAdmin.setEmail(email);
        newAdmin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        newAdmin.setStatus(true);

        // Đăng ký admin
        try {
            boolean success = adminService.registerAdmin(newAdmin);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/login?success=Admin registration successful! Please login.");
            } else {
                request.setAttribute("error", "Đăng ký thất bại! Username hoặc email có thể đã tồn tại.");
                request.setAttribute("adminRegister", true);
                request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.severe("Error during admin registration: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi đăng ký: " + e.getMessage());
            request.setAttribute("adminRegister", true);
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
   @Override
    public String getServletInfo() {
        return "Servlet for admin registration";
    }

    @Override
    public void destroy() {
        LOGGER.info("AdminRegistrationServlet destroyed");
    }

}
