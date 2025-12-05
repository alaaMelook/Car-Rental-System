package car_rental_system;

import java.sql.*;

public class BookingDAO {
    
    public static boolean createBooking(int userId, int carId, String startDate, 
                                       String endDate, double totalPrice) {
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement updateCarStatus = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Check if car is still available
            String checkSql = "SELECT status FROM cars WHERE car_id = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setInt(1, carId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String status = rs.getString("status");
                if (!status.equals("Available")) {
                    conn.rollback();
                    return false;
                }
            } else {
                conn.rollback();
                return false;
            }
            rs.close();
            pst.close();
            
            // 2. Calculate number of days
            long diffInMillies = Math.abs(
                java.sql.Date.valueOf(endDate).getTime() - 
                java.sql.Date.valueOf(startDate).getTime()
            );
            long numberOfDays = diffInMillies / (1000 * 60 * 60 * 24);
            if (numberOfDays == 0) numberOfDays = 1;
            
            // 3. Insert booking (with number_of_days)
            String insertSql = "INSERT INTO bookings (user_id, car_id, start_date, end_date, number_of_days, total_price, booking_status) " +
                              "VALUES (?, ?, ?, ?, ?, ?, 'Confirmed')";
            pst = conn.prepareStatement(insertSql);
            pst.setInt(1, userId);
            pst.setInt(2, carId);
            pst.setString(3, startDate);
            pst.setString(4, endDate);
            pst.setLong(5, numberOfDays);
            pst.setDouble(6, totalPrice);
            
            int rowsAffected = pst.executeUpdate();
            
            if (rowsAffected > 0) {
                // 4. Update car status to 'Booked' (not 'Rented')
                String updateSql = "UPDATE cars SET status = 'Booked' WHERE car_id = ?";
                updateCarStatus = conn.prepareStatement(updateSql);
                updateCarStatus.setInt(1, carId);
                updateCarStatus.executeUpdate();
                
                conn.commit(); // Commit transaction
                return true;
            } else {
                conn.rollback();
                return false;
            }
            
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pst != null) pst.close();
                if (updateCarStatus != null) updateCarStatus.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}