/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class SystemConfigController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SystemConfigController.class.getName());

    // Phương thức init() để khởi tạo tài nguyên (hiện tại chưa có service, nhưng chuẩn bị cho tương lai)
    @Override
    public void init() throws ServletException {
        try {
            LOGGER.info("SystemConfigController initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize SystemConfigController: " + e.getMessage());
            throw new ServletException("Failed to initialize SystemConfigController", e);
        }
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
            out.println("<title>Servlet SystemConfigController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SystemConfigController at " + request.getContextPath () + "</h1>");
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
        // Giả định: Lấy dữ liệu cấu hình hệ thống
        request.setAttribute("systemConfig", "Sample Config Data");
        request.getRequestDispatcher("/views/admin/system_config.jsp").forward(request, response);
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
        try {
            String configData = request.getParameter("configData");
            // Lưu configData vào cơ sở dữ liệu hoặc file cấu hình
            request.getSession().setAttribute("message", "System configuration updated successfully!");
            request.getSession().setAttribute("messageType", "success");
        } catch (Exception e) {
            request.getSession().setAttribute("message", "Failed to update system configuration: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }
        response.sendRedirect(request.getContextPath() + "/admin/system_config");
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    // Phương thức destroy() để dọn dẹp tài nguyên khi servlet bị hủy
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