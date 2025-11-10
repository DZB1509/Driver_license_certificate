/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 * @author ADMIN
 */
public class Admin {
    private int adminID;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private boolean status;

    public Admin() {
    }

    public Admin(int adminID, String username, String password, String fullName, String email, boolean status) {
        this.adminID = adminID;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
    }

    public Admin(String username, String password, String fullName, String email, boolean status) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
    }

    public int getAdminID() {
        return adminID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Admin[" +
                "adminID=" + adminID +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ']';
    }
}