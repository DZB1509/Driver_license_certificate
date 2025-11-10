package controller.user;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.Certificate;
import model.entity.User;
import model.service.CertificateService;
import model.service.NotificationService;
import java.util.List;
import java.util.logging.Logger;

public class CertificateApprovalServlet extends HttpServlet {

    private CertificateService certificateService;
    private NotificationService notificationService;
    private static final Logger LOGGER = Logger.getLogger(CertificateApprovalServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            certificateService = new CertificateService();
            notificationService = new NotificationService();
            LOGGER.info("CertificateApprovalServlet initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize CertificateApprovalServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize CertificateApprovalServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("loggedUser");
        if (!"TrafficPolice".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: TrafficPolice only");
            return;
        }

        try {
            String action = request.getParameter("action");

            if (action == null || action.equals("list")) {
                int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
                int pageSize = 5; // Giới hạn số chứng chỉ hiển thị trong dashboard

                // Lấy danh sách chứng chỉ chờ phê duyệt gần đây
//                List<Certificate> recentCertificates = certificateService.getPendingCertificatesCount()getpen;

                // Gán dữ liệu cho dashboard hoặc certificate_approval.jsp
//                request.setAttribute("recentCertificates", recentCertificates);
                request.setAttribute("currentPage", page);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("totalCertificates", certificateService.getPendingCertificatesCount());

                // Chuyển tiếp đến dashboard hoặc trang phê duyệt chi tiết
                String targetPage = request.getParameter("fromDashboard") != null 
                    ? "/views/user/trafficpolice/dashboard.jsp" 
                    : "/views/user/trafficpolice/certificate_approval.jsp";
                request.getRequestDispatcher(targetPage).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi tải danh sách chứng chỉ: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/views/user/trafficpolice/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("loggedUser");
        if (!"TrafficPolice".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: TrafficPolice only");
            return;
        }

        String action = request.getParameter("action");

//        try {
//            if ("approve".equals(action)) {
//                // Phê duyệt chứng chỉ
//                int certificateId = Integer.parseInt(request.getParameter("certificateId"));
//                Certificate certificate = certificateService.getCertificateById(certificateId);
//                if (certificate != null && certificateService.approveCertificate(certificateId)) {
//                    notificationService.sendNotification(
//                            certificate.getUserID(),
//                            "Chứng chỉ kỹ năng lái xe của bạn (Mã: " + certificate.getCertificateCode() + ") đã được phê duyệt bởi cảnh sát giao thông."
//                    );
//                    request.setAttribute("message", "Phê duyệt chứng chỉ thành công!");
//                    request.setAttribute("messageType", "success");
//                } else {
//                    request.setAttribute("message", "Không thể phê duyệt chứng chỉ.");
//                    request.setAttribute("messageType", "danger");
//                }
//            } else if ("revoke".equals(action)) {
//                // Thu hồi chứng chỉ
//                int certificateId = Integer.parseInt(request.getParameter("certificateId"));
//                if (certificateService.revokeCertificate(certificateId)) {
//                    Certificate certificate = certificateService.getCertificateById(certificateId);
//                    notificationService.sendNotification(
//                            certificate.getUserID(),
//                            "Chứng chỉ kỹ năng lái xe của bạn (Mã: " + certificate.getCertificateCode() + ") đã bị thu hồi bởi cảnh sát giao thông."
//                    );
//                    request.setAttribute("message", "Thu hồi chứng chỉ thành công!");
//                    request.setAttribute("messageType", "success");
//                } else {
//                    request.setAttribute("message", "Không thể thu hồi chứng chỉ.");
//                    request.setAttribute("messageType", "danger");
//                }
//            } else if ("renew".equals(action)) {
//                // Gia hạn chứng chỉ
//                int certificateId = Integer.parseInt(request.getParameter("certificateId"));
//                Certificate renewed = certificateService.renewCertificate(certificateId);
//                if (renewed != null) {
//                    notificationService.sendNotification(
//                            renewed.getUserID(),
//                            "Chứng chỉ kỹ năng lái xe của bạn (Mã: " + renewed.getCertificateCode() + ") đã được gia hạn. Ngày hết hạn mới: " + renewed.getExpirationDate()
//                    );
//                    request.setAttribute("message", "Gia hạn chứng chỉ thành công!");
//                    request.setAttribute("messageType", "success");
//                } else {
//                    request.setAttribute("message", "Không thể gia hạn chứng chỉ.");
//                    request.setAttribute("messageType", "danger");
//                }
//            }
//
//            // Sau khi xử lý, quay lại dashboard hoặc trang phê duyệt
//            String targetPage = request.getParameter("fromDashboard") != null 
//                ? "/views/user/trafficpolice/dashboard.jsp" 
//                : "/views/user/trafficpolice/certificate_approval.jsp";
//            request.getRequestDispatcher(targetPage).forward(request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("message", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
//            request.setAttribute("messageType", "danger");
//            request.getRequestDispatcher("/views/user/trafficpolice/dashboard.jsp").forward(request, response);
//        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý phê duyệt chứng chỉ cho cảnh sát giao thông";
    }

    @Override
    public void destroy() {
        try {
            LOGGER.info("CertificateApprovalServlet destroyed successfully");
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi hủy CertificateApprovalServlet: " + e.getMessage());
        }
    }
}