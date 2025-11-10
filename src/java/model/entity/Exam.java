/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class Exam {
    private int examID;
    private int courseID;
    private Date date;
    private String room;
    private int supervisorID;
    private String status; // Pending, Approved, In Progress, Completed, Cancelled

    public Exam() {
    }

    public Exam(int examID, int courseID, Date date, String room, int supervisorID, String status) {
        this.examID = examID;
        this.courseID = courseID;
        this.date = date;
        this.room = room;
        this.supervisorID = supervisorID;
        this.status = status;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(int supervisorID) {
        this.supervisorID = supervisorID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examID=" + examID +
                ", courseID=" + courseID +
                ", date=" + date +
                ", room='" + room + '\'' +
                ", supervisorID=" + supervisorID +
                ", status='" + status + '\'' +
                '}';
    }
}
