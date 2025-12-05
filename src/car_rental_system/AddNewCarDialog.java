package car_rental_system;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AddNewCarDialog extends JDialog {
    
    private JTextField modelField;
    private JTextArea descArea;
    private JTextField priceField;
    private JTextField yearField;
    private JComboBox<String> seatsCombo;
    private JComboBox<String> fuelCombo;
    private JComboBox<String> transmissionCombo;
    private JComboBox<String> statusCombo;
    private JTextField imageUrlField;
    
    private AdminDashboard parentFrame;

    public AddNewCarDialog(AdminDashboard parent) {
        super(parent, "Add New Car", true);
        this.parentFrame = parent;
        
        setSize(600, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);
        
        getContentPane().setBackground(Color.WHITE);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(600, 750));
        
        // Title
        JLabel titleLabel = new JLabel("Add New Car");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setBounds(30, 20, 400, 30);
        mainPanel.add(titleLabel);
        
        // Close button
        JButton closeBtn = new JButton("✕");
        closeBtn.setBounds(540, 20, 30, 30);
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        closeBtn.setForeground(new Color(120, 120, 120));
        closeBtn.setBackground(Color.WHITE);
        closeBtn.setBorder(null);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        mainPanel.add(closeBtn);
        
        int yPos = 70;
        
        // Car Model Name
        JLabel modelLabel = new JLabel("Car Model Name");
        modelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        modelLabel.setForeground(new Color(60, 60, 60));
        modelLabel.setBounds(30, yPos, 540, 20);
        mainPanel.add(modelLabel);
        
        modelField = new JTextField("e.g., Tesla Model S");
        modelField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        modelField.setForeground(new Color(150, 150, 150));
        modelField.setBounds(30, yPos + 25, 540, 45);
        modelField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        modelField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (modelField.getText().equals("e.g., Tesla Model S")) {
                    modelField.setText("");
                    modelField.setForeground(new Color(30, 30, 30));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (modelField.getText().isEmpty()) {
                    modelField.setText("e.g., Tesla Model S");
                    modelField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        mainPanel.add(modelField);
        
        yPos += 85;
        
        // Description
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(60, 60, 60));
        descLabel.setBounds(30, yPos, 540, 20);
        mainPanel.add(descLabel);
        
        descArea = new JTextArea("Describe the car features and specifications");
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setForeground(new Color(150, 150, 150));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        descArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (descArea.getText().equals("Describe the car features and specifications")) {
                    descArea.setText("");
                    descArea.setForeground(new Color(30, 30, 30));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (descArea.getText().isEmpty()) {
                    descArea.setText("Describe the car features and specifications");
                    descArea.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBounds(30, yPos + 25, 540, 80);
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        mainPanel.add(descScroll);
        
        yPos += 120;
        
        // Daily Rate and Year (side by side)
        JLabel priceLabel = new JLabel("Daily Rate ($)");
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priceLabel.setForeground(new Color(60, 60, 60));
        priceLabel.setBounds(30, yPos, 250, 20);
        mainPanel.add(priceLabel);
        
        JLabel yearLabel = new JLabel("Year");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        yearLabel.setForeground(new Color(60, 60, 60));
        yearLabel.setBounds(320, yPos, 250, 20);
        mainPanel.add(yearLabel);
        
        priceField = new JTextField("0");
        priceField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceField.setBounds(30, yPos + 25, 250, 45);
        priceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        mainPanel.add(priceField);
        
        yearField = new JTextField("2025");
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearField.setBounds(320, yPos + 25, 250, 45);
        yearField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        mainPanel.add(yearField);
        
        yPos += 85;
        
        // Seats and Fuel Type
        JLabel seatsLabel = new JLabel("Seats");
        seatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seatsLabel.setForeground(new Color(60, 60, 60));
        seatsLabel.setBounds(30, yPos, 250, 20);
        mainPanel.add(seatsLabel);
        
        JLabel fuelLabel = new JLabel("Fuel Type");
        fuelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fuelLabel.setForeground(new Color(60, 60, 60));
        fuelLabel.setBounds(320, yPos, 250, 20);
        mainPanel.add(fuelLabel);
        
        String[] seats = {"2 Seats", "4 Seats", "5 Seats", "7 Seats", "8+ Seats"};
        seatsCombo = new JComboBox<>(seats);
        seatsCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        seatsCombo.setBounds(30, yPos + 25, 250, 45);
        seatsCombo.setBackground(Color.WHITE);
        seatsCombo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        seatsCombo.setSelectedIndex(2);
        mainPanel.add(seatsCombo);
        
        String[] fuel = {"Gasoline", "Diesel", "Electric", "Hybrid"};
        fuelCombo = new JComboBox<>(fuel);
        fuelCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fuelCombo.setBounds(320, yPos + 25, 250, 45);
        fuelCombo.setBackground(Color.WHITE);
        fuelCombo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        mainPanel.add(fuelCombo);
        
        yPos += 85;
        
        // Transmission and Status
        JLabel transLabel = new JLabel("Transmission");
        transLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transLabel.setForeground(new Color(60, 60, 60));
        transLabel.setBounds(30, yPos, 250, 20);
        mainPanel.add(transLabel);
        
        JLabel statusLabel = new JLabel("Status");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(60, 60, 60));
        statusLabel.setBounds(320, yPos, 250, 20);
        mainPanel.add(statusLabel);
        
        String[] transmission = {"Automatic", "Manual"};
        transmissionCombo = new JComboBox<>(transmission);
        transmissionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transmissionCombo.setBounds(30, yPos + 25, 250, 45);
        transmissionCombo.setBackground(Color.WHITE);
        transmissionCombo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        mainPanel.add(transmissionCombo);
        
        // IMPORTANT: match DB enum values exactly
        String[] status = {"Available", "Booked", "Maintenance", "Inactive"};
        statusCombo = new JComboBox<>(status);
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setBounds(320, yPos + 25, 250, 45);
        statusCombo.setBackground(Color.WHITE);
        statusCombo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        mainPanel.add(statusCombo);
        
        yPos += 85;
        
        // Image URL
        JLabel imageLabel = new JLabel("Image URL");
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        imageLabel.setForeground(new Color(60, 60, 60));
        imageLabel.setBounds(30, yPos, 540, 20);
        mainPanel.add(imageLabel);
        
        imageUrlField = new JTextField("https://example.com/car-image.jpg");
        imageUrlField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        imageUrlField.setForeground(new Color(150, 150, 150));
        imageUrlField.setBounds(30, yPos + 25, 490, 45);
        imageUrlField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        imageUrlField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (imageUrlField.getText().equals("https://example.com/car-image.jpg")) {
                    imageUrlField.setText("");
                    imageUrlField.setForeground(new Color(30, 30, 30));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (imageUrlField.getText().isEmpty()) {
                    imageUrlField.setText("https://example.com/car-image.jpg");
                    imageUrlField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        mainPanel.add(imageUrlField);
        
        // Upload icon button
        JButton uploadBtn = new JButton("⬆");
        uploadBtn.setBounds(530, yPos + 25, 40, 45);
        uploadBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        uploadBtn.setBackground(Color.WHITE);
        uploadBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        uploadBtn.setFocusPainted(false);
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                imageUrlField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                imageUrlField.setForeground(new Color(30, 30, 30));
            }
        });
        mainPanel.add(uploadBtn);
        
        yPos += 90;
        
        // Buttons
        JButton saveBtn = new JButton("Save Car");
        saveBtn.setBounds(30, yPos, 250, 48);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(new Color(33, 150, 243));
        saveBtn.setBorder(null);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveCar());
        mainPanel.add(saveBtn);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(320, yPos, 250, 48);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cancelBtn.setForeground(new Color(120, 120, 120));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());
        mainPanel.add(cancelBtn);
        
        add(new JScrollPane(mainPanel));
        setVisible(true);
    }
    
    private void saveCar() {
        String model = modelField.getText().trim();
        String desc = descArea.getText().trim();
        String priceStr = priceField.getText().trim();
        String yearStr = yearField.getText().trim();
        String seats = (String) seatsCombo.getSelectedItem();
        String fuel = (String) fuelCombo.getSelectedItem();
        String transmission = (String) transmissionCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();
        String imageUrl = imageUrlField.getText().trim();
        
        // Validation
        if (model.isEmpty() || model.equals("e.g., Tesla Model S")) {
            JOptionPane.showMessageDialog(this, "Please enter car model name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (desc.isEmpty() || desc.equals("Describe the car features and specifications")) {
            JOptionPane.showMessageDialog(this, "Please enter description!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid daily rate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract seat number (handles "8+ Seats" -> 8)
        int seatNum;
        try {
            String s = seats.split(" ")[0].replace("+", "");
            seatNum = Integer.parseInt(s);
        } catch (Exception e) {
            seatNum = 5; // fallback default
        }

        // Map UI/status values to DB enum values if needed
        // (we use values that match DB enum: Available, Booked, Maintenance, Inactive)
        String dbStatus = status;

        // Insert into database
        try {
            Connection conn = DBConnection.getConnection();
            // Note: column names must match the DB schema (seats_no, fuel_type, transmission_type)
            String sql = "INSERT INTO cars (model_name, description, daily_price, seats_no, fuel_type, transmission_type, year, status, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, model);
            pst.setString(2, desc);
            pst.setDouble(3, price);
            pst.setInt(4, seatNum);
            pst.setString(5, fuel);
            pst.setString(6, transmission);
            pst.setInt(7, year);
            pst.setString(8, dbStatus);
            pst.setString(9, imageUrl.equals("https://example.com/car-image.jpg") ? "" : imageUrl);
            
            pst.executeUpdate();
            pst.close();
            conn.close();
            
            JOptionPane.showMessageDialog(this, "Car added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.loadAllCars();
            dispose();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding car: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}