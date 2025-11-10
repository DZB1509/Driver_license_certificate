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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import model.entity.Course;
import model.entity.User;
import model.service.CourseService;

/**
 *
 * @author ADMIN
 */
public class CreateCourseServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreateCourseServlet.class.getName());
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        try {
            courseService = new CourseService();
            LOGGER.info("CreateCourseServlet initialized successfully with courseService");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize CourseService: " + e.getMessage());
            throw new ServletException("Failed to initialize CourseService", e);
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
            out.println("<title>Servlet CreateCourseServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateCourseServlet at " + request.getContextPath () + "</h1>");
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

        User currentUser = (User) session.getAttribute("loggedUser");
        String role = currentUser.getRole();
        if (role == null || !role.equalsIgnoreCase("teacher")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Teachers only");
            return;
        }

        // Forward đến create_course.jsp để hiển thị form
        request.getRequestDispatcher("/views/user/teacher/create_course.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        try {
            // Lấy loggedUser từ session
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                session.setAttribute("message", "Vui lòng đăng nhập để tạo khóa học!");
                session.setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Lấy teacherID từ loggedUser
            int teacherID = loggedUser.getUserID();
            String role = loggedUser.getRole();

            // Kiểm tra vai trò (chỉ giáo viên được tạo khóa học)
            if (!"teacher".equalsIgnoreCase(role)) {
                session.setAttribute("message", "Chỉ giáo viên mới có quyền tạo khóa học!");
                session.setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/UserdashboardServlet=" + role);
                return;
            }

            // Lấy thông tin từ form
            String courseName = request.getParameter("courseName");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String description = request.getParameter("description");
            String status = request.getParameter("status");

            // Chuyển đổi ngày từ String sang java.util.Date, sau đó sang java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedStartDate = dateFormat.parse(startDateStr);
            java.util.Date parsedEndDate = dateFormat.parse(endDateStr);

            // Chuyển từ java.util.Date sang java.sql.Date
            Date startDate = new Date(parsedStartDate.getTime());
            Date endDate = new Date(parsedEndDate.getTime());

            // Tạo đối tượng Course
            Course course = new Course();
            course.setCourseName(courseName);
            course.setTeacherID(teacherID);
            course.setStartDate(startDate);
            course.setEndDate(endDate);
            course.setDescription(description);
            course.setStatus(status);

            // Gọi CourseService để tạo khóa học
            courseService.createCourse(course);

            // Thiết lập thông báo thành công
            session.setAttribute("message", "Tạo khóa học thành công!");
            session.setAttribute("messageType", "success");
        } catch (Exception e) {
            // Thiết lập thông báo lỗi
            session.setAttribute("message", "Lỗi khi tạo khóa học: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        // Chuyển hướng về dashboard
        response.sendRedirect(request.getContextPath() + "/UserdashboardServlet?role=teacher");
    }
    
    @Override
    public void destroy() {
        try {
            // Hiện tại không có tài nguyên cần dọn dẹp
            // Nếu sau này có tài nguyên (như thread, file, hoặc kết nối), thêm logic dọn dẹp ở đây
            LOGGER.info("SystemConfigController destroyed successfully");
        } catch (Exception e) {
            LOGGER.severe("Error during SystemConfigController destruction: " + e.getMessage());
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
