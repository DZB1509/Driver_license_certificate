///*
// * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nibh://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller.user;
//
//import controller.admin.SystemConfigController;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import model.entity.Certificate;
//import model.entity.Exam;
//import model.entity.Result;
//import model.entity.User;
//import model.service.CertificateService;
//import model.service.ExamService;
//import model.service.ResultService;
//import model.service.UserService;
//import java.util.ArrayList;
//import java.util.logging.Logger;
//
///**
// *
// * @author ADMIN
// */
//public class PoliceReportServlet extends HttpServlet {
//     private static final long serialVersionUID = 1L;
//    private static final Logger LOGGER = Logger.getLogger(SystemConfigController.class.getName());
//
//    private ExamService examService = new ExamService();
//    private ResultService resultService = new ResultService();
//    private CertificateService certificateService = new CertificateService();
//    private UserService userService = new UserService();
//    @Override
//    public void init() throws ServletException {
//        try {
//            LOGGER.info("CertificateApprovalServlet initialized successfully");
//            // Future initialization code (e.g., service setup) can go here
//        } catch (Exception e) {
//            LOGGER.severe("Failed to initialize CertificateApprovalServlet: " + e.getMessage());
//            throw new ServletException("Failed to initialize CertificateApprovalServlet", e);
//        }
//    }
//
//    /** 
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet PoliceReportServlet</title>");  
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PoliceReportServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    } 
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /** 
//     * Handles the HTTP <code>GET</code> method.
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//      
//        response.setContentType("text/html; charset=UTF-8");
//        //processRequest(request, response);
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        User currentUser = (User) session.getAttribute("user");
//        if (!"TrafficPolice".equals(currentUser.getRole())) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: TrafficPolice only");
//            return;
//        }
//
//        try {
//            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
//            int pageSize = 10; // Có thể config trong properties
//
//            String reportType = request.getParameter("type");
//            if (reportType == null) {
//                reportType = "summary"; // Báo cáo tổng quan mặc định
//            }
//
//            switch (reportType) {
//                case "exams":
//                    generateExamReport(request, page, pageSize);
//                    break;
//                case "certificates":
//                    generateCertificateReport(request, page, pageSize);
//                    break;
//                case "students":
//                    generateStudentReport(request, page, pageSize);
//                    break;
//                case "summary":
//                default:
//                    generateSummaryReport(request);
//                    break;
//            }
//
//            request.setAttribute("reportType", reportType);
//            request.setAttribute("currentPage", page);
//            request.setAttribute("pageSize", pageSize);
//            request.getRequestDispatcher("/views/user/police/reports.jsp").forward(request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating report: " + e.getMessage());
//        }
//    } 
//
//    /** 
//     * Handles the HTTP <code>POST</code> method.
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//         request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=UTF-8");
//        //processRequest(request, response);
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        User currentUser = (User) session.getAttribute("user");
//        if (!"TrafficPolice".equals(currentUser.getRole())) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: TrafficPolice only");
//            return;
//        }
//
//        try {
//            String action = request.getParameter("action");
//            if ("export".equals(action)) {
//                String reportType = request.getParameter("reportType");
//                String format = request.getParameter("format"); // pdf hoặc excel
//                if (reportType == null || format == null) {
//                    throw new IllegalArgumentException("Report type and format are required.");
//                }
//
//                // Placeholder cho chức năng xuất file (cần thư viện như Apache POI hoặc iText)
//                response.setContentType("application/octet-stream");
//                response.setHeader("Content-Disposition", "attachment; filename=police_report_" + reportType + "." + format);
//
//                // TODO: Thực hiện logic xuất file thực tế
//                try (PrintWriter out = response.getWriter()) {
//                    out.println("Exporting " + reportType + " report in " + format + " format. (Placeholder)");
//                }
//
//                response.sendRedirect(request.getContextPath() + "/police/reports?type=" + reportType);
//            } else {
//                doGet(request, response);
//            }
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Error exporting report: " + e.getMessage());
//            doGet(request, response);
//        }
//    }
//
//    /** 
//     * Tạo báo cáo tổng quan
//     */
//    private void generateSummaryReport(HttpServletRequest request) {
//        try {
//            Map<String, Integer> examStatusCount = new HashMap<>();
//            examStatusCount.put("Pending", examService.countExamsByStatus("Pending"));
//            examStatusCount.put("Approved", examService.countExamsByStatus("Approved"));
//            examStatusCount.put("In Progress", examService.countExamsByStatus("In Progress"));
//            examStatusCount.put("Completed", examService.countExamsByStatus("Completed"));
//            examStatusCount.put("Cancelled", examService.countExamsByStatus("Cancelled"));
//
//            int totalCertificates = certificateService.countAllCertificates();
//            int certificatesThisMonth = certificateService.countCertificatesIssuedThisMonth();
//            int certificatesLastMonth = certificateService.countCertificatesIssuedLastMonth();
//
//            int totalResults = resultService.countAllResults();
//            int passResults = resultService.countPassResults();
//            double passRate = totalResults > 0 ? (double) passResults / totalResults * 100 : 0;
//
//            request.setAttribute("examStatusCount", examStatusCount);
//            request.setAttribute("totalCertificates", totalCertificates);
//            request.setAttribute("certificatesThisMonth", certificatesThisMonth);
//            request.setAttribute("certificatesLastMonth", certificatesLastMonth);
//            request.setAttribute("passRate", passRate);
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Error generating summary report: " + e.getMessage());
//        }
//    }
//
//    private void generateExamReport(HttpServletRequest request, int page, int pageSize) {
//        try {
//            String startDateStr = request.getParameter("startDate");
//            String endDateStr = request.getParameter("endDate");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setLenient(false);
//
//            List<Exam> allExams;
//            if (startDateStr != null && endDateStr != null && !startDateStr.isEmpty() && !endDateStr.isEmpty()) {
//                try {
//                    java.util.Date startUtilDate = dateFormat.parse(startDateStr);
//                    java.util.Date endUtilDate = dateFormat.parse(endDateStr);
//                    java.sql.Date startDate = new java.sql.Date(startUtilDate.getTime());
//                    java.sql.Date endDate = new java.sql.Date(endUtilDate.getTime());
//                    allExams = examService.getExamsByDateRange(startDate, endDate);
//                } catch (Exception e) {
//                    allExams = examService.getAllExams();
//                    request.setAttribute("errorMessage", "Invalid date format. Using all exams: " + e.getMessage());
//                }
//            } else {
//                allExams = examService.getAllExams();
//            }
//
//            int start = (page - 1) * pageSize;
//            int end = Math.min(start + pageSize, allExams.size());
//            List<Exam> pagedExams = start < allExams.size() ? allExams.subList(start, end) : new ArrayList<>();
//
//            Map<Integer, Integer> examParticipants = new HashMap<>();
//            for (Exam exam : pagedExams) {
//                int examId = exam.getExamID();
//                int count = resultService.countResultsByExamId(examId);
//                examParticipants.put(examId, count);
//            }
//
//            request.setAttribute("exams", pagedExams);
//            request.setAttribute("examParticipants", examParticipants);
//            request.setAttribute("totalExams", allExams.size());
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Error generating exam report: " + e.getMessage());
//        }
//    }
//
//    private void generateCertificateReport(HttpServletRequest request, int page, int pageSize) {
//        try {
//            String startDate = request.getParameter("startDate");
//            String endDate = request.getParameter("endDate");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setLenient(false);
//
//            List<Certificate> allCertificates;
//            if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
//                try {
//                    dateFormat.parse(startDate);
//                    dateFormat.parse(endDate);
//                    allCertificates = certificateService.getCertificatesByDateRange(startDate, endDate);
//                } catch (Exception e) {
//                    allCertificates = certificateService.getAllCertificates(1, 10); // Default pagination
//                    request.setAttribute("errorMessage", "Invalid date format. Using default certificates.");
//                }
//            } else {
//                allCertificates = certificateService.getAllCertificates(1, 10); // Default pagination
//            }
//
//            int start = (page - 1) * pageSize;
//            int end = Math.min(start + pageSize, allCertificates.size());
//            List<Certificate> pagedCertificates = start < allCertificates.size() ? allCertificates.subList(start, end) : new ArrayList<>();
//
//            Map<String, Integer> schoolCertificates = new HashMap<>();
//            for (Certificate cert : pagedCertificates) {
//                User student = userService.getUserById(cert.getUserID());
//                String school = (student != null) ? student.getSchool() : "Unknown";
//                schoolCertificates.put(school, schoolCertificates.getOrDefault(school, 0) + 1);
//            }
//
//            request.setAttribute("certificates", pagedCertificates);
//            request.setAttribute("schoolCertificates", schoolCertificates);
//            request.setAttribute("totalCertificates", allCertificates.size());
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Error generating certificate report: " + e.getMessage());
//        }
//    }
//
//    private void generateStudentReport(HttpServletRequest request, int page, int pageSize) {
//        try {
//            List<User> allStudents = userService.getUsersByRole("Student");
//            int start = (page - 1) * pageSize;
//            int end = Math.min(start + pageSize, allStudents.size());
//            List<User> pagedStudents = start < allStudents.size() ? allStudents.subList(start, end) : new ArrayList<>();
//
//            Map<Integer, Boolean> studentCertStatus = new HashMap<>();
//            for (User student : pagedStudents) {
//                boolean hasCertificate = certificateService.userHasValidCertificate(student.getUserID());
//                studentCertStatus.put(student.getUserID(), hasCertificate);
//            }
//
//            Map<String, Integer> schoolStudents = new HashMap<>();
//            for (User student : pagedStudents) {
//                String school = student.getSchool();
//                schoolStudents.put(school, schoolStudents.getOrDefault(school, 0) + 1);
//            }
//
//            request.setAttribute("students", pagedStudents);
//            request.setAttribute("studentCertStatus", studentCertStatus);
//            request.setAttribute("schoolStudents", schoolStudents);
//            request.setAttribute("totalStudents", allStudents.size());
//        } catch (Exception e) {
//            request.setAttribute("errorMessage", "Error generating student report: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Servlet for generating police reports";
//    }// </editor-fold>
//    @Override
//    public void destroy() {
//        try {
//            // Hiện tại không có tài nguyên cần dọn dẹp
//            // Nếu sau này có tài nguyên (như thread, file, hoặc kết nối), thêm logic dọn dẹp ở đây
//            LOGGER.info("SystemConfigController destroyed successfully");
//        } catch (Exception e) {
//            LOGGER.severe("Error during SystemConfigController destruction: " + e.getMessage());
//        }
//    }
//}