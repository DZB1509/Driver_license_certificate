/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import model.entity.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ADMIN
 */
public class AdminDAO {
    private DBContext db = DBContext.getInstance();

    public Admin getAdminByUsername(String username) {
        String query = "SELECT * FROM Admins WHERE Username = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin getAdminById(int adminID) {
        String query = "SELECT * FROM Admins WHERE AdminID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, adminID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin getAdminByEmailOrUsername(String emailOrUsername) {
        String query = "SELECT * FROM Admins WHERE Email = ? OR Username = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, emailOrUsername);
            statement.setString(2, emailOrUsername);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Admin> getAllAdmins() {
        List<Admin> list = new ArrayList<>();
        String query = "SELECT * FROM Admins";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                list.add(mapAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertAdmin(Admin admin) {
        String query = "INSERT INTO Admins (Username, Password, FullName, Email, Status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            setAdminParams(statement, admin);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAdmin(Admin admin) {
        String query = "UPDATE Admins SET Username = ?, Password = ?, FullName = ?, Email = ?, Status = ? WHERE AdminID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            setAdminParams(statement, admin);
            statement.setInt(6, admin.getAdminID());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAdminProfile(Admin admin) {
        String query = "UPDATE Admins SET FullName = ?, Email = ? WHERE AdminID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, admin.getFullName());
            statement.setString(2, admin.getEmail());
            statement.setInt(3, admin.getAdminID());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAdminPassword(int adminID, String newPassword) {
        String query = "UPDATE Admins SET Password = ? WHERE AdminID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setInt(2, adminID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deactivateAdmin(int adminID) {
        String query = "UPDATE Admins SET Status = 0 WHERE AdminID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, adminID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM Admins WHERE Username = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM Admins WHERE Email = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Admin mapAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin(
                rs.getString("Username"),
                rs.getString("Password"),
                rs.getString("FullName"),
                rs.getString("Email"),
                rs.getBoolean("Status")
        );
        admin.setAdminID(rs.getInt("AdminID")); // Gán AdminID
        return admin;
    }

    private void setAdminParams(PreparedStatement statement, Admin admin) throws SQLException {
        statement.setString(1, admin.getUsername());
        statement.setString(2, admin.getPassword());
        statement.setString(3, admin.getFullName());
        statement.setString(4, admin.getEmail());
        statement.setBoolean(5, admin.isStatus());
    }
}