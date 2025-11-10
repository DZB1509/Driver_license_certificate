/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

import model.dao.NotificationDAO;
import model.entity.Notification;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class NotificationService {
    private NotificationDAO notificationDAO;
    private static final int MAX_MESSAGE_LENGTH = 500; // Đồng bộ với NotificationDAO

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    // Create a new notification for a user with validation
    public Notification createNotification(int userId, String message) {
        try {
            if (userId <= 0 || message == null || message.trim().isEmpty() || message.length() > MAX_MESSAGE_LENGTH) {
                System.out.println("Invalid user ID or message");
                return null;
            }
            Notification notification = new Notification();
            notification.setUserID(userId);
            notification.setMessage(message);
            notification.setSentDate(new Timestamp(System.currentTimeMillis()));
            notification.setRead(false);

            boolean success = notificationDAO.createNotification(notification);
            return success ? notification : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all notifications for a user with pagination
    public List<Notification> getNotificationsByUserId(int userId, int page, int pageSize) {
        try {
            if (userId <= 0 || page < 1 || pageSize <= 0) {
                System.out.println("Invalid user ID, page, or page size");
                return new ArrayList<>();
            }
            return notificationDAO.getNotificationsByUserId(userId, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get unread notifications for a user with pagination
    public List<Notification> getUnreadNotifications(int userId, int page, int pageSize) {
        try {
            if (userId <= 0 || page < 1 || pageSize <= 0) {
                System.out.println("Invalid user ID, page, or page size");
                return new ArrayList<>();
            }
            return notificationDAO.getUnreadNotificationsByUserId(userId, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Mark a notification as read
    public boolean markAsRead(int notificationId) {
        try {
            if (notificationId <= 0) {
                System.out.println("Invalid notification ID");
                return false;
            }
            return notificationDAO.markAsRead(notificationId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mark all notifications for a user as read
    public boolean markAllAsRead(int userId) {
        try {
            if (userId <= 0) {
                System.out.println("Invalid user ID");
                return false;
            }
            return notificationDAO.markAllAsRead(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a notification
    public boolean deleteNotification(int notificationId) {
        try {
            if (notificationId <= 0) {
                System.out.println("Invalid notification ID");
                return false;
            }
            return notificationDAO.deleteNotification(notificationId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete all notifications for a user
    public boolean deleteAllNotificationsForUser(int userId) {
        try {
            if (userId <= 0) {
                System.out.println("Invalid user ID");
                return false;
            }
            return notificationDAO.deleteAllNotificationsForUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Send simple notification (for ResultService compatibility)
    public boolean sendNotification(int userId, String message) {
        try {
            Notification notification = createNotification(userId, message);
            return notification != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void sendNotificationToAdmins(String message) {
        try {
            // Giả định gửi thông báo cho tất cả admin
            notificationDAO.sendToRole("Admin", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Send notification about exam registration
    public boolean sendExamRegistrationNotification(int userId, int examId, String examDate) {
        try {
            if (userId <= 0 || examId <= 0 || examDate == null || examDate.trim().isEmpty()) {
                System.out.println("Invalid user ID, exam ID, or exam date");
                return false;
            }
            String message = "You have been registered for exam #" + examId + " scheduled on " + examDate + ".";
            return sendNotification(userId, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Send notification about exam results
    public boolean sendExamResultNotification(int userId, int examId, boolean passed) {
        try {
            if (userId <= 0 || examId <= 0) {
                System.out.println("Invalid user ID or exam ID");
                return false;
            }
            String status = passed ? "passed" : "failed";
            String message = "Your results for exam #" + examId + " are now available. You have " + status + " the exam.";
            return sendNotification(userId, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Send notification about certificate issuance
    public boolean sendCertificateIssuedNotification(int userId, String certificateCode) {
        try {
            if (userId <= 0 || certificateCode == null || certificateCode.trim().isEmpty()) {
                System.out.println("Invalid user ID or certificate code");
                return false;
            }
            String message = "Congratulations! Your driving skills certificate with code " + certificateCode + " has been issued.";
            return sendNotification(userId, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Send notification about certificate expiration
    public boolean sendCertificateExpirationNotification(int userId, String certificateCode, String expirationDate) {
        try {
            if (userId <= 0 || certificateCode == null || certificateCode.trim().isEmpty() || expirationDate == null || expirationDate.trim().isEmpty()) {
                System.out.println("Invalid user ID, certificate code, or expiration date");
                return false;
            }
            String message = "Your driving skills certificate with code " + certificateCode + " will expire on " + expirationDate + ". Please consider renewal.";
            return sendNotification(userId, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Send custom notification to multiple users
    public boolean sendBulkNotification(List<Integer> userIds, String message) {
        try {
            if (userIds == null || userIds.isEmpty() || message == null || message.trim().isEmpty() || message.length() > MAX_MESSAGE_LENGTH) {
                System.out.println("Invalid user IDs or message");
                return false;
            }
            boolean allSuccess = true;
            for (int userId : userIds) {
                if (userId <= 0) {
                    System.out.println("Invalid user ID in bulk: " + userId);
                    allSuccess = false;
                    continue;
                }
                if (!sendNotification(userId, message)) {
                    allSuccess = false;
                }
            }
            return allSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get unread notification count for a user
    public int getUnreadNotificationCount(int userId) {
        try {
            if (userId <= 0) {
                System.out.println("Invalid user ID");
                return 0;
            }
            return notificationDAO.countUnreadNotifications(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    } 
}