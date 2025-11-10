/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nibh://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import controller.admin.SystemConfigController;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import model.entity.Course;
import model.entity.User;
import model.service.CourseService;

/**
 *
 * @author ADMIN
 */
public class CourseManagementServlet extends HttpServlet {

    private CourseService courseService;
    private static final Logger LOGGER = Logger.getLogger(SystemConfigController.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            LOGGER.info("CertificateApprovalServlet initialized successfully");
            // Future initialization code (e.g., service setup) can go here
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize CertificateApprovalServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize CertificateApprovalServlet", e);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
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
            out.println("<title>Servlet CourseManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CourseManagementServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        //processRequest(request, response);
        String action = request.getParameter("action");

        try {
           if (action == null || action.equals("list")) {
                int page = 1;
                int pageSize = 10;
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                }
                String pageSizeParam = request.getParameter("pageSize");
                if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                    pageSize = Integer.parseInt(pageSizeParam);
                }
                List<Course> courses = courseService.getAllCourses(page, pageSize);
                request.setAttribute("courses", courses);
                request.getRequestDispatcher("views/student/course_list.jsp").forward(request, response);
            } 
            // Thêm logic cho teacher dashboard
            else if (action.equals("teacherDashboard")) {
                HttpSession session = request.getSession();
                User loggedUser = (User) session.getAttribute("loggedUser");
                if (loggedUser == null || !"Teacher".equals(loggedUser.getRole())) {
                    throw new ServletException("Quyền truy cập bị từ chối. Người dùng chưa đăng nhập hoặc không phải giáo viên.");
                }
                int teacherID = loggedUser.getUserID();
                List<Course> teachingCoursesList = courseService.getCoursesByTeacherID(teacherID);
                request.setAttribute("teachingCoursesList", teachingCoursesList);
                request.getRequestDispatcher("/views/user/teacher/dashboard.jsp").forward(request, response);
            } 
            else if (action.equals("edit")) {
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                Course course = courseService.getCourseById(courseId);
                request.setAttribute("course", course);
                request.getRequestDispatcher("views/teacher/editCourse.jsp").forward(request, response);
            } 
            else if (action.equals("delete")) {
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                courseService.deleteCourse(courseId);
                response.sendRedirect("CourseManagementServlet?action=list");
            } 
            else if (action.equals("view")) {
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                Course course = courseService.getCourseById(courseId);
                request.setAttribute("course", course);
                request.getRequestDispatcher("views/student/View_course.jsp").forward(request, response);
            }} catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("views/common/error.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        //processRequest(request, response);
        String action = request.getParameter("action");

        try {
            // Comment hoặc xóa dòng kiểm tra vai trò
            // HttpSession session = request.getSession();
            // String role = (String) session.getAttribute("role");
            // if (!"teacher".equals(role)) {
            //     throw new ServletException("Access denied. Only teachers can create or edit courses.");
            // }
            if (action.equals("create")) {
                Course course = new Course();
                course.setCourseName(request.getParameter("courseName"));
                course.setTeacherID(Integer.parseInt(request.getParameter("teacherId")));
                course.setStartDate(Date.valueOf(request.getParameter("startDate")));
                course.setEndDate(Date.valueOf(request.getParameter("endDate")));
                course.setDescription(request.getParameter("description"));
                course.setStatus(request.getParameter("status"));

                courseService.createCourse(course);
                response.sendRedirect("courses?action=list");

            } else if (action.equals("update")) {
                Course course = new Course();
                course.setCourseID(Integer.parseInt(request.getParameter("courseId")));
                course.setCourseName(request.getParameter("courseName"));
                course.setTeacherID(Integer.parseInt(request.getParameter("teacherId")));
                course.setStartDate(Date.valueOf(request.getParameter("startDate")));
                course.setEndDate(Date.valueOf(request.getParameter("endDate")));
                course.setDescription(request.getParameter("description"));
                course.setStatus(request.getParameter("status"));

                courseService.updateCourse(course);
                response.sendRedirect("courses?action=list");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("views/common/error.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

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
}
