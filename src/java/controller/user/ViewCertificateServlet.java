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
import model.entity.Certificate;
import model.entity.User;
import model.service.CertificateService;
import model.service.NotificationService;
import jakarta.servlet.ServletContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class ViewCertificateServlet extends HttpServlet {

    private CertificateService certificateService = new CertificateService();
    private NotificationService notificationService = new NotificationService();
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
            out.println("<title>Servlet ViewCertificateServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewCertificateServlet at " + request.getContextPath() + "</h1>");
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
            int pageSize = 5; // Có thể config trong properties

            // Get all certificates for the student
            List<Certificate> allCertificates = certificateService.getCertificatesByUserId(currentUser.getUserID());
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, allCertificates.size());
            List<Certificate> pagedCertificates = start < allCertificates.size() ? allCertificates.subList(start, end) : new ArrayList<>();

            LocalDate currentDate = LocalDate.now();
            List<String> certificateStatuses = new ArrayList<>();

            // Check certificate validity
            for (Certificate certificate : pagedCertificates) {
                LocalDate expirationDate = certificate.getExpirationDate().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                if (currentDate.isAfter(expirationDate)) {
                    certificateStatuses.add("Expired");
                } else if (currentDate.plusMonths(3).isAfter(expirationDate)) {
                    certificateStatuses.add("Expiring Soon");
                } else {
                    certificateStatuses.add("Valid");
                }
            }

            request.setAttribute("certificates", pagedCertificates);
            request.setAttribute("certificateStatuses", certificateStatuses);
            request.setAttribute("currentDate", currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            request.setAttribute("currentPage", page);
            request.setAttribute("totalCertificates", allCertificates.size());
            request.setAttribute("pageSize", pageSize);

            request.getRequestDispatcher("/views/user/student/certificates.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading certificates: " + e.getMessage());
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
            if ("renewRequest".equals(action)) {
                try {
                    int certificateId = Integer.parseInt(request.getParameter("certificateId"));
                    Certificate certificate = certificateService.getCertificateById(certificateId);
                    if (certificate != null && certificate.getUserID() == currentUser.getUserID()) {
                        // Gửi thông báo đến admin để yêu cầu gia hạn
                        notificationService.sendNotificationToAdmins(
                                "Student " + currentUser.getFullName() + " (ID: " + currentUser.getUserID() + ") "
                                + "requests renewal for certificate ID: " + certificateId
                        );
                        request.setAttribute("successMessage", "Certificate renewal request submitted successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Invalid certificate or permission denied.");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid certificate ID.");
                }
            } else if ("download".equals(action)) {
                try {
                    int certificateId = Integer.parseInt(request.getParameter("certificateId"));
                    Certificate certificate = certificateService.getCertificateById(certificateId);
                    if (certificate != null && certificate.getUserID() == currentUser.getUserID()) {
                        // Giả lập tải xuống (thực tế cần tạo PDF)
                        response.setContentType("application/pdf");
                        response.setHeader("Content-Disposition", "attachment; filename=certificate_" + certificate.getCertificateCode() + ".pdf");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("This is a placeholder PDF for certificate: " + certificate.getCertificateCode());
                        }
                        return; // Không forward JSP vì đã gửi file
                    } else {
                        request.setAttribute("errorMessage", "Failed to download certificate.");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid certificate ID.");
                }
            }

            // Quay lại trang chứng chỉ sau khi xử lý
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for viewing and managing student certificates";
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
