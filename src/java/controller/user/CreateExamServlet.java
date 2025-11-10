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
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.entity.Exam;
import model.service.ExamService;
import model.entity.User;


/**
 *
 * @author ADMIN
 */
public class CreateExamServlet extends HttpServlet {
    private ExamService examService;
    @Override
    public void init() throws ServletException {
        examService = new ExamService();
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
            out.println("<title>Servlet CreateExamServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateExamServlet at " + request.getContextPath () + "</h1>");
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
        processRequest(request, response);        
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
        //processRequest(request, response);
        response.setContentType("text/html;charset=UTF-8");
        // Lấy session để truy cập thông tin người dùng hiện tại
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        // Kiểm tra xem người dùng đã đăng nhập và là giáo viên chưa
        if (loggedUser == null || !"teacher".equals(loggedUser.getRole())) {
            request.setAttribute("message", "Bạn cần đăng nhập với vai trò giáo viên để tạo kỳ thi.");
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/views/user/teacher/dashboard.jsp").forward(request, response);
            return;
        }

        // Lấy dữ liệu từ form
        int courseID = Integer.parseInt(request.getParameter("courseID"));
        String examDateStr = request.getParameter("examDate");
        String room = request.getParameter("room");
        String status = request.getParameter("status");

        try {
            // Chuyển đổi ngày từ chuỗi sang Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date examDate = sdf.parse(examDateStr);

            // Lấy teacherID từ loggedUser thay vì từ form
            int supervisorID = loggedUser.getUserID();

            // Tạo đối tượng Exam
            Exam exam = new Exam();
            exam.setCourseID(courseID);
            exam.setDate(examDate);
            exam.setRoom(room);
            exam.setSupervisorID(supervisorID); // Gán supervisorID từ teacherID
            exam.setStatus(status);

            // Gọi ExamService để tạo kỳ thi
            boolean success = examService.createExam(exam);

            // Phản hồi kết quả
            if (success) {
                request.setAttribute("message", "Tạo kỳ thi thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Không thể tạo kỳ thi. Vui lòng kiểm tra lại thông tin.");
                request.setAttribute("messageType", "danger");
            }
            request.getRequestDispatcher("/views/user/teacher/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Đã xảy ra lỗi trong quá trình tạo kỳ thi.");
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/views/user/teacher/dashboard.jsp").forward(request, response);
        }
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
