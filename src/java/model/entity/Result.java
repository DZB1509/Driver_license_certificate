/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author ADMIN
 */
public class Result {
    private int resultID;
    private int examID;
    private int userID;
    private double score;
    private boolean passStatus; // true: Passed, false: Failed

    public Result() {
    }

    public Result(int resultID, int examID, int userID, double score, boolean passStatus) {
        this.resultID = resultID;
        this.examID = examID;
        this.userID = userID;
        this.score = score;
        this.passStatus = passStatus;
    }

    // Getters and Setters
    public int getResultID() {
        return resultID;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isPassStatus() {
        return passStatus;
    }

    public void setPassStatus(boolean passStatus) {
        this.passStatus = passStatus;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultID=" + resultID +
                ", examID=" + examID +
                ", userID=" + userID +
                ", score=" + score +
                ", passStatus=" + passStatus +
                '}';
    }
}
