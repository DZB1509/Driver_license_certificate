/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import controller.admin.SystemConfigController;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.entity.Certificate;
import model.entity.Exam;
import model.entity.User;
import model.service.CertificateService;
import model.service.ExamService;
import model.service.ResultService;
import model.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class TeacherReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(SystemConfigController.class.getName());
    private ExamService examService = new ExamService();
    private ResultService resultService = new ResultService();
    private CertificateService certificateService = new CertificateService();
    private UserService userService = new UserService();

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
            out.println("<title>Servlet TeacherReportServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TeacherReportServlet at " + request.getContextPath() + "</h1>");
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

            String reportType = request.getParameter("type");
            if (reportType == null) {
                reportType = "summary"; // Báo cáo tổng quan mặc định
            }

            switch (reportType) {
                case "exams":
                    generateExamReport(request, page, pageSize, currentUser);
                    break;
                case "students":
                    generateStudentReport(request, page, pageSize, currentUser);
                    break;
                case "summary":
                default:
                    generateSummaryReport(request, currentUser);
                    break;
            }

            request.setAttribute("reportType", reportType);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.getRequestDispatcher("/views/user/teacher/reports.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating report: " + e.getMessage());
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

        try {
            String action = request.getParameter("action");
            if ("export".equals(action)) {
                String reportType = request.getParameter("reportType");
                String format = request.getParameter("format"); // pdf hoặc excel
                if (reportType == null || format == null) {
                    throw new IllegalArgumentException("Report type and format are required.");
                }

                // Placeholder cho chức năng xuất file (cần thư viện như Apache POI hoặc iText)
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=teacher_report_" + reportType + "." + format);

                try (PrintWriter out = response.getWriter()) {
                    out.println("Exporting " + reportType + " report in " + format + " format. (Placeholder)");
                }

                response.sendRedirect(request.getContextPath() + "/teacher/reports?type=" + reportType);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error exporting report: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void generateSummaryReport(HttpServletRequest request, User currentUser) {
        try {
            // Số lượng kỳ thi của giáo viên này
            Map<String, Integer> examStatusCount = new HashMap<>();
            String[] statuses = {"Pending", "Approved", "In Progress", "Completed", "Cancelled"};
            for (String status : statuses) {
                examStatusCount.put(status, examService.countExamsByStatusAndSupervisor(status, currentUser.getUserID()));
            }

            // Tỷ lệ đậu/rớt của học viên trong các kỳ thi của giáo viên
            int totalResults = resultService.countResultsBySupervisor(currentUser.getUserID());
            int passResults = resultService.countPassResultsBySupervisor(currentUser.getUserID());
            double passRate = totalResults > 0 ? (double) passResults / totalResults * 100 : 0;

            // Số chứng chỉ đã cấp trong các kỳ thi của giáo viên
            int totalCertificates = certificateService.countCertificatesBySupervisor(currentUser.getUserID());

            request.setAttribute("examStatusCount", examStatusCount);
            request.setAttribute("totalCertificates", totalCertificates);
            request.setAttribute("passRate", passRate);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error generating summary report: " + e.getMessage());
        }
    }

    private void generateExamReport(HttpServletRequest request, int page, int pageSize, User currentUser) {
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            List<Exam> allExams;
            if (startDateStr != null && endDateStr != null && !startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                try {
                    java.util.Date startUtilDate = dateFormat.parse(startDateStr);
                    java.util.Date endUtilDate = dateFormat.parse(endDateStr);
                    java.sql.Date startDate = new java.sql.Date(startUtilDate.getTime());
                    java.sql.Date endDate = new java.sql.Date(endUtilDate.getTime());
                    allExams = examService.getExamsByDateRangeAndSupervisor(startDate, endDate, currentUser.getUserID());
                } catch (Exception e) {
                    allExams = examService.getExamsBySupervisorId(currentUser.getUserID());
                    request.setAttribute("errorMessage", "Invalid date format. Using all exams for this teacher: " + e.getMessage());
                }
            } else {
                allExams = examService.getExamsBySupervisorId(currentUser.getUserID());
            }

            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, allExams.size());
            List<Exam> pagedExams = start < allExams.size() ? allExams.subList(start, end) : new ArrayList<>();

            Map<Integer, Integer> examParticipants = new HashMap<>();
            for (Exam exam : pagedExams) {
                int examId = exam.getExamID();
                int count = resultService.countResultsByExamId(examId);
                examParticipants.put(examId, count);
            }

            request.setAttribute("exams", pagedExams);
            request.setAttribute("examParticipants", examParticipants);
            request.setAttribute("totalExams", allExams.size());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error generating exam report: " + e.getMessage());
        }
    }

    private void generateStudentReport(HttpServletRequest request, int page, int pageSize, User currentUser) {
        try {
            List<User> allStudents = userService.getStudentsBySupervisor(currentUser.getUserID());
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, allStudents.size());
            List<User> pagedStudents = start < allStudents.size() ? allStudents.subList(start, end) : new ArrayList<>();

            Map<Integer, Boolean> studentCertStatus = new HashMap<>();
            for (User student : pagedStudents) {
                boolean hasCertificate = certificateService.userHasValidCertificate(student.getUserID());
                studentCertStatus.put(student.getUserID(), hasCertificate);
            }

            Map<String, Integer> schoolStudents = new HashMap<>();
            for (User student : pagedStudents) {
                String school = student.getSchool();
                schoolStudents.put(school, schoolStudents.getOrDefault(school, 0) + 1);
            }

            request.setAttribute("students", pagedStudents);
            request.setAttribute("studentCertStatus", studentCertStatus);
            request.setAttribute("schoolStudents", schoolStudents);
            request.setAttribute("totalStudents", allStudents.size());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error generating student report: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for generating teacher reports";
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
