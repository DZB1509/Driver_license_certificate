/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nvarchar/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import model.dao.DBContext;
import model.dao.UserDAO;
import model.entity.User;
import model.entity.Admin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.dao.AdminDAO;
import model.entity.Course;
import model.exception.EmailAlreadyExistsException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ADMIN
 */
public class UserService {

    private UserDAO userDAO;
    private AdminDAO adminDAO;

    public UserService() {
        try {
            this.userDAO = new UserDAO();
            this.adminDAO = new AdminDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    public List<Course> getAllRegisteredCourses(int userID) throws SQLException {
        return userDAO.getAllRegisteredCourses(userID);
    }
   
    public boolean registerUser(User user) throws SQLException {
        // Kiểm tra password không null hoặc rỗng
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống hoặc null");
        }

        // Mã hóa password trước khi lưu
        String salt = BCrypt.gensalt(12);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);

        // Đặt giá trị mặc định nếu chưa có
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        // Nếu CreatedBy là 0, đặt thành null (cho phép null khi người dùng tự đăng ký)
        if (user.getCreatedBy() != null && user.getCreatedBy() == 0) {
            user.setCreatedBy(null);
        }

        // Đặt trạng thái mặc định là active
        user.setStatus(true);

        return userDAO.registerUser(user);
    }
    
    public boolean updateUserRole(String email, String role) throws SQLException {
        // Kiểm tra vai trò hợp lệ
        List<String> validRoles = Arrays.asList("student", "teacher", "police");
        if (role == null || !validRoles.contains(role.toLowerCase())) {
            throw new IllegalArgumentException("Vai trò không hợp lệ. Vai trò phải là: student, teacher, hoặc police.");
        }

        return userDAO.updateUserRole(email, role);
    }
    
    /**
     * Get a user by their email.
     * @param email The email of the user.
     * @return User object if found, null otherwise.
     */
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    /**
     * Get a user by their ID.
     * @param userID The ID of the user.
     * @return User object if found, null otherwise.
     */
    public User getUserById(int userID) {
        return userDAO.getUserById(userID);
    }

    /**
     * Get all users in the system.
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public boolean createUser(User user) throws EmailAlreadyExistsException {
        // Kiểm tra email đã tồn tại chưa
        if (userDAO.checkEmailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        // Mã hóa mật khẩu trước khi lưu
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        
        // Đặt giá trị mặc định nếu chưa có
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        // Gọi UserDAO để tạo người dùng
        try {
            return userDAO.createUser(user);
        } catch (SQLException e) {
            // Xử lý SQLException ngay trong UserService
            throw new RuntimeException("Failed to create user due to database error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get users by their role.
     * @param role The role to filter users by (e.g., "Student", "Teacher", "TrafficPolice").
     * @return List of users with the specified role.
     */
    public List<User> getUsersByRole(String role) {
        return userDAO.getUsersByRole(role);
    }

    /**
     * Insert a new user into the system.
     * @param user The user to insert.
     * @return True if insertion is successful, false otherwise.
     */
    public boolean insertUser(User user) {
        // Kiểm tra email đã tồn tại trước khi thêm
        if (userDAO.checkEmailExists(user.getEmail())) {
            return false; // Email đã tồn tại
        }
        // Đặt giá trị mặc định nếu chưa có
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        return userDAO.insertUser(user);
    }
    
    public User authenticateUser(String email, String password) {
        // Lấy thông tin user từ UserDAO
        User user = userDAO.authenticateUser(email, password);
        
        // Kiểm tra xem user có tồn tại và mật khẩu có khớp không
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user; // Trả về user nếu xác thực thành công
        }
        return null; // Trả về null nếu xác thực thất bại
    }

    /**
     * Update an existing user's information.
     * @param user The user with updated information.
     * @return True if update is successful, false otherwise.
     */
    public boolean updateUser(User user) {
        // Kiểm tra xem user có tồn tại không trước khi cập nhật
        User existingUser = userDAO.getUserById(user.getUserID());
        if (existingUser == null) {
            return false; // User không tồn tại
        }
        return userDAO.updateUser(user);
    }

    /**
     * Update a user's profile (excluding password and role).
     * @param user The user with updated profile information.
     * @return True if update is successful, false otherwise.
     */
    public boolean updateUserProfile(User user) {
        // Kiểm tra xem user có tồn tại không trước khi cập nhật
        User existingUser = userDAO.getUserById(user.getUserID());
        if (existingUser == null) {
            return false; // User không tồn tại
        }
        return userDAO.updateUserProfile(user);
    }

    /**
     * Update a user's password.
     * @param userID The ID of the user.
     * @param newPassword The new password to set.
     * @return True if update is successful, false otherwise.
     */
    public boolean updatePassword(int userID, String newPassword) {
        // Kiểm tra xem user có tồn tại không trước khi cập nhật
        User existingUser = userDAO.getUserById(userID);
        if (existingUser == null) {
            return false; // User không tồn tại
        }
        return userDAO.updatePassword(userID, newPassword);
    }

    /**
     * Delete a user by setting their status to inactive (soft delete).
     * @param userID The ID of the user to delete.
     * @return True if deletion is successful, false otherwise.
     */
    public boolean deleteUser(int userID) {
        // Kiểm tra xem user có tồn tại không trước khi xóa
        User existingUser = userDAO.getUserById(userID);
        if (existingUser == null || !existingUser.isStatus()) {
            return false; // User không tồn tại hoặc đã bị xóa
        }
        return userDAO.deleteUser(userID);
    }

    /**
     * Check if an email already exists in the system.
     * @param email The email to check.
     * @return True if the email exists, false otherwise.
     */
    public boolean checkEmailExists(String email) {
        return userDAO.checkEmailExists(email);
    }
    
    public List<User> getUsersWithCertificates() throws Exception {
        return userDAO.getUsersWithCertificates();
    }
    
    public List<User> getStudentsByExamId(int examId) throws Exception {
        return userDAO.getStudentsByExamId(examId);
    }
    
    public List<User> getStudentsBySupervisor(int supervisorId) throws Exception {
        try {
            List<User> students = userDAO.getStudentsBySupervisor(supervisorId);
            // Thêm logic xử lý nếu cần, ví dụ: sắp xếp theo FullName
            students.sort((u1, u2) -> u1.getFullName().compareTo(u2.getFullName()));
            return students;
        } catch (Exception e) {
            throw new Exception("Failed to retrieve students by supervisor due to: " + e.getMessage(), e);
        }
    }
}