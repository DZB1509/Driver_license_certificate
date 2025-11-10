package model.entity;

import java.sql.Timestamp;

public class User {
    private int userID;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String className;
    private String school;
    private String phone;
    private Integer createdBy; // Sửa thành Integer để hỗ trợ giá trị null
    private Timestamp createdAt;
    private boolean status;
    private int adminID; // Thêm thuộc tính để lưu AdminID của admin khi đăng nhập

    // Constructor mặc định
    public User() {
    }

    // Constructor đầy đủ tham số
    public User(int userID, String fullName, String email, String password, String role, String className, String school, String phone, Integer createdBy, Timestamp createdAt, boolean status) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.className = className;
        this.school = school;
        this.phone = phone;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Constructor không có userID và createdAt (dùng khi tạo user mới)
    public User(String fullName, String email, String password, String role, String className, String school, String phone, Integer createdBy, boolean status) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.className = className;
        this.school = school;
        this.phone = phone;
        this.createdBy = createdBy;
        this.status = status;
    }

    // Getters và setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    @Override
    public String toString() {
        return "User[" +
                "userID=" + userID +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", className='" + className + '\'' +
                ", school='" + school + '\'' +
                ", phone='" + phone + '\'' +
                ", createdBy=" + createdBy +
                ", status=" + status +
                "]";
    }
}