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
import model.entity.Exam;
import model.entity.Result;
import model.entity.User;
import model.service.CertificateService;
import model.service.ExamService;
import model.service.NotificationService;
import model.service.ResultService;
import model.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class GradeManagementServlet extends HttpServlet {

    private ExamService examService = new ExamService();
    private ResultService resultService = new ResultService();
    private UserService userService = new UserService();
    private NotificationService notificationService = new NotificationService();
    private CertificateService certificateService = new CertificateService();
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
            out.println("<title>Servlet GradeManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GradeManagementServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"Teacher".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Teachers only");
            return;
        }

        try {
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int pageSize = 10; // Có thể config trong properties

            // Get examId from request parameter if available
            String examIdParam = request.getParameter("examId");
            if (examIdParam != null && !examIdParam.isEmpty()) {
                int examId = Integer.parseInt(examIdParam);
                Exam exam = examService.getExamById(examId);

                if (exam != null && exam.getSupervisorID() == currentUser.getUserID()) {
                    // Get list of students registered for this exam (giả định từ Registrations qua Exam)
                    List<User> allStudents = userService.getStudentsByExamId(examId); // Thêm vào UserService
                    int start = (page - 1) * pageSize;
                    int end = Math.min(start + pageSize, allStudents.size());
                    List<User> pagedStudents = start < allStudents.size() ? allStudents.subList(start, end) : new ArrayList<>();

                    // Get existing results for this exam
                    List<Result> allResults = resultService.getResultsByExamId(examId);
                    List<Result> pagedResults = start < allResults.size() ? allResults.subList(start, end) : new ArrayList<>();

                    request.setAttribute("exam", exam);
                    request.setAttribute("students", pagedStudents);
                    request.setAttribute("results", pagedResults);
                    request.setAttribute("totalStudents", allStudents.size());
                } else {
                    request.setAttribute("errorMessage", "Exam not found or you are not authorized.");
                }
            }

            // Get list of exams for this teacher
            List<Exam> allTeacherExams = examService.getExamsBySupervisorId(currentUser.getUserID()); // Sửa từ getExamsByTeacherId
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, allTeacherExams.size());
            List<Exam> pagedTeacherExams = start < allTeacherExams.size() ? allTeacherExams.subList(start, end) : new ArrayList<>();

            request.setAttribute("teacherExams", pagedTeacherExams);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalExams", allTeacherExams.size());

            request.getRequestDispatcher("/views/user/teacher/grade_input.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading grade management: " + e.getMessage());
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"Teacher".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Teachers only");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("submitGrades".equals(action)) {
                int examId = Integer.parseInt(request.getParameter("examId"));
                Exam exam = examService.getExamById(examId);
                if (exam == null || exam.getSupervisorID() != currentUser.getUserID()) {
                    request.setAttribute("errorMessage", "Invalid exam or unauthorized access.");
                    doGet(request, response);
                    return;
                }

                String[] studentIds = request.getParameterValues("studentId");
                String[] scores = request.getParameterValues("score");

                if (studentIds != null && scores != null && studentIds.length == scores.length) {
                    for (int i = 0; i < studentIds.length; i++) {
                        try {
                            int studentId = Integer.parseInt(studentIds[i]);
                            double score = Double.parseDouble(scores[i]);
                            boolean passed = score >= 70.0;

                            // Kiểm tra nếu result đã tồn tại
                            Result existingResult = resultService.getResultByUserAndExam(studentId, examId);
                            Result result = new Result();
                            result.setExamID(examId);
                            result.setUserID(studentId);
                            result.setScore(score);
                            result.setPassStatus(passed);

                            boolean success;
                            if (existingResult == null) {
                                success = resultService.createResult(result);
                            } else {
                                result.setResultID(existingResult.getResultID());
                                success = resultService.updateResult(result);
                            }

                            if (success) {
                                // Gửi thông báo cho student
                                notificationService.sendNotification(
                                        studentId,
                                        "Your result for Exam ID " + examId + ": " + score + " (" + (passed ? "Passed" : "Failed") + ")"
                                );

                                // Nếu passed, cấp chứng chỉ (logic đã có trong ResultService.createResult/updateResult)
                            }
                        } catch (NumberFormatException e) {
                            request.setAttribute("errorMessage", "Invalid student ID or score at index " + i);
                            doGet(request, response);
                            return;
                        }
                    }

                    // Cập nhật trạng thái kỳ thi
                    examService.updateExamStatus(examId, "Completed");
                    request.setAttribute("successMessage", "Grades submitted successfully.");
                } else {
                    request.setAttribute("errorMessage", "Invalid data submitted.");
                }
            }

            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error submitting grades: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for managing grades by teachers";
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
