/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author ADMIN
 */
public class Registration {
    private int RegistrationID;
    private int UserID;
    private int CourseID;
    private String Status;
    private String Comments;

    public Registration() {
    }

    public Registration(int RegistrationID, int UserID, int CourseID, String Status, String Comments) {
        this.RegistrationID = RegistrationID;
        this.UserID = UserID;
        this.CourseID = CourseID;
        this.Status = Status;
        this.Comments = Comments;
    }

    public int getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(int RegistrationID) {
        this.RegistrationID = RegistrationID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public int getCourseID() {
        return CourseID;
    }

    public void setCourseID(int CourseID) {
        this.CourseID = CourseID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String Comments) {
        this.Comments = Comments;
    }

    @Override
    public String toString() {
        return "Registration{" + "RegistrationID=" + RegistrationID + ", UserID=" + UserID + ", CourseID=" + CourseID + ", Status=" + Status + ", Comments=" + Comments + '}';
    }
}
