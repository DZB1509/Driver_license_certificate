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
public class Certificate {
    private int certificateID;
    private int userID;
    private Date issuedDate;
    private Date expirationDate;
    private String certificateCode;
    
    public Certificate() {
    }

    public Certificate(int certificateID, int userID, Date issuedDate, Date expirationDate, String certificateCode) {
        this.certificateID = certificateID;
        this.userID = userID;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
        this.certificateCode = certificateCode;
    }

    // Getters and Setters
    public int getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(int certificateID) {
        this.certificateID = certificateID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificateID=" + certificateID +
                ", userID=" + userID +
                ", issuedDate=" + issuedDate +
                ", expirationDate=" + expirationDate +
                ", certificateCode='" + certificateCode + '\'' +
                '}';
    }  
}
