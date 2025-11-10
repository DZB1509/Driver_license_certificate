/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;
import model.entity.Registration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
/**
 *
 * @author ADMIN
 */
public class RegistrationDAO {
    private DBContext dbConnection;

    public RegistrationDAO() throws Exception {
        dbConnection = DBContext.getInstance();
    }

    // Đăng ký khóa học mới
    public void createRegistration(Registration registration) throws Exception {
        String sql = "INSERT INTO Registrations (UserID, CourseID, Status, Comments) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, registration.getUserID());
            ps.setInt(2, registration.getCourseID());
            ps.setString(3, registration.getStatus());
            ps.setString(4, registration.getComments());
            ps.executeUpdate();
        }
    }

    // Lấy danh sách đăng ký theo UserID
    public ArrayList<Registration> getRegistrationsByUserId(int userId) throws Exception {
        ArrayList<Registration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM Registrations WHERE UserID = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registration reg = new Registration(
                        rs.getInt("RegistrationID"),
                        rs.getInt("UserID"),
                        rs.getInt("CourseID"),
                        rs.getString("Status"),
                        rs.getString("Comments")
                );
                registrations.add(reg);
            }
        }
        return registrations;
    }

    // Lấy danh sách đăng ký theo CourseID
    public ArrayList<Registration> getRegistrationsByCourseId(int courseId) throws Exception {
        ArrayList<Registration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM Registrations WHERE CourseID = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registration reg = new Registration(
                        rs.getInt("RegistrationID"),
                        rs.getInt("UserID"),
                        rs.getInt("CourseID"),
                        rs.getString("Status"),
                        rs.getString("Comments")
                );
                registrations.add(reg);
            }
        }
        return registrations;
    }

    // Cập nhật trạng thái đăng ký (Approved/Rejected)
    public boolean updateRegistrationStatus(int registrationId, String status) throws Exception {
        String sql = "UPDATE Registrations SET Status = ? WHERE RegistrationID = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, registrationId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
