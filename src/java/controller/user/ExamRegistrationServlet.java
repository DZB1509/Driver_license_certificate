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
import model.entity.Course;
import model.entity.Exam;
import model.entity.User;
import model.service.CourseService;
import model.service.ExamService;
import model.service.NotificationService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class ExamRegistrationServlet extends HttpServlet {

    private ExamService examService = new ExamService();
    private CourseService courseService;
    private NotificationService notificationService = new NotificationService();
    private static final Logger LOGGER = Logger.getLogger(SystemConfigController.class.getName());

    public ExamRegistrationServlet() throws Exception {
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
            out.println("<title>Servlet ExamRegistrationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExamRegistrationServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false); // Không tạo session mới nếu không tồn tại
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"Student".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Students only");
            return;
        }

        try {
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int pageSize = 10; // Có thể config trong properties

            // Lấy danh sách kỳ thi khả dụng và đã đăng ký (giả lập vì ExamService chưa có)
            List<Exam> allFutureExams = examService.getFutureExamsByCourseId(0); // 0 để lấy tất cả
            List<Exam> availableExams = new ArrayList<>();
            List<Exam> registeredExams = new ArrayList<>();
            for (Exam exam : allFutureExams) {
                if (examService.canStudentRegisterForExam(currentUser.getUserID(), exam.getExamID())) {
                    availableExams.add(exam);
                } else {
                    registeredExams.add(exam);
                }
            }

            // Phân trang thủ công
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, availableExams.size());
            List<Exam> pagedAvailableExams = start < availableExams.size() ? availableExams.subList(start, end) : new ArrayList<>();
            end = Math.min(start + pageSize, registeredExams.size());
            List<Exam> pagedRegisteredExams = start < registeredExams.size() ? registeredExams.subList(start, end) : new ArrayList<>();

            List<Course> enrolledCourses = courseService.getCoursesByUserId(currentUser.getUserID());

            request.setAttribute("availableExams", pagedAvailableExams);
            request.setAttribute("registeredExams", pagedRegisteredExams);
            request.setAttribute("enrolledCourses", enrolledCourses);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalAvailable", availableExams.size());
            request.setAttribute("totalRegistered", registeredExams.size());
            request.setAttribute("pageSize", pageSize);

            request.getRequestDispatcher("/views/user/student/exam_registration.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading exam registration: " + e.getMessage());
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
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"Student".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("register".equals(action)) {
            // Register for an exam
            int examId = Integer.parseInt(request.getParameter("examId"));

            try {
                boolean registered = examService.registerStudentForExam(examId, currentUser.getUserID());

                if (registered) {
                    // Get exam details for notification
                    Exam exam = examService.getExamById(examId);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String examDate = sdf.format(exam.getDate());

                    // Send notification to the student
                    notificationService.sendExamRegistrationNotification(currentUser.getUserID(), examId, examDate);

                    request.setAttribute("successMessage", "Successfully registered for the exam.");
                } else {
                    request.setAttribute("errorMessage", "Failed to register for the exam. You may already be registered or the exam is full.");
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            }
        } else if ("cancel".equals(action)) {
            // Cancel registration for an exam
            int examId = Integer.parseInt(request.getParameter("examId"));

            try {
                boolean cancelled = examService.cancelExamRegistration(examId, currentUser.getUserID());

                if (cancelled) {
                    request.setAttribute("successMessage", "Successfully cancelled exam registration.");
                } else {
                    request.setAttribute("errorMessage", "Failed to cancel exam registration.");
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            }
        }

        // Redirect back to the exam registration page
        doGet(request, response);
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
