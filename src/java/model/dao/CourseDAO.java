/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

/**
 *
 * @author ADMIN
 */
import model.entity.Course;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private DBContext dbConnection;

    public CourseDAO() throws Exception {
        dbConnection = DBContext.getInstance();
    }

    public boolean createCourse(Course course) throws Exception {
        String sql = "INSERT INTO Courses (CourseName, TeacherID, StartDate, EndDate, Description, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getTeacherID());
            ps.setDate(3, course.getStartDate());
            ps.setDate(4, course.getEndDate());
            ps.setString(5, course.getDescription());
            ps.setString(6, course.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Course> getAllCourses() throws Exception {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Course course = new Course(
                    rs.getInt("CourseID"),
                    rs.getString("CourseName"),
                    rs.getInt("TeacherID"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getString("Description"),
                    rs.getString("Status")
                );
                courses.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Courses WHERE CourseID = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getInt("TeacherID"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Description"),
                        rs.getString("Status")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Course> getCoursesByTeacherID(int teacherID) throws Exception {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Courses WHERE TeacherID = ?";
        List<Course> courses = new ArrayList<>();
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, teacherID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setTeacherID(rs.getInt("TeacherID"));
                course.setStartDate(rs.getDate("StartDate"));
                course.setEndDate(rs.getDate("EndDate"));
                course.setDescription(rs.getString("Description"));
                course.setStatus(rs.getString("Status"));
                courses.add(course);
            }
        }
        return courses;
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE Courses SET CourseName = ?, TeacherID = ?, StartDate = ?, EndDate = ?, Description = ?, Status = ? WHERE CourseID = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getTeacherID());
            ps.setDate(3, course.getStartDate());
            ps.setDate(4, course.getEndDate());
            ps.setString(5, course.getDescription());
            ps.setString(6, course.getStatus());
            ps.setInt(7, course.getCourseID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserEnrolledInCourse(int userId, int courseId) {
        String sql = "SELECT COUNT(*) FROM Registrations WHERE UserID = ? AND CourseID = ? AND Status = 'Approved'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
