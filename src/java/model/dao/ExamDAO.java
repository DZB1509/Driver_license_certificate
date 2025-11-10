/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;
import model.entity.Exam;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class ExamDAO {
// Create a new exam
    public boolean createExam(Exam exam) {
        DBContext db = DBContext.getInstance();
        String sql = "INSERT INTO Exams (CourseID, Date, Room, SupervisorID, Status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, exam.getCourseID());
            statement.setDate(2, new java.sql.Date(exam.getDate().getTime()));
            statement.setString(3, exam.getRoom());
            statement.setObject(4, exam.getSupervisorID() != 0 ? exam.getSupervisorID() : null, Types.INTEGER); // Handle nullable SupervisorID
            statement.setString(5, exam.getStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        exam.setExamID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get exam by ID
    public Exam getExamById(int examId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Exams WHERE ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, examId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapExam(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all exams
    public List<Exam> getAllExams() {
        DBContext db = DBContext.getInstance();
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM Exams";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                exams.add(mapExam(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }

    // Get exams by course ID
    public List<Exam> getExamsByCourseId(int courseId) {
        DBContext db = DBContext.getInstance();
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM Exams WHERE CourseID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, courseId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    exams.add(mapExam(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }
    
    // Lấy danh sách kỳ thi theo trạng thái
    public List<Exam> getExamsByStatus(String status) {
        DBContext db = DBContext.getInstance();
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM Exams WHERE Status = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, status);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    exams.add(mapExam(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }
    
    public List<Exam> getExamsByDate(Date date) throws SQLException {
    DBContext db = DBContext.getInstance();
    List<Exam> exams = new ArrayList<>();
    String sql = "SELECT * FROM Exams WHERE Date = ?";
    try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
        statement.setDate(1, date);
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                exams.add(mapExam(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return exams;
}
    
    public List<Exam> getExamsByDateRange(java.util.Date start, java.util.Date end) throws Exception {
    DBContext db = DBContext.getInstance();
    List<Exam> exams = new ArrayList<>();
    String sql = "SELECT * FROM Exams WHERE Date BETWEEN ? AND ?";
    try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
        statement.setDate(1, (Date) start);
        statement.setDate(2, (Date) end);
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                exams.add(mapExam(rs));
            }
        }
    }
    return exams;
}
    
    // Get exams by supervisor ID
    public List<Exam> getExamsBySupervisorId(int supervisorId) {
        DBContext db = DBContext.getInstance();
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM Exams WHERE SupervisorID = ? ORDER BY Date DESC";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, supervisorId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    exams.add(mapExam(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }
    
    public List<Exam> getUpcomingExams(java.sql.Date fromDate, int page, int pageSize) throws SQLException {
    DBContext db = DBContext.getInstance();
    List<Exam> exams = new ArrayList<>();
    String sql = "SELECT * FROM Exams WHERE Date >= ? AND Status NOT IN ('Completed', 'Cancelled') " +
                 "ORDER BY Date ASC LIMIT ? OFFSET ?";
    int offset = (page - 1) * pageSize;
    try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
        statement.setDate(1, fromDate);
        statement.setInt(2, pageSize);
        statement.setInt(3, offset);
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                exams.add(mapExam(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return exams;
}
    
    public int getUpcomingExamsCount(java.sql.Date fromDate) throws SQLException {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Exams WHERE Date >= ? AND Status NOT IN ('Completed', 'Cancelled')";
    try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
        statement.setDate(1, fromDate);
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return 0;
}

    // Update exam
    public boolean updateExam(Exam exam) {
        DBContext db = DBContext.getInstance();
        String sql = "UPDATE Exams SET CourseID = ?, Date = ?, Room = ?, SupervisorID = ?, Status = ? WHERE ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, exam.getCourseID());
            statement.setDate(2, new java.sql.Date(exam.getDate().getTime()));
            statement.setString(3, exam.getRoom());
            statement.setObject(4, exam.getSupervisorID() != 0 ? exam.getSupervisorID() : null, Types.INTEGER); // Handle nullable SupervisorID
            statement.setString(5, exam.getStatus());
            statement.setInt(6, exam.getExamID());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update exam status
    public boolean updateExamStatus(int examId, String status) {
        DBContext db = DBContext.getInstance();
        String sql = "UPDATE Exams SET Status = ? WHERE ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, examId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete exam
    public boolean deleteExam(int examId) {
        DBContext db = DBContext.getInstance();
        String sql = "DELETE FROM Exams WHERE ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, examId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int countExamsByStatus(String status) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Exams WHERE Status = ?";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setString(1, status);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countExamsByStatusAndSupervisor(String status, int supervisorId) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Exams WHERE Status = ? AND SupervisorID = ?";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setString(1, status);
        ps.setInt(2, supervisorId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public List<Exam> getExamsByDateRangeAndSupervisor(java.sql.Date start, java.sql.Date end, int supervisorId) throws Exception {
    DBContext db = DBContext.getInstance();
    List<Exam> exams = new ArrayList<>();
    String sql = "SELECT * FROM Exams WHERE Date BETWEEN ? AND ? AND SupervisorID = ?";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setDate(1, start);
        ps.setDate(2, end);
        ps.setInt(3, supervisorId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                exams.add(mapExam(rs));
            }
        }
    }
    return exams;
}

    // Helper method to map ResultSet to Exam object
    private Exam mapExam(ResultSet rs) throws SQLException {
    Exam exam = new Exam();
    exam.setExamID(rs.getInt("ExamID"));
    exam.setCourseID(rs.getInt("CourseID"));
    exam.setDate(rs.getDate("Date"));
    exam.setRoom(rs.getString("Room"));
    Integer supervisorId = rs.getObject("SupervisorID") != null ? rs.getInt("SupervisorID") : null;
    exam.setSupervisorID(supervisorId != null ? supervisorId : 0);
    exam.setStatus(rs.getString("Status"));
    return exam;
}
}
