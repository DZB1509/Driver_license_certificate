/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

/**
 *
 * @author ADMIN
 */

import model.dao.ViolationDAO;
import java.sql.SQLException;

public class ViolationService {

    private ViolationDAO violationDAO;

    public ViolationService() {
        violationDAO = new ViolationDAO();
    }

    // Lấy tổng số vi phạm
    public int getTotalViolations() {
        try {
            return violationDAO.getTotalViolations();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
