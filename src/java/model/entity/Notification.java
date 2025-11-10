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
public class Notification {
     private int notificationID;
    private int userID;
    private String message;
    private Date sentDate;
    private boolean isRead;

    public Notification() {
    }

    public Notification(int notificationID, int userID, String message, Date sentDate, boolean isRead) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.message = message;
        this.sentDate = sentDate;
        this.isRead = isRead;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationID=" + notificationID +
                ", userID=" + userID +
                ", message='" + message + '\'' +
                ", sentDate=" + sentDate +
                ", isRead=" + isRead +
                '}';
    }
}
