/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;
import model.entity.Certificate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class CertificateDAO {
   // Create a new certificate with validation
    public boolean createCertificate(Certificate certificate) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra khóa ngoại UserID
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUserById(certificate.getUserID()) == null) {
            System.out.println("Invalid UserID");
            return false;
        }
        // Kiểm tra trùng lặp CertificateCode
        if (getCertificateByCode(certificate.getCertificateCode()) != null) {
            System.out.println("CertificateCode already exists");
            return false;
        }
        // Kiểm tra ngày hợp lệ
        if (certificate.getIssuedDate().after(certificate.getExpirationDate())) {
            System.out.println("IssuedDate must be before ExpirationDate");
            return false;
        }

        String sql = "INSERT INTO Certificates (UserID, IssuedDate, ExpirationDate, CertificateCode) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, certificate.getUserID());
            statement.setDate(2, new java.sql.Date(certificate.getIssuedDate().getTime()));
            statement.setDate(3, new java.sql.Date(certificate.getExpirationDate().getTime()));
            statement.setString(4, certificate.getCertificateCode());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        certificate.setCertificateID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getPendingCertificatesCount() throws SQLException {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Certificates WHERE Result IS NULL OR Result = 'Pending'";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
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
    // Get certificate by ID
    public Certificate getCertificateById(int certificateId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Certificates WHERE CertificateID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, certificateId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapCertificate(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Certificate> getRecentCertificates() throws SQLException {
    DBContext db = DBContext.getInstance();
    List<Certificate> certificates = new ArrayList<>();
    String sql = "SELECT * FROM Certificates WHERE IssuedDate >= DATEADD(day, -30, GETDATE()) ORDER BY IssuedDate DESC";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                certificates.add(mapCertificate(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return certificates;
}

    // Get certificate by code
    public Certificate getCertificateByCode(String certificateCode) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Certificates WHERE CertificateCode = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, certificateCode);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapCertificate(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all certificates for a user
    public List<Certificate> getCertificatesByUserId(int userId) {
        DBContext db = DBContext.getInstance();
        List<Certificate> certificates = new ArrayList<>();
        String sql = "SELECT * FROM Certificates WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    certificates.add(mapCertificate(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificates;
    }

    // Get all certificates
    public List<Certificate> getAllCertificates() {
        DBContext db = DBContext.getInstance();
        List<Certificate> certificates = new ArrayList<>();
        String sql = "SELECT * FROM Certificates ORDER BY IssuedDate DESC";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                certificates.add(mapCertificate(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificates;
    }
    
    public List<Certificate> getCertificatesByDateRange(java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
    DBContext db = DBContext.getInstance();
    List<Certificate> certificates = new ArrayList<>();
    String sql = "SELECT * FROM Certificates WHERE IssuedDate BETWEEN ? AND ?";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setDate(1, startDate);
        ps.setDate(2, endDate);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                certificates.add(mapCertificate(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return certificates;
}

    // Get certificates with user details (with pagination)
    public List<Object[]> getCertificatesWithUserDetails(int page, int pageSize) {
        DBContext db = DBContext.getInstance();
        List<Object[]> certificates = new ArrayList<>();
        String sql = "SELECT c.*, u.FullName, u.Email, u.Class, u.School FROM Certificates c " +
                     "JOIN Users u ON c.UserID = u.UserID " +
                     "ORDER BY c.IssuedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, (page - 1) * pageSize);
            statement.setInt(2, pageSize);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[9];
                    row[0] = rs.getInt("CertificateID");
                    row[1] = rs.getInt("UserID");
                    row[2] = rs.getDate("IssuedDate");
                    row[3] = rs.getDate("ExpirationDate");
                    row[4] = rs.getString("CertificateCode");
                    row[5] = rs.getString("FullName");
                    row[6] = rs.getString("Email");
                    row[7] = rs.getString("Class");
                    row[8] = rs.getString("School");
                    certificates.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificates;
    }
    
    public int countAllCertificates() throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Certificates";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countCertificatesIssuedThisMonth() throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Certificates WHERE MONTH(IssuedDate) = MONTH(GETDATE()) AND YEAR(IssuedDate) = YEAR(GETDATE())";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countCertificatesIssuedLastMonth() throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Certificates WHERE MONTH(IssuedDate) = MONTH(DATEADD(month, -1, GETDATE())) AND YEAR(IssuedDate) = YEAR(DATEADD(month, -1, GETDATE()))";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
    
    public int countCertificatesBySupervisor(int supervisorId) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Certificates c JOIN Results r ON c.UserID = r.UserID JOIN Exams e ON r.ExamID = e.ExamID WHERE e.SupervisorID = ?";
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

    // Update certificate with validation
    public boolean updateCertificate(Certificate certificate) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra khóa ngoại UserID
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUserById(certificate.getUserID()) == null) {
            System.out.println("Invalid UserID for update");
            return false;
        }
        // Kiểm tra trùng lặp CertificateCode (trừ chính nó)
        Certificate existing = getCertificateByCode(certificate.getCertificateCode());
        if (existing != null && existing.getCertificateID() != certificate.getCertificateID()) {
            System.out.println("CertificateCode already exists for another certificate");
            return false;
        }
        // Kiểm tra ngày hợp lệ
        if (certificate.getIssuedDate().after(certificate.getExpirationDate())) {
            System.out.println("IssuedDate must be before ExpirationDate for update");
            return false;
        }

        String sql = "UPDATE Certificates SET UserID = ?, IssuedDate = ?, ExpirationDate = ?, CertificateCode = ? WHERE CertificateID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, certificate.getUserID());
            statement.setDate(2, new java.sql.Date(certificate.getIssuedDate().getTime()));
            statement.setDate(3, new java.sql.Date(certificate.getExpirationDate().getTime()));
            statement.setString(4, certificate.getCertificateCode());
            statement.setInt(5, certificate.getCertificateID());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean userHasValidCertificate(int userId) throws Exception {
    DBContext db = DBContext.getInstance();
    String sql = "SELECT COUNT(*) FROM Certificates WHERE UserID = ? AND ExpirationDate >= GETDATE()";
    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        }
    }
    return false;
}

    // Delete certificate
    public boolean deleteCertificate(int certificateId) {
        DBContext db = DBContext.getInstance();
        String sql = "DELETE FROM Certificates WHERE CertificateID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, certificateId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper method to map ResultSet to Certificate object
    private Certificate mapCertificate(ResultSet rs) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setCertificateID(rs.getInt("CertificateID"));
        certificate.setUserID(rs.getInt("UserID"));
        certificate.setIssuedDate(rs.getDate("IssuedDate"));
        certificate.setExpirationDate(rs.getDate("ExpirationDate"));
        certificate.setCertificateCode(rs.getString("CertificateCode"));
        return certificate;
    }
}
