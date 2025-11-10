/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import controller.admin.SystemConfigController;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import model.entity.Course;
import model.entity.Registration;
import model.service.CourseService;
import model.service.RegistrationService;

/**
 *
 * @author ADMIN
 */
public class CourseRegistrationServlet extends HttpServlet {

    private CourseService courseService;
    private RegistrationService registrationService = new RegistrationService();
    private static final Logger LOGGER = Logger.getLogger(SystemConfigController.class.getName());

    public CourseRegistrationServlet() throws Exception {
        this.courseService = new CourseService();
    }

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
            out.println("<title>Servlet CourseRegistrationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CourseRegistrationServlet at " + request.getContextPath() + "</h1>");
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
                // Lấy userId từ session (giả sử đã đăng nhập)
                Integer userIdObj = (Integer) request.getSession().getAttribute("userId");
                if (userIdObj == null) {
                    throw new ServletException("User ID not found in session. Please log in.");
                }
                int userId = userIdObj;

                ArrayList<Registration> registrations = registrationService.getUserRegistrations(userId);
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
                request.setAttribute("registrations", registrations);
                request.setAttribute("courses", courses);
                request.getRequestDispatcher("views/student/course_registration.jsp").forward(request, response);
            }
        } catch (Exception e) {
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        //processRequest(request, response);
        String action = request.getParameter("action");

        try {
            if (action.equals("register")) {
                // Lấy userId từ session
                Integer userIdObj = (Integer) request.getSession().getAttribute("userId");
                if (userIdObj == null) {
                    throw new ServletException("User ID not found in session. Please log in.");
                }
                int userId = userIdObj;

                int courseId = Integer.parseInt(request.getParameter("courseId"));
                String comments = request.getParameter("comments");

                Registration registration = new Registration();
                registration.setUserID(userId);
                registration.setCourseID(courseId);
                registration.setStatus("Pending");
                registration.setComments(comments != null ? comments : "");

                registrationService.registerCourse(registration);
                response.sendRedirect("register?action=list");
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
