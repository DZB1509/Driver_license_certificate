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
import jakarta.servlet.http.HttpSession;
import model.entity.Result;
import model.entity.User;
import model.service.ExamService;
import model.service.ResultService;
import java.util.List;
import jakarta.servlet.ServletContext;
import java.util.ArrayList;
import java.util.logging.Logger;
import model.entity.Exam;

/**
 *
 * @author ADMIN
 */
public class ViewResultServlet extends HttpServlet {

    private ResultService resultService = new ResultService();
    private ExamService examService = new ExamService();
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
            out.println("<title>Servlet ViewResultServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewResultServlet at " + request.getContextPath() + "</h1>");
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
        if (!"Student".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Students only");
            return;
        }

        try {
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int pageSize = 10; // Có thể config trong properties

            // Get exam results for the student with pagination
            List<Result> allResults = resultService.getResultsByUserId(currentUser.getUserID());
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, allResults.size());
            List<Result> pagedResults = start < allResults.size() ? allResults.subList(start, end) : new ArrayList<>();

            // Get completed exams and calculate pending results
            List<Exam> completedExams = examService.getCompletedExams();
            int completedExamCount = 0;
            for (Exam exam : completedExams) {
                if (resultService.getResultByUserAndExam(currentUser.getUserID(), exam.getExamID()) != null) {
                    completedExamCount++;
                }
            }
            int pendingResults = completedExams.size() - completedExamCount;

            // Calculate statistics
            int passedCount = 0;
            int failedCount = 0;
            double totalScore = 0;
            for (Result result : allResults) {
                if (result.isPassStatus()) {
                    passedCount++;
                } else {
                    failedCount++;
                }
                totalScore += result.getScore();
            }
            double averageScore = allResults.isEmpty() ? 0 : totalScore / allResults.size();

            request.setAttribute("results", pagedResults);
            request.setAttribute("pendingResults", pendingResults);
            request.setAttribute("passedCount", passedCount);
            request.setAttribute("failedCount", failedCount);
            request.setAttribute("averageScore", averageScore);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalResults", allResults.size());
            request.setAttribute("pageSize", pageSize);

            request.getRequestDispatcher("/views/user/student/results.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading results: " + e.getMessage());
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
        if (!"Student".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Students only");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("filter".equals(action)) {
                String filterType = request.getParameter("filterType");
                if (filterType == null || filterType.trim().isEmpty()) {
                    filterType = "all"; // Mặc định hiển thị tất cả nếu filterType không hợp lệ
                }

                List<Result> filteredResults;
                switch (filterType.toLowerCase()) {
                    case "passed":
                        filteredResults = resultService.getPassedResultsByUserId(currentUser.getUserID());
                        break;
                    case "failed":
                        filteredResults = resultService.getFailedResultsByUserId(currentUser.getUserID());
                        break;
                    default:
                        filteredResults = resultService.getResultsByUserId(currentUser.getUserID());
                        break;
                }

                // Tính toán thống kê
                int passedCount = 0;
                int failedCount = 0;
                double totalScore = 0;
                for (Result result : filteredResults) {
                    if (result.isPassStatus()) {
                        passedCount++;
                    } else {
                        failedCount++;
                    }
                    totalScore += result.getScore();
                }
                double averageScore = filteredResults.isEmpty() ? 0 : totalScore / filteredResults.size();

                // Đặt thuộc tính cho JSP
                request.setAttribute("results", filteredResults);
                request.setAttribute("filterType", filterType);
                request.setAttribute("passedCount", passedCount);
                request.setAttribute("failedCount", failedCount);
                request.setAttribute("averageScore", averageScore);

                // Forward về JSP
                request.getRequestDispatcher("/views/user/student/results.jsp").forward(request, response);
            } else {
                // Nếu action không hợp lệ, quay lại doGet
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error filtering results: " + e.getMessage());
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
