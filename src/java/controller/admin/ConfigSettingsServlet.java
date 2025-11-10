/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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
public class ConfigSettingsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ConfigSettingsServlet.class.getName());

    // Phương thức init() để khởi tạo tài nguyên (hiện tại chưa có service, nhưng chuẩn bị cho tương lai)
    @Override
    public void init() throws ServletException {
        try {
            LOGGER.info("ConfigSettingsServlet initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize ConfigSettingsServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize ConfigSettingsServlet", e);
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
            out.println("<title>Servlet ConfigSettingsServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConfigSettingsServlet at " + request.getContextPath () + "</h1>");
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
        request.setAttribute("settingName", "Example Setting");
        request.setAttribute("settingValue", "Example Value");
        request.getRequestDispatcher("/views/admin/system_config/settings.jsp").forward(request, response);
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
        String settingName = request.getParameter("settingName");
        String settingValue = request.getParameter("settingValue");

        try {
            // Giả định: Lưu cài đặt vào cơ sở dữ liệu hoặc file cấu hình
            // Thực tế cần một service để xử lý logic này
            request.getSession().setAttribute("message", "Setting updated successfully!");
            request.getSession().setAttribute("messageType", "success");
        } catch (Exception e) {
            request.getSession().setAttribute("message", "Failed to update setting: " + e.getMessage());
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

    // Đóng tài nguyên khi servlet bị hủy (nếu cần)
    @Override
    public void destroy() {
        LOGGER.info("ConfigSettingsServlet destroyed");
    }
}