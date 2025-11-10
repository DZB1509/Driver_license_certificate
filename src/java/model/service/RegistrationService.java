/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;
import model.dao.RegistrationDAO;
import model.entity.Registration;
import java.util.ArrayList;
/**
 *
 * @author ADMIN
 */
public class RegistrationService {
    private RegistrationDAO registrationDAO;

    public RegistrationService() throws Exception {
        this.registrationDAO = new RegistrationDAO();
    }

    // Đăng ký khóa học
    public boolean registerCourse(Registration registration) throws Exception {
        if (registration == null) {
            throw new IllegalArgumentException("Registration cannot be null");
        }
        if (registration.getUserID() <= 0 || registration.getCourseID() <= 0) {
            throw new IllegalArgumentException("Invalid UserID or CourseID");
        }
        if (!isValidStatus(registration.getStatus())) {
            throw new IllegalArgumentException("Invalid status: " + registration.getStatus());
        }

        // Kiểm tra xem user đã đăng ký khóa học này chưa
        ArrayList<Registration> existingRegistrations = registrationDAO.getRegistrationsByUserId(registration.getUserID());
        for (Registration reg : existingRegistrations) {
            if (reg.getCourseID() == registration.getCourseID() && 
                (reg.getStatus().equals("Pending") || reg.getStatus().equals("Approved"))) {
                throw new IllegalStateException("User has already registered for this course");
            }
        }

        registrationDAO.createRegistration(registration);
        return true;
    }

    // Lấy danh sách đăng ký của học sinh
    public ArrayList<Registration> getUserRegistrations(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid UserID");
        }
        ArrayList<Registration> registrations = registrationDAO.getRegistrationsByUserId(userId);
        if (registrations == null || registrations.isEmpty()) {
            throw new Exception("No registrations found for user ID: " + userId);
        }
        return registrations;
    }

    // Lấy danh sách học sinh đăng ký khóa học
    public ArrayList<Registration> getCourseRegistrations(int courseId) throws Exception {
        if (courseId <= 0) {
            throw new IllegalArgumentException("Invalid CourseID");
        }
        ArrayList<Registration> registrations = registrationDAO.getRegistrationsByCourseId(courseId);
        if (registrations == null || registrations.isEmpty()) {
            throw new Exception("No registrations found for course ID: " + courseId);
        }
        return registrations;
    }

    // Cập nhật trạng thái đăng ký (Approved/Rejected)
    public boolean updateRegistrationStatus(int registrationId, String status) throws Exception {
        if (registrationId <= 0) {
            throw new IllegalArgumentException("Invalid RegistrationID");
        }
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        return registrationDAO.updateRegistrationStatus(registrationId, status);
    }

    // Kiểm tra trạng thái hợp lệ
    private boolean isValidStatus(String status) {
        return status != null && ("Pending".equals(status) || "Approved".equals(status) || "Rejected".equals(status));
    }
}
