package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.Course;
import model.entity.User;
import model.service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegisteredCoursesServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get the logged-in user
            User loggedUser = (User) session.getAttribute("loggedUser");
            int userID = loggedUser.getUserID();

            // Fetch the full list of registered courses for the user
            List<Course> registeredCoursesList = userService.getAllRegisteredCourses(userID);
            request.setAttribute("registeredCoursesList", registeredCoursesList);

            // Forward to the courses JSP page
            request.getRequestDispatcher("/views/user/student/courses.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("message", "Lỗi khi tải danh sách khóa học: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/views/user/student/courses.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Registered Courses Servlet";
    }
}