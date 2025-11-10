/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;
import model.entity.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
/**
 *
 * @author ADMIN
 */
public class NotificationDAO {
  private static final int MAX_MESSAGE_LENGTH = 500; // Giả định độ dài tối đa của Message trong DB

    // Create a new notification with validation
    public boolean createNotification(Notification notification) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra khóa ngoại UserID
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUserById(notification.getUserID()) == null) {
            System.out.println("Invalid UserID");
            return false;
        }
        // Kiểm tra độ dài Message
        if (notification.getMessage() == null || notification.getMessage().length() > MAX_MESSAGE_LENGTH) {
            System.out.println("Message is null or exceeds maximum length");
            return false;
        }

        String sql = "INSERT INTO Notifications (UserID, Message, SentDate, IsRead) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, notification.getUserID());
            statement.setString(2, notification.getMessage());
            // Sử dụng SentDate nếu có, nếu không dùng thời gian hiện tại
            if (notification.getSentDate() != null) {
                statement.setTimestamp(3, new java.sql.Timestamp(notification.getSentDate().getTime()));
            } else {
                statement.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            }
            statement.setBoolean(4, notification.isRead());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        notification.setNotificationID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Gửi thông báo đến tất cả user có vai trò cụ thể
    public void sendToRole(String role, String message) throws Exception {
        DBContext db = DBContext.getInstance();
        
        // Truy vấn danh sách UserID theo Role
        String selectSql = "SELECT UserID FROM Users WHERE Role = ?";
        List<Integer> userIds = new ArrayList<>();
        try (PreparedStatement selectStmt = db.getConnection().prepareStatement(selectSql)) {
            selectStmt.setString(1, role);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("UserID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để tầng trên xử lý
        }

        // Chèn thông báo cho từng UserID
        String insertSql = "INSERT INTO Notifications (UserID, Message, SentDate, IsRead) VALUES (?, ?, GETDATE(), 0)";
        try (Connection conn = db.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            for (int userId : userIds) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, message);
                insertStmt.addBatch(); // Thêm vào batch để thực thi hàng loạt
            }
            insertStmt.executeBatch(); // Thực thi tất cả lệnh INSERT cùng lúc
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để tầng trên xử lý
        }
    }

    // Get notification by ID
    public Notification getNotificationById(int notificationId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT * FROM Notifications WHERE NotificationID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, notificationId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapNotification(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all notifications for a user (with pagination)
    public List<Notification> getNotificationsByUserId(int userId, int page, int pageSize) {
        DBContext db = DBContext.getInstance();
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? " +
                     "ORDER BY SentDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, (page - 1) * pageSize);
            statement.setInt(3, pageSize);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapNotification(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Get unread notifications for a user (with pagination)
    public List<Notification> getUnreadNotificationsByUserId(int userId, int page, int pageSize) {
        DBContext db = DBContext.getInstance();
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? AND IsRead = 0 " +
                     "ORDER BY SentDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, (page - 1) * pageSize);
            statement.setInt(3, pageSize);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapNotification(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Mark notification as read
    public boolean markAsRead(int notificationId) {
        DBContext db = DBContext.getInstance();
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE NotificationID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, notificationId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mark all notifications as read for a user
    public boolean markAllAsRead(int userId) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra UserID
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUserById(userId) == null) {
            System.out.println("Invalid UserID");
            return false;
        }

        String sql = "UPDATE Notifications SET IsRead = 1 WHERE UserID = ? AND IsRead = 0";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete notification
    public boolean deleteNotification(int notificationId) {
        DBContext db = DBContext.getInstance();
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, notificationId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete all notifications for a user
    public boolean deleteAllNotificationsForUser(int userId) {
        DBContext db = DBContext.getInstance();
        // Kiểm tra UserID
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUserById(userId) == null) {
            System.out.println("Invalid UserID");
            return false;
        }

        String sql = "DELETE FROM Notifications WHERE UserID = ?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Count unread notifications for a user
    public int countUnreadNotifications(int userId) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT COUNT(*) FROM Notifications WHERE UserID = ? AND IsRead = 0";
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Helper method to map ResultSet to Notification object
    private Notification mapNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationID(rs.getInt("NotificationID"));
        notification.setUserID(rs.getInt("UserID"));
        notification.setMessage(rs.getString("Message"));
        notification.setSentDate(rs.getTimestamp("SentDate"));
        notification.setRead(rs.getBoolean("IsRead"));
        return notification;
    }
}
