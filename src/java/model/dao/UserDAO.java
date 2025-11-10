package model.dao;

import model.entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.Course;

/**
 * @author ADMIN
 */
public class UserDAO {
    private DBContext db = DBContext.getInstance();

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE Email = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerUser(User user) throws SQLException {
        // Kiểm tra xem email đã tồn tại chưa
        if (checkEmailExists(user.getEmail())) {
            return false; // Trả về false nếu email đã tồn tại
        }

        // Chuẩn bị câu lệnh SQL để chèn người dùng mới
        String sql = "INSERT INTO Users (FullName, Email, Password, Role, Class, School, Phone, CreatedBy, CreatedAt, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getClassName());
            statement.setString(6, user.getSchool());
            statement.setString(7, user.getPhone());
            statement.setObject(8, user.getCreatedBy(), Types.INTEGER); // Sử dụng setObject để hỗ trợ null
            statement.setTimestamp(9, user.getCreatedAt() != null ? user.getCreatedAt() : new Timestamp(System.currentTimeMillis()));
            statement.setBoolean(10, user.isStatus());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updateUserRole(String email, String role) throws SQLException {
        String sql = "UPDATE Users SET Role = ? WHERE Email = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, role);
            statement.setString(2, email);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public User getUserById(int userID) {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, userID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users WHERE Role = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, role);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    users.add(mapUser(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Course> getAllRegisteredCourses(int userID) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT c.CourseID, c.CourseName, r.RegistrationDate, c.Status " +
                      "FROM Registrations r " +
                      "JOIN Courses c ON r.CourseID = c.CourseID " +
                      "WHERE r.UserID = ? AND r.Status = 'Approved' " +
                      "ORDER BY r.RegistrationDate DESC";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                // Giả sử Course có thuộc tính RegistrationDate
                // course.setRegistrationDate(rs.getDate("RegistrationDate"));
                course.setStatus(rs.getString("Status").equalsIgnoreCase("Completed") ? "completed" : "ongoing");
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return courses;
    }

    public List<User> getUsersWithCertificates() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM Users u " +
                     "JOIN Certificates c ON u.UserID = c.UserID " +
                     "ORDER BY u.UserID";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    users.add(mapUser(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return users;
    }

    public List<User> getStudentsByExamId(int examId) throws Exception {
        List<User> students = new ArrayList<>();
        String sql = "SELECT u.* FROM Users u " +
                     "JOIN Results r ON u.UserID = r.UserID " +
                     "WHERE r.ExamID = ? AND u.Role = 'Student'";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapUser(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return students;
    }

    public List<User> getStudentsBySupervisor(int supervisorId) throws Exception {
        List<User> students = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM Users u " +
                     "JOIN Results r ON u.UserID = r.UserID " +
                     "JOIN Exams e ON r.ExamID = e.ExamID " +
                     "WHERE e.SupervisorID = ? AND u.Role = 'Student'";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, supervisorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapUser(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return students;
    }

    public boolean insertUser(User user) {
        String query = "INSERT INTO Users (FullName, Email, Password, Role, Class, School, Phone, CreatedBy, Status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getClassName());
            statement.setString(6, user.getSchool());
            statement.setString(7, user.getPhone());
            statement.setObject(8, user.getCreatedBy(), Types.INTEGER); // Sử dụng setObject để hỗ trợ null
            statement.setBoolean(9, user.isStatus());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE Users SET FullName = ?, Email = ?, Password = ?, Role = ?, Class = ?, School = ?, Phone = ?, CreatedBy = ?, Status = ? WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getClassName());
            statement.setString(6, user.getSchool());
            statement.setString(7, user.getPhone());
            statement.setObject(8, user.getCreatedBy(), Types.INTEGER); // Sử dụng setObject để hỗ trợ null
            statement.setBoolean(9, user.isStatus());
            statement.setInt(10, user.getUserID());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateUserProfile(User user) {
        DBContext db = DBContext.getInstance();
        String query = "UPDATE Users SET FullName = ?, Email = ?, Class = ?, School = ?, Phone = ? WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getClassName());
            statement.setString(4, user.getSchool());
            statement.setString(5, user.getPhone());
            statement.setInt(6, user.getUserID());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userID, String newPassword) {
        String query = "UPDATE Users SET Password = ? WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setInt(2, userID);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int userID) {
        String query = "UPDATE Users SET Status = 0 WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, userID);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        Integer createdBy = rs.getInt("CreatedBy");
        if (rs.wasNull()) {
            createdBy = null; // Xử lý trường hợp CreatedBy là NULL trong cơ sở dữ liệu
        }

        return new User(
                rs.getInt("UserID"),
                rs.getString("FullName"),
                rs.getString("Email"),
                rs.getString("Password"),
                rs.getString("Role"),
                rs.getString("Class"),
                rs.getString("School"),
                rs.getString("Phone"),
                createdBy,
                rs.getTimestamp("CreatedAt"),
                rs.getBoolean("Status")
        );
    }

    private void setUserParams(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getFullName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getRole());
        statement.setString(5, user.getClassName());
        statement.setString(6, user.getSchool());
        statement.setString(7, user.getPhone());
        statement.setObject(8, user.getCreatedBy(), Types.INTEGER); // Sử dụng setObject để hỗ trợ null
        statement.setBoolean(9, user.isStatus());
    }

    public User authenticateUser(String email, String password) {
        String sql = "SELECT * FROM Users WHERE Email = ? AND Status = 1";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = mapUser(rs);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (FullName, Email, Password, Role, Class, School, Phone, CreatedBy, CreatedAt, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getClassName());
            statement.setString(6, user.getSchool());
            statement.setString(7, user.getPhone());
            statement.setObject(8, user.getCreatedBy(), Types.INTEGER); // Sử dụng setObject để hỗ trợ null
            statement.setTimestamp(9, new Timestamp(user.getCreatedAt().getTime()));
            statement.setBoolean(10, true); // Mặc định Status là true khi tạo mới
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}