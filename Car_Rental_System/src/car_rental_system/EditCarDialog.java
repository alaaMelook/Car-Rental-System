package car_rental_system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditCarDialog extends JDialog {
    private int carId;
    private JTextField modelField;
    private JTextArea descArea;
    private JTextField priceField;
    private JTextField yearField;
    private JComboBox<String> seatsCombo;
    private JComboBox<String> fuelCombo;
    private JComboBox<String> transmissionCombo;
    private JComboBox<String> statusCombo;
    private JTextField imagePathField;
    private AdminDashboard parent;

    public EditCarDialog(AdminDashboard parent, int carId) {
        super(parent, "Edit Car Details", true);
        this.parent = parent;
        this.carId = carId;
        
        setSize(600, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        initComponents();
        loadCarData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("Edit Car Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Car Model Name
        mainPanel.add(createLabel("Car Model Name"));
        modelField = createTextField();
        mainPanel.add(modelField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Description
        mainPanel.add(createLabel("Description"));
        descArea = new JTextArea(3, 20);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        mainPanel.add(descScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Daily Rate and Year (side by side)
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.add(createLabel("Daily Rate ($)"));
        priceField = createTextField();
        pricePanel.add(priceField);

        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setBackground(Color.WHITE);
        yearPanel.add(createLabel("Year"));
        yearField = createTextField();
        yearPanel.add(yearField);

        row1.add(pricePanel);
        row1.add(yearPanel);
        mainPanel.add(row1);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Seats and Fuel Type
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setBackground(Color.WHITE);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel seatsPanel = new JPanel();
        seatsPanel.setLayout(new BoxLayout(seatsPanel, BoxLayout.Y_AXIS));
        seatsPanel.setBackground(Color.WHITE);
        seatsPanel.add(createLabel("Seats"));
        String[] seats = {"2 Seats", "4 Seats", "5 Seats", "7 Seats", "8 Seats"};
        seatsCombo = createComboBox(seats);
        seatsPanel.add(seatsCombo);

        JPanel fuelPanel = new JPanel();
        fuelPanel.setLayout(new BoxLayout(fuelPanel, BoxLayout.Y_AXIS));
        fuelPanel.setBackground(Color.WHITE);
        fuelPanel.add(createLabel("Fuel Type"));
        String[] fuels = {"Gasoline", "Diesel", "Electric", "Hybrid"};
        fuelCombo = createComboBox(fuels);
        fuelPanel.add(fuelCombo);

        row2.add(seatsPanel);
        row2.add(fuelPanel);
        mainPanel.add(row2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Transmission and Status
        JPanel row3 = new JPanel(new GridLayout(1, 2, 20, 0));
        row3.setBackground(Color.WHITE);
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel transPanel = new JPanel();
        transPanel.setLayout(new BoxLayout(transPanel, BoxLayout.Y_AXIS));
        transPanel.setBackground(Color.WHITE);
        transPanel.add(createLabel("Transmission"));
        String[] transmissions = {"Automatic", "Manual"};
        transmissionCombo = createComboBox(transmissions);
        transPanel.add(transmissionCombo);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(createLabel("Status"));
        String[] statuses = {"Available", "Booked", "Maintenance", "Inactive"};
        statusCombo = createComboBox(statuses);
        statusPanel.add(statusCombo);

        row3.add(transPanel);
        row3.add(statusPanel);
        mainPanel.add(row3);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Image URL/Path
        mainPanel.add(createLabel("Image URL"));
        JPanel imagePanel = new JPanel(new BorderLayout(10, 0));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        imagePathField = createTextField();
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        
        JButton browseBtn = new JButton("📁");
        browseBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        browseBtn.setPreferredSize(new Dimension(45, 45));
        browseBtn.setBackground(new Color(243, 244, 246));
        browseBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        browseBtn.setFocusPainted(false);
        browseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        browseBtn.addActionListener(e -> browseImage());
        imagePanel.add(browseBtn, BorderLayout.EAST);
        
        mainPanel.add(imagePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton saveBtn = new JButton("Save Car");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(new Color(59, 130, 246));
        saveBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveCar());

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cancelBtn.setForeground(new Color(100, 100, 100));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(11, 20, 11, 20)
        ));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        buttonsPanel.add(saveBtn);
        buttonsPanel.add(cancelBtn);
        mainPanel.add(buttonsPanel);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return field;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return combo;
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") 
                    || f.getName().toLowerCase().endsWith(".png") 
                    || f.getName().toLowerCase().endsWith(".jpeg");
            }
            public String getDescription() {
                return "Image Files (*.jpg, *.png, *.jpeg)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void loadCarData() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM cars WHERE car_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, carId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                modelField.setText(rs.getString("model_name"));
                descArea.setText(rs.getString("description"));
                priceField.setText(String.valueOf(rs.getDouble("daily_price")));
                yearField.setText(String.valueOf(rs.getInt("year")));
                
                // Set combo boxes
                int seats = rs.getInt("seats_no");
                seatsCombo.setSelectedItem(seats + " Seats");
                
                fuelCombo.setSelectedItem(rs.getString("fuel_type"));
                transmissionCombo.setSelectedItem(rs.getString("transmission_type"));
                statusCombo.setSelectedItem(rs.getString("status"));
                
                String imgPath = rs.getString("image_path");
                imagePathField.setText(imgPath != null ? imgPath : "");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading car data: " + e.getMessage());
        }
    }

    private void saveCar() {
        // Validate inputs
        if (modelField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!");
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());
            
            // Extract seats number from combo (e.g., "5 Seats" -> 5)
            String seatsStr = (String) seatsCombo.getSelectedItem();
            int seats = Integer.parseInt(seatsStr.split(" ")[0]);

            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE cars SET model_name=?, description=?, daily_price=?, " +
                        "seats_no=?, fuel_type=?, transmission_type=?, year=?, status=?, image_path=? " +
                        "WHERE car_id=?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, modelField.getText().trim());
            pst.setString(2, descArea.getText().trim());
            pst.setDouble(3, price);
            pst.setInt(4, seats);
            pst.setString(5, (String) fuelCombo.getSelectedItem());
            pst.setString(6, (String) transmissionCombo.getSelectedItem());
            pst.setInt(7, year);
            pst.setString(8, (String) statusCombo.getSelectedItem());
            pst.setString(9, imagePathField.getText().trim());
            pst.setInt(10, carId);

            int result = pst.executeUpdate();
            conn.close();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Car updated successfully!");
                parent.loadAllCars(); // Refresh the admin dashboard
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update car!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and year!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating car: " + e.getMessage());
        }
    }
}