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
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import model.exception.EmailAlreadyExistsException;
import model.service.UserService;
import java.util.logging.Logger;
import model.entity.Admin;

/**
 *
 * @author ADMIN
 */
public class CreateUserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreateUserServlet.class.getName());
    private UserService userService;

    // Phương thức init() để khởi tạo UserService
    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            LOGGER.info("CreateUserServlet initialized successfully with UserService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize UserService in CreateUserServlet: " + e.getMessage());
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
            out.println("<title>Servlet CreateUserServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateUserServlet at " + request.getContextPath () + "</h1>");
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
        // Hiển thị form tạo người dùng
        request.getRequestDispatcher("/views/admin/user_management/create_user.jsp").forward(request, response);
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
        // Kiểm tra admin đang đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        // Lấy AdminID của admin đang đăng nhập
        Admin loggedAdmin = (Admin) session.getAttribute("admin");
        int adminId = loggedAdmin.getAdminID();
        if (adminId <= 0) {
            request.setAttribute("error", "Không thể xác định admin đang đăng nhập!");
            request.getRequestDispatcher("/views/admin/user_management/create_user.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy dữ liệu từ form
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String className = request.getParameter("className");
            String school = request.getParameter("school");
            String phone = request.getParameter("phone");
            boolean status = "on".equals(request.getParameter("status"));

            // Kiểm tra dữ liệu đầu vào
            if (fullName == null || fullName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    role == null || role.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập đầy đủ các thông tin bắt buộc!");
                request.getRequestDispatcher("/views/admin/user_management/create_user.jsp").forward(request, response);
                return;
            }

            // Tạo đối tượng User
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password); // Mật khẩu sẽ được mã hóa trong UserService
            user.setRole(role);
            user.setClassName(className);
            user.setSchool(school);
            user.setPhone(phone);
            user.setCreatedBy(adminId); // Gán CreatedBy với AdminID của admin đang đăng nhập
            user.setStatus(status);

            // Gọi service để tạo người dùng
            boolean created = userService.createUser(user);

            if (created) {
                request.getSession().setAttribute("message", "User created successfully!");
                request.getSession().setAttribute("messageType", "success");
                response.sendRedirect(request.getContextPath() + "/views/admin/user_management/create_user.jsp");
            } else {
                request.setAttribute("error", "Failed to create user.");
                request.getRequestDispatcher("/views/admin/user_management/create_user.jsp").forward(request, response);
            }
        } catch (EmailAlreadyExistsException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/admin/user_management/create_user.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/user_management/create_user.jsp").forward(request, response);
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
            LOGGER.info("CreateUserServlet destroyed successfully");
        } catch (Exception e) {
            LOGGER.severe("Error during CreateUserServlet destruction: " + e.getMessage());
        }
    }
}