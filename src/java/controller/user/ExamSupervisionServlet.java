package controller.user;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.Exam;
import model.entity.User;
import model.service.ExamService;
import model.service.NotificationService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class ExamSupervisionServlet extends HttpServlet {

    private ExamService examService;
    private NotificationService notificationService;
    private static final Logger LOGGER = Logger.getLogger(ExamSupervisionServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            examService = new ExamService();
            notificationService = new NotificationService();
            LOGGER.info("ExamSupervisionServlet initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize ExamSupervisionServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize ExamSupervisionServlet", e);
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
        if (!"tPolice".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: TrafficPolice only");
            return;
        }

        try {
            String action = request.getParameter("action");

            if (action == null || action.equals("list")) {
                int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
                int pageSize = 5; // Giới hạn số kỳ thi hiển thị trong dashboard

                // Lấy danh sách kỳ thi sắp diễn ra
                LocalDate today = LocalDate.now();
                List<Exam> upcomingExamsList = examService.getUpcomingExams(java.sql.Date.valueOf(LocalDate.now()), page, pageSize);

                // Gán dữ liệu cho dashboard hoặc exam_supervision.jsp
                request.setAttribute("upcomingExamsList", upcomingExamsList);
                request.setAttribute("currentPage", page);
                request.setAttribute("pageSize", pageSize);

                // Chuyển tiếp đến dashboard hoặc trang giám sát chi tiết
                String targetPage = request.getParameter("fromDashboard") != null 
                    ? "/views/user/trafficpolice/dashboard.jsp" 
                    : "/views/user/trafficpolice/exam_supervision.jsp";
                request.getRequestDispatcher(targetPage).forward(request, response);
            } else if (action.equals("view")) {
                int examId = Integer.parseInt(request.getParameter("examId"));
                Exam exam = examService.getExamById(examId);
                request.setAttribute("exam", exam);
                request.getRequestDispatcher("/views/user/trafficpolice/exam_supervision.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi tải danh sách kỳ thi: " + e.getMessage());
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
        if (!"trafficPolice".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: TrafficPolice only");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("supervise".equals(action)) {
                // Gán cảnh sát hiện tại làm người giám sát kỳ thi
                int examId = Integer.parseInt(request.getParameter("examId"));
                int supervisorId = currentUser.getUserID();
                if (examService.assignSupervisor(examId, supervisorId)) {
                    notificationService.sendNotification(
                            supervisorId,
                            "Bạn đã được gán làm người giám sát cho kỳ thi #" + examId + "."
                    );
                    request.setAttribute("message", "Đã gán bạn làm người giám sát kỳ thi!");
                    request.setAttribute("messageType", "success");
                } else {
                    request.setAttribute("message", "Không thể gán người giám sát cho kỳ thi.");
                    request.setAttribute("messageType", "danger");
                }
            } else if ("updateStatus".equals(action)) {
                int examId = Integer.parseInt(request.getParameter("examId"));
                String newStatus = request.getParameter("newStatus");
                if (examService.updateExamStatus(examId, newStatus)) {
                    Exam exam = examService.getExamById(examId);
                    if (exam.getSupervisorID() > 0) {
                        notificationService.sendNotification(
                                exam.getSupervisorID(),
                                "Trạng thái kỳ thi #" + examId + " đã được cập nhật thành " + newStatus + "."
                        );
                    }
                    request.setAttribute("message", "Cập nhật trạng thái kỳ thi thành công!");
                    request.setAttribute("messageType", "success");
                } else {
                    request.setAttribute("message", "Không thể cập nhật trạng thái kỳ thi.");
                    request.setAttribute("messageType", "danger");
                }
            } else if ("scheduleExam".equals(action)) {
                int examId = Integer.parseInt(request.getParameter("examId"));
                Date date = Date.valueOf(request.getParameter("date"));
                String room = request.getParameter("room");
                if (examService.updateExamSchedule(examId, date, room)) {
                    List<User> students = examService.getStudentsByExamId(examId); // Giả định có phương thức này
                    for (User student : students) {
                        notificationService.sendNotification(
                                student.getUserID(),
                                "Kỳ thi #" + examId + " của bạn đã được lên lịch vào ngày " + date + " tại phòng " + room + "."
                        );
                    }
                    request.setAttribute("message", "Lên lịch kỳ thi thành công!");
                    request.setAttribute("messageType", "success");
                } else {
                    request.setAttribute("message", "Không thể lên lịch kỳ thi.");
                    request.setAttribute("messageType", "danger");
                }
            }

            // Sau khi xử lý, quay lại dashboard hoặc trang giám sát
            String targetPage = request.getParameter("fromDashboard") != null 
                ? "/views/user/trafficpolice/dashboard.jsp" 
                : "/views/user/trafficpolice/exam_supervision.jsp";
            request.getRequestDispatcher(targetPage).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/views/user/trafficpolice/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý giám sát kỳ thi cho cảnh sát giao thông";
    }

    @Override
    public void destroy() {
        try {
            LOGGER.info("ExamSupervisionServlet destroyed successfully");
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi hủy ExamSupervisionServlet: " + e.getMessage());
        }
    }
}