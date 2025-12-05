package car_rental_system;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDAO {
    
    /**
     * ✅ تسجيل مستخدم جديد في قاعدة البيانات
     */
    public static boolean registerUser(String username, String password) {
        // التحقق من عدم وجود المستخدم مسبقاً
        String checkQuery = "SELECT user_id FROM users WHERE username = ?";
        String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, 'USER')";
        
        try (Connection conn = DBConnection.getConnection()) {
            
            // 1. التحقق من عدم وجود اسم المستخدم
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("❌ Username already exists!");
                return false;  // المستخدم موجود بالفعل
            }
            
            // 2. تشفير كلمة المرور (اختياري - للأمان)
            String hashedPassword = hashPassword(password);
            
            // 3. إدراج المستخدم الجديد
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            
            int rowsAffected = insertStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ User registered successfully: " + username);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Database Error during registration!");
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * التحقق من Login - يرجع userId إذا نجح، -1 إذا فشل
     */
    public static int authenticateUser(String username, String password) {
        String query = "SELECT user_id, role FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            // تشفير كلمة المرور للمقارنة
            String hashedPassword = hashPassword(password);
            
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");
                System.out.println("✅ Login Successful! Welcome " + username + " (Role: " + role + ")");
                return userId;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Database Error!");
            e.printStackTrace();
        }
        
        System.out.println("❌ Invalid Username or Password!");
        return -1;
    }
    
    /**
     * الحصول على الـ role بناءً على userId
     */
    public static String getUserRole(int userId) {
        String query = "SELECT role FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * الحصول على اسم المستخدم بناءً على userId
     */
    public static String getUsername(int userId) {
        String query = "SELECT username FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("username");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * تحديث معلومات المستخدم
     */
    public static boolean updateUser(int userId, String newUsername, String newPassword) {
        try (Connection conn = DBConnection.getConnection()) {
            
            String query;
            PreparedStatement pstmt;
            
            if (newPassword != null && !newPassword.isEmpty()) {
                // تحديث الاسم وكلمة المرور
                query = "UPDATE users SET username = ?, password = ? WHERE user_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, newUsername);
                pstmt.setString(2, hashPassword(newPassword));
                pstmt.setInt(3, userId);
            } else {
                // تحديث الاسم فقط
                query = "UPDATE users SET username = ? WHERE user_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, newUsername);
                pstmt.setInt(2, userId);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * التحقق من وجود اسم مستخدم
     */
    public static boolean usernameExists(String username) {
        String query = "SELECT user_id FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * تشفير كلمة المرور باستخدام SHA-256
     * (للأمان - يمكن إزالته إذا أردت تخزين كلمات المرور بدون تشفير)
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // في حالة فشل التشفير، نرجع كلمة المرور كما هي
            return password;
        }
    }
    
    /**
     * التحقق من كون المستخدم Admin
     */
    public static boolean isAdmin(int userId) {
        String role = getUserRole(userId);
        return "ADMIN".equalsIgnoreCase(role);
    }
    
    /**
     * الحصول على جميع المستخدمين (للـ Admin Dashboard)
     */
    public static ResultSet getAllUsers() {
        String query = "SELECT user_id, username, role FROM users";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}