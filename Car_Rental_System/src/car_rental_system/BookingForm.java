package car_rental_system;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File; 
import java.net.URL;
import java.util.Date;
import java.util.Calendar;
import java.awt.geom.RoundRectangle2D; // ⭐️⭐️ تم إضافة هذا الاستيراد ⭐️⭐️
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class BookingForm extends JFrame {
    private int userId;
    private int carId;
    private String carModel;
    private double dailyPrice;
    private String carImage;
    private String carDescription;
    private int seats;
    private String fuel;
    private String transmission;
    private String year;
    
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JLabel numberOfDaysLabel;
    private JLabel totalPriceLabel;
    private JButton confirmBtn;
    private JButton cancelBtn;

  public BookingForm(int userId, int carId, String carModel, double dailyPrice, 
                     String imgPath, String desc, int seats1, String fuelType, 
                     String transmission1, String year1) {
    this.userId = userId;
    this.carId = carId;
    this.carModel = carModel;
    this.dailyPrice = dailyPrice;
    this.carImage = imgPath; 
    this.carDescription = desc; 
    this.seats = seats1; 
    this.fuel = fuelType; 
    this.transmission = transmission1; 
    this.year = year1; 
    

        setTitle("DriveElite - Book Your Car");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // Main Container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(245, 247, 250));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(30, 0));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Left Side - Car Details
        JPanel leftPanel = createCarDetailsPanel();
        contentPanel.add(leftPanel, BorderLayout.CENTER);

        // Right Side - Booking Details
        JPanel rightPanel = createBookingDetailsPanel();
        contentPanel.add(rightPanel, BorderLayout.EAST);

        mainContainer.add(contentPanel, BorderLayout.CENTER);
        add(mainContainer);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Back Button
        JButton backBtn = new JButton("← Back to Cars");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.setForeground(new Color(33, 150, 243));
        backBtn.setBackground(new Color(245, 247, 250));
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Book Your Car");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Complete your reservation details");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(new Color(245, 247, 250));
        backPanel.add(backBtn);
        
        headerPanel.add(backPanel, BorderLayout.EAST);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createCarDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(230, 230, 230), 1, 12),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Car Image Container (to hold the rounded panel)
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(800, 350));
        imageContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        imageContainer.setBackground(Color.WHITE); // الخلفية بيضاء

        // استخدام الفئة الجديدة للحواف المستديرة 
        RoundedImagePanel roundedImagePanel = new RoundedImagePanel(12);
        roundedImagePanel.setLayout(new BorderLayout());
        roundedImagePanel.setBackground(new Color(245, 247, 250));
        roundedImagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // مسافة داخلية

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);
        
        // منطق تحميل الصورة المُوحّد
        if (carImage != null && !carImage.isEmpty()) {
            try {
                ImageIcon icon = null;
                File imageFile = new File(carImage);
                
                if (carImage.startsWith("http://") || carImage.startsWith("https://")) {
                    icon = new ImageIcon(new URL(carImage));
                } else if (imageFile.exists()) {
                    icon = new ImageIcon(carImage);
                } else {
                    String resourcePath = carImage.startsWith("/") ? carImage : "/" + carImage;
                    URL imgURL = getClass().getResource(resourcePath);
                    if (imgURL != null) {
                        icon = new ImageIcon(imgURL);
                    }
                }
                
                if (icon != null && icon.getIconWidth() != -1) {
                    // تحجيم الصورة لتناسب الحاوية مع ترك مساحة للحواف
                    Image img = icon.getImage().getScaledInstance(730, 300, Image.SCALE_SMOOTH); 
                    imgLabel.setIcon(new ImageIcon(img));
                } else {
                    imgLabel.setText("Image Not Found at path: " + carImage);
                    imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    imgLabel.setForeground(new Color(220, 38, 38));
                }
            } catch (Exception e) {
                imgLabel.setText("Error loading image: " + e.getMessage());
                imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                imgLabel.setForeground(new Color(220, 38, 38));
                e.printStackTrace();
            }
        } else {
            imgLabel.setText("No car image provided 🚗");
            imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            imgLabel.setForeground(new Color(180, 180, 180));
        }
        
        roundedImagePanel.add(imgLabel, BorderLayout.CENTER);
        
        // إضافة ظل خفيف للحاوية الخارجية للمسة جمالية
        imageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // مسافة خارجية
        
        imageContainer.add(roundedImagePanel, BorderLayout.CENTER);
        
        // Car Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        // Car Model and Status
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.setBackground(Color.WHITE);
        modelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel modelLabel = new JLabel(carModel);
        modelLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        modelLabel.setForeground(new Color(30, 30, 30));

        JLabel availableLabel = new JLabel(" ✓ Available ");
        availableLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        availableLabel.setForeground(new Color(34, 139, 34));
        availableLabel.setOpaque(true);
        availableLabel.setBackground(new Color(220, 252, 231));
        availableLabel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(220, 252, 231), 0, 5),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));


        modelPanel.add(modelLabel, BorderLayout.WEST);
        modelPanel.add(availableLabel, BorderLayout.EAST);

        infoPanel.add(modelPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Specifications Grid
        JPanel specsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        specsPanel.setBackground(Color.WHITE);
        specsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        String seatsValue = (seats > 0) ? seats + " Seats" : "5 Seats";
        String fuelValue = (fuel != null && !fuel.isEmpty()) ? fuel : "Gasoline";
        String transmissionValue = (transmission != null && !transmission.isEmpty()) ? transmission : "Automatic";
        String yearValue = (year != null && !year.isEmpty()) ? year : "2024";

        specsPanel.add(createSpecBox("👥", "Seats", seatsValue));
        specsPanel.add(createSpecBox("⛽", "Fuel", fuelValue));
        specsPanel.add(createSpecBox("⚙️", "Transmission", transmissionValue));
        specsPanel.add(createSpecBox("📅", "Year", yearValue));

        infoPanel.add(specsPanel);

        panel.add(imageContainer); 
        panel.add(infoPanel);

        return panel;
    }

    private JPanel createSpecBox(String icon, String label, String value) {
        JPanel specBox = new JPanel();
        specBox.setLayout(new BoxLayout(specBox, BoxLayout.Y_AXIS));
        specBox.setBackground(new Color(249, 250, 251));
        specBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10) 
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));


        JLabel labelText = new JLabel(label, SwingConstants.CENTER);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 11)); 
        labelText.setForeground(new Color(107, 114, 128));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueText = new JLabel(value, SwingConstants.CENTER);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        valueText.setForeground(new Color(17, 24, 39));
        valueText.setAlignmentX(Component.CENTER_ALIGNMENT);

        specBox.add(Box.createRigidArea(new Dimension(0, 5)));
        specBox.add(iconLabel);
        specBox.add(Box.createRigidArea(new Dimension(0, 8)));
        specBox.add(labelText);
        specBox.add(Box.createRigidArea(new Dimension(0, 4)));
        specBox.add(valueText);
        specBox.add(Box.createRigidArea(new Dimension(0, 5)));

        return specBox;
    }

    private JPanel createBookingDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(420, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(230, 230, 230), 1, 12),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel("Booking Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Start Date
        JLabel startDateLabel = new JLabel("Start Date");
        startDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        startDateLabel.setForeground(new Color(60, 60, 60));
        startDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("MM/dd/yyyy");
        startDateChooser.setMinSelectableDate(new Date()); 
        startDateChooser.getJCalendar().setTodayButtonVisible(true);
        startDateChooser.getJCalendar().setNullDateButtonVisible(false);
        startDateChooser.setDate(new Date()); 
        
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) startDateChooser.getDateEditor();
        dateEditor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateEditor.setForeground(new Color(30, 30, 30));
        dateEditor.setBackground(Color.WHITE);
        dateEditor.setEditable(false);
        
        startDateChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        startDateChooser.setPreferredSize(new Dimension(360, 50));
        startDateChooser.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(200, 200, 200), 1, 8),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        startDateChooser.setBackground(Color.WHITE);
        
        startDateChooser.addPropertyChangeListener("date", e -> {
            Date newStartDate = startDateChooser.getDate();
            Date currentEndDate = endDateChooser.getDate();
            
            endDateChooser.setMinSelectableDate(newStartDate != null ? newStartDate : new Date());

            if (newStartDate != null && currentEndDate != null && currentEndDate.before(newStartDate)) {
                endDateChooser.setDate(newStartDate);
            } else if (newStartDate != null && currentEndDate == null) {
                endDateChooser.setDate(newStartDate);
            }
            
            calculateTotalPrice();
        });

        panel.add(startDateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(startDateChooser);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // End Date
        JLabel endDateLabel = new JLabel("End Date");
        endDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        endDateLabel.setForeground(new Color(60, 60, 60));
        endDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("MM/dd/yyyy");
        endDateChooser.setMinSelectableDate(new Date()); 
        endDateChooser.getJCalendar().setTodayButtonVisible(true);
        endDateChooser.getJCalendar().setNullDateButtonVisible(false);
        endDateChooser.setDate(new Date()); 
        
        JTextFieldDateEditor endDateEditor = (JTextFieldDateEditor) endDateChooser.getDateEditor();
        endDateEditor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        endDateEditor.setForeground(new Color(30, 30, 30));
        endDateEditor.setBackground(Color.WHITE);
        endDateEditor.setEditable(false);
        
        endDateChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        endDateChooser.setPreferredSize(new Dimension(360, 50));
        endDateChooser.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(200, 200, 200), 1, 8),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        endDateChooser.setBackground(Color.WHITE);
        
        endDateChooser.addPropertyChangeListener("date", e -> calculateTotalPrice());

        panel.add(endDateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(endDateChooser);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Daily Rate
        JPanel dailyRatePanel = new JPanel(new BorderLayout());
        dailyRatePanel.setBackground(Color.WHITE);
        dailyRatePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel dailyRateLabel = new JLabel("Daily Rate");
        dailyRateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        dailyRateLabel.setForeground(new Color(80, 80, 80));

        JLabel dailyRateValue = new JLabel("$" + (int)dailyPrice);
        dailyRateValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dailyRateValue.setForeground(new Color(30, 30, 30));

        dailyRatePanel.add(dailyRateLabel, BorderLayout.WEST);
        dailyRatePanel.add(dailyRateValue, BorderLayout.EAST);

        panel.add(dailyRatePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Number of Days
        JPanel daysPanel = new JPanel(new BorderLayout());
        daysPanel.setBackground(Color.WHITE);
        daysPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel daysLabel = new JLabel("Number of Days");
        daysLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        daysLabel.setForeground(new Color(80, 80, 80));

        numberOfDaysLabel = new JLabel("1"); 
        numberOfDaysLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        numberOfDaysLabel.setForeground(new Color(30, 30, 30));

        daysPanel.add(daysLabel, BorderLayout.WEST);
        daysPanel.add(numberOfDaysLabel, BorderLayout.EAST);

        panel.add(daysPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Total Price
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(219, 234, 254));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(59, 130, 246), 2, 10),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel totalLabel = new JLabel("Total Price");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(30, 64, 175));

        totalPriceLabel = new JLabel("$" + (int)dailyPrice);
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalPriceLabel.setForeground(new Color(30, 64, 175));

        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalPriceLabel, BorderLayout.EAST);

        panel.add(totalPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Confirm Button
        confirmBtn = new RoundedButton("Confirm Booking", 10);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(new Color(59, 130, 246));
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmBtn.addActionListener(e -> handleConfirmBooking());

        panel.add(confirmBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Cancel Button
        cancelBtn = new RoundedButton("Cancel", 10);
        cancelBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancelBtn.setForeground(new Color(220, 38, 38));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(new RoundedBorder(new Color(220, 38, 38), 2, 10));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        panel.add(cancelBtn);

        return panel;
    }

    private void calculateTotalPrice() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();

        if (startDate != null && endDate != null) {
            
            if (endDate.before(startDate)) {
                numberOfDaysLabel.setText("0");
                totalPriceLabel.setText("$0");
                return;
            }

            long diffInMillies = endDate.getTime() - startDate.getTime(); 
            long days = diffInMillies / (1000 * 60 * 60 * 24);
            
            if (days == 0) days = 1; 

            double total = days * dailyPrice;

            numberOfDaysLabel.setText(String.valueOf(days));
            totalPriceLabel.setText("$" + (int)total);
        } else {
             numberOfDaysLabel.setText("0");
             totalPriceLabel.setText("$0");
        }
    }

    private void handleConfirmBooking() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this,
                "Please select both start and end dates!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (endDate.before(startDate)) {
            JOptionPane.showMessageDialog(this,
                "End date must be after or equal to start date!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long diffInMillies = endDate.getTime() - startDate.getTime();
        long days = diffInMillies / (1000 * 60 * 60 * 24);
        if (days == 0) days = 1;
        
        double total = days * dailyPrice;

        String startDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String endDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate);

        // هذا الجزء يفترض وجود فئة BookingDAO للتعامل مع قاعدة البيانات
        boolean success = BookingDAO.createBooking(userId, carId, startDateStr, endDateStr, total);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Booking confirmed successfully!\n" +
                "Car: " + carModel + "\n" +
                "Duration: " + days + " day(s)\n" +
                "Total: $" + (int)total,
                "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create booking. The car may not be available for these dates.",
                "Booking Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // الفئة المساعدة للحدود المستديرة (لتلوين الحدود)
    class RoundedBorder extends AbstractBorder {
        private Color color;
        private int thickness;
        private int radius;
        
        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness/2, y + thickness/2, 
                             width - thickness, height - thickness, 
                             radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        }
    }

    // ⭐️⭐️ الفئة المساعدة لتطبيق الحواف المستديرة على حاوية الصورة ⭐️⭐️
    class RoundedImagePanel extends JPanel {
        private int cornerRadius;
        
        public RoundedImagePanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false); // مهم للسماح بالرسم على الخلفية
            setLayout(new BorderLayout());
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            
            // رسم مستطيل مستدير يملأ الحاوية
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            
            g2.dispose();
            super.paintComponent(g);
        }
        
        // منع رسم أي حواف مربعة
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // قص المحتوى الداخلي بحيث لا يظهر خارج الحواف المستديرة
            g2.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            
            super.paint(g2);
            g2.dispose();
        }
    }
    
    // الفئة المساعدة للزر المستدير
    class RoundedButton extends JButton {
        private int cornerRadius;
        
        public RoundedButton(String text, int radius) {
            super(text);
            this.cornerRadius = radius;
            setContentAreaFilled(false);
            setBorderPainted(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}