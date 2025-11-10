/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;
import model.entity.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class ResultDAO {
    private static final double MAX_SCORE = 999.99; // Giới hạn của DECIMAL(5,2)

    // Create a new result with validation
    public boolean createResult(Result result) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra khóa ngoại
        ExamDAO examDAO = new ExamDAO();
        UserDAO userDAO = new UserDAO();
        if (examDAO.getExamById(result.getExamID()) == null || userDAO.getUserById(result.getUserID()) == null) {
            System.out.println("Invalid ExamID or UserID");
            return false;
        }
        // Kiểm tra trùng lặp
        if (getResultByUserAndExam(result.getUserID(), result.getExamID()) != null) {
            System.out.println("Result already exists for this UserID and ExamID");
            return false;
        }
        // Kiểm tra giá trị Score
        if (result.getScore() < 0 || result.getScore() > MAX_SCORE) {
            System.out.println("Invalid score value");
            return false;
        }

        String sql = "INSERT INTO Results (ExamID, UserID, Score, PassStatus) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, result.getExamID());
            statement.setInt(2, result.getUserID());
            statement.setDouble(3, result.getScore());
            statement.setBoolean(4, result.isPassStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result.setResultID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get result by ID
    public Result getResultById(int resultId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Results WHERE ResultID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, resultId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all results for a user
    public List<Result> getResultsByUserId(int userId) {
        DBContext db = DBContext.getInstance();
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM Results WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResult(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Get all results for an exam
    public List<Result> getResultsByExamId(int examId) {
        DBContext db = DBContext.getInstance();
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM Results WHERE ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, examId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResult(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Get result for a specific user in a specific exam
    public Result getResultByUserAndExam(int userId, int examId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Results WHERE UserID = ? AND ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, examId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update result with validation
    public boolean updateResult(Result result) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra khóa ngoại
        ExamDAO examDAO = new ExamDAO();
        UserDAO userDAO = new UserDAO();
        if (examDAO.getExamById(result.getExamID()) == null || userDAO.getUserById(result.getUserID()) == null) {
            System.out.println("Invalid ExamID or UserID for update");
            return false;
        }
        // Kiểm tra giá trị Score
        if (result.getScore() < 0 || result.getScore() > MAX_SCORE) {
            System.out.println("Invalid score value for update");
            return false;
        }

        String sql = "UPDATE Results SET ExamID = ?, UserID = ?, Score = ?, PassStatus = ? WHERE ResultID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, result.getExamID());
            statement.setInt(2, result.getUserID());
            statement.setDouble(3, result.getScore());
            statement.setBoolean(4, result.isPassStatus());
            statement.setInt(5, result.getResultID());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update result score and pass status with validation
    public boolean updateResultScore(int resultId, double score, boolean passStatus) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra giá trị Score
        if (score < 0 || score > MAX_SCORE) {
            System.out.println("Invalid score value for update");
            return false;
        }

        String sql = "UPDATE Results SET Score = ?, PassStatus = ? WHERE ResultID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setDouble(1, score);
            statement.setBoolean(2, passStatus);
            statement.setInt(3, resultId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int countAllResults() throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Results";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countPassResults() throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Results WHERE PassStatus = 1";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countResultsByExamId(int examId) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Results WHERE ExamID = ?";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setInt(1, examId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countResultsBySupervisor(int supervisorId) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Results r JOIN Exams e ON r.ExamID = e.ExamID WHERE e.SupervisorID = ?";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setInt(1, supervisorId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countPassResultsBySupervisor(int supervisorId) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Results r JOIN Exams e ON r.ExamID = e.ExamID WHERE e.SupervisorID = ? AND r.PassStatus = 1";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setInt(1, supervisorId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}

    // Delete result
    public boolean deleteResult(int resultId) {
        DBContext db = DBContext.getInstance();
        String sql = "DELETE FROM Results WHERE ResultID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, resultId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get results with user details (with pagination)
    public List<Object[]> getResultsWithUserDetails(int examId, int page, int pageSize) {
        DBContext db = DBContext.getInstance();
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT r.*, u.FullName, u.Email, u.Class, u.School FROM Results r " +
                     "JOIN Users u ON r.UserID = u.UserID " +
                     "WHERE r.ExamID = ? ORDER BY r.ResultID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, examId);
            statement.setInt(2, (page - 1) * pageSize);
            statement.setInt(3, pageSize);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[9];
                    row[0] = rs.getInt("ResultID");
                    row[1] = rs.getInt("ExamID");
                    row[2] = rs.getInt("UserID");
                    row[3] = rs.getDouble("Score");
                    row[4] = rs.getBoolean("PassStatus");
                    row[5] = rs.getString("FullName");
                    row[6] = rs.getString("Email");
                    row[7] = rs.getString("Class");
                    row[8] = rs.getString("School");
                    results.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Count passed students for an exam
    public int countPassedStudentsByExamId(int examId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT COUNT(*) FROM Results WHERE ExamID = ? AND PassStatus = 1";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, examId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Calculate average score for an exam
    public double getAverageScoreByExamId(int examId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT AVG(Score) FROM Results WHERE ExamID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, examId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Helper method to map ResultSet to Result object
    private Result mapResult(ResultSet rs) throws SQLException {
        Result result = new Result();
        result.setResultID(rs.getInt("ResultID"));
        result.setExamID(rs.getInt("ExamID"));
        result.setUserID(rs.getInt("UserID"));
        result.setScore(rs.getDouble("Score"));
        result.setPassStatus(rs.getBoolean("PassStatus"));
        return result;
    }
    }
