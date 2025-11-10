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
import java.time.LocalDate;
import model.entity.User;
import model.service.CertificateService;
import model.service.ExamService;
import model.service.ViolationService;

/**
 *
 * @author ADMIN
 */
public class PoliceDashboardServlet extends HttpServlet {
    private CertificateService certificateService;
    private ExamService examService;
    private ViolationService violationService;

    @Override
    public void init() throws ServletException {
        certificateService = new CertificateService();
        examService = new ExamService();
        violationService = new ViolationService();
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
            out.println("<title>Servlet PoliceDashboardServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PoliceDashboardServlet at " + request.getContextPath () + "</h1>");
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
        // Lấy thông tin người dùng từ session
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        if (loggedUser == null || !"TrafficPolice".equals(loggedUser.getRole())) {
            request.setAttribute("message", "Bạn cần đăng nhập với vai trò cảnh sát giao thông để truy cập dashboard.");
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/views/common/login.jsp").forward(request, response);
            return;
        }

        int pendingCertificates = certificateService.getPendingCertificatesCount();
        int upcomingExams = examService.getUpcomingExamsCount(java.sql.Date.valueOf(LocalDate.now()));
        int totalViolations = violationService.getTotalViolations(); // Thêm dòng này
        request.setAttribute("pendingCertificates", pendingCertificates);
        request.setAttribute("upcomingExams", upcomingExams);
        request.setAttribute("totalViolations", totalViolations); // Gán attribute cho dashboard
        request.setAttribute("recentCertificates", certificateService.getRecentCertificates());
        request.setAttribute("upcomingExamsList", examService.getUpcomingExams(java.sql.Date.valueOf(LocalDate.now()), 1, 5));

        request.getRequestDispatcher("/views/user/trafficpolice/dashboard.jsp").forward(request, response);
    
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
        processRequest(request, response);
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
