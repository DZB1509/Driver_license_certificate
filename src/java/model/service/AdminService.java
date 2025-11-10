package model.service;

import java.sql.SQLException;
import model.dao.AdminDAO;
import model.entity.Admin;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author ADMIN
 */
public class AdminService {
    private AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    public Admin authenticateAdmin(String emailOrUsername, String password) {
        // Kiểm tra dữ liệu đầu vào
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null; // Dữ liệu đầu vào không hợp lệ
        }

        try {
            // Lấy admin bằng email hoặc username
            Admin admin = adminDAO.getAdminByEmailOrUsername(emailOrUsername);
            if (admin == null || !admin.isStatus()) {
                return null; // Admin không tồn tại hoặc đã bị vô hiệu hóa
            }

            // Kiểm tra mật khẩu sử dụng BCrypt
            if (BCrypt.checkpw(password, admin.getPassword())) {
                return admin; // Xác thực thành công
            }
            return null; // Mật khẩu không đúng
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Lỗi hệ thống
        }
    }

    public boolean registerAdmin(Admin admin) throws Exception {
        return adminDAO.insertAdmin(admin);
    }

    public Admin getAdminByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null; // Username không hợp lệ
        }
        return adminDAO.getAdminByUsername(username);
    }

    public Admin getAdminByEmailOrUsername(String emailOrUsername) throws SQLException {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
            return null;
        }
        return adminDAO.getAdminByEmailOrUsername(emailOrUsername);
    }

    public Admin getAdminById(int adminID) {
        if (adminID <= 0) {
            return null; // ID không hợp lệ
        }
        return adminDAO.getAdminById(adminID);
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = adminDAO.getAllAdmins();
        return admins != null ? admins : List.of();
    }

    public boolean insertAdmin(Admin admin) {
        if (admin == null ||
                admin.getUsername() == null || admin.getUsername().trim().isEmpty() ||
                admin.getEmail() == null || admin.getEmail().trim().isEmpty() ||
                admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            return false; // Dữ liệu đầu vào không hợp lệ
        }

        // Kiểm tra username và email đã tồn tại trước khi thêm
        if (adminDAO.checkUsernameExists(admin.getUsername()) ||
                adminDAO.checkEmailExists(admin.getEmail())) {
            return false; // Username hoặc email đã tồn tại
        }

        // Mã hóa mật khẩu trước khi lưu
        String hashedPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt(12));
        admin.setPassword(hashedPassword);

        return adminDAO.insertAdmin(admin);
    }

    public boolean updateAdmin(Admin admin) {
        if (admin == null || admin.getAdminID() <= 0) {
            return false; // Dữ liệu đầu vào không hợp lệ
        }

        // Kiểm tra xem admin có tồn tại không trước khi cập nhật
        Admin existingAdmin = adminDAO.getAdminById(admin.getAdminID());
        if (existingAdmin == null) {
            return false; // Admin không tồn tại
        }

        // Kiểm tra email mới có trùng với email khác không (nếu email thay đổi)
        if (!existingAdmin.getEmail().equals(admin.getEmail()) && adminDAO.checkEmailExists(admin.getEmail())) {
            return false; // Email đã tồn tại
        }

        return adminDAO.updateAdmin(admin);
    }

    public boolean updateAdminProfile(Admin admin) {
        if (admin == null || admin.getAdminID() <= 0) {
            return false; // Dữ liệu đầu vào không hợp lệ
        }

        // Kiểm tra xem admin có tồn tại không trước khi cập nhật
        Admin existingAdmin = adminDAO.getAdminById(admin.getAdminID());
        if (existingAdmin == null) {
            return false; // Admin không tồn tại
        }

        // Kiểm tra email mới có trùng với email khác không (nếu email thay đổi)
        if (!existingAdmin.getEmail().equals(admin.getEmail()) && adminDAO.checkEmailExists(admin.getEmail())) {
            return false; // Email đã tồn tại
        }

        return adminDAO.updateAdminProfile(admin);
    }

    public boolean updateAdminPassword(int adminID, String newPassword) {
        if (adminID <= 0 || newPassword == null || newPassword.trim().isEmpty()) {
            return false; // Dữ liệu đầu vào không hợp lệ
        }

        // Kiểm tra xem admin có tồn tại không trước khi cập nhật
        Admin existingAdmin = adminDAO.getAdminById(adminID);
        if (existingAdmin == null) {
            return false; // Admin không tồn tại
        }

        // Mã hóa mật khẩu với BCrypt
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        return adminDAO.updateAdminPassword(adminID, hashedPassword);
    }

    public boolean deactivateAdmin(int adminID) {
        if (adminID <= 0) {
            return false; // ID không hợp lệ
        }

        // Kiểm tra xem admin có tồn tại và đang hoạt động không trước khi vô hiệu hóa
        Admin existingAdmin = adminDAO.getAdminById(adminID);
        if (existingAdmin == null || !existingAdmin.isStatus()) {
            return false; // Admin không tồn tại hoặc đã bị vô hiệu hóa
        }

        return adminDAO.deactivateAdmin(adminID);
    }

    public boolean checkUsernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false; // Username không hợp lệ
        }
        return adminDAO.checkUsernameExists(username);
    }

    public boolean checkEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false; // Email không hợp lệ
        }
        return adminDAO.checkEmailExists(email);
    }
}