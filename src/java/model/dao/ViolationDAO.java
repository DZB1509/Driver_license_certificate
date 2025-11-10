/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

/**
 *
 * @author ADMIN
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViolationDAO {

    private DBContext dbContext;

    public ViolationDAO() {
        dbContext = DBContext.getInstance();
    }

    // Lấy tổng số vi phạm (dựa trên chứng chỉ không đạt và kỳ thi bị hủy)
    public int getTotalViolations() throws SQLException {
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM Certificates WHERE Result LIKE '%Không đạt%') + " +
                     "(SELECT COUNT(*) FROM Exams WHERE Status = 'Cancelled') AS totalViolations";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("totalViolations");
            }
            return 0;
        }
    }
}