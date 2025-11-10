/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.User;
import model.service.UserService;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class EditUserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditUserServlet.class.getName());
    private UserService userService;

    // Phương thức init() để khởi tạo UserService
    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            LOGGER.info("EditUserServlet initialized successfully with UserService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize UserService in EditUserServlet: " + e.getMessage());
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
            out.println("<title>Servlet EditUserServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditUserServlet at " + request.getContextPath () + "</h1>");
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
        try {
            int userID = Integer.parseInt(request.getParameter("userID"));
            User user = userService.getUserById(userID);
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/views/admin/user_management/edit_user.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("message", "User not found.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/views/admin/user_management/edit_user.jsp");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Invalid user ID format.");
            request.getSession().setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/user_management/edit_user.jsp");
        }
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
        try {
            int userID = Integer.parseInt(request.getParameter("userID"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String role = request.getParameter("role");
            String className = request.getParameter("className");
            String school = request.getParameter("school");
            String phone = request.getParameter("phone");
            boolean status = "on".equals(request.getParameter("status"));

            User user = userService.getUserById(userID);
            if (user != null) {
                user.setFullName(fullName);
                user.setEmail(email);
                user.setRole(role);
                user.setClassName(className);
                user.setSchool(school);
                user.setPhone(phone);
                user.setStatus(status);

                boolean updated = userService.updateUser(user);
                if (updated) {
                    request.getSession().setAttribute("message", "User updated successfully!");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Failed to update user.");
                    request.getSession().setAttribute("messageType", "error");
                }
            } else {
                request.getSession().setAttribute("message", "User not found.");
                request.getSession().setAttribute("messageType", "error");
            }
            response.sendRedirect(request.getContextPath() + "/views/admin/user_management/edit_user.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("message", "An error occurred: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/user_management/edit_user.jsp");
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

    // Phương thức destroy() để dọn dẹp tài nguyên khi servlet bị hủy
    @Override
    public void destroy() {
        try {
            // Hiện tại không có tài nguyên cần dọn dẹp (UserService không giữ tài nguyên)
            // Nếu sau này có tài nguyên (như thread, file, hoặc kết nối), thêm logic dọn dẹp ở đây
            LOGGER.info("EditUserServlet destroyed successfully");
        } catch (Exception e) {
            LOGGER.severe("Error during EditUserServlet destruction: " + e.getMessage());
        }
    }
}