package car_rental_system;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.net.URL;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.geom.RoundRectangle2D; // يجب التأكد من وجود هذا الاستيراد إذا تم استخدامه في الفئات المساعدة

public class UserDashboard extends JFrame {
    private int userId;
    private String username;
    private JPanel carsPanel;
    private JTextField searchField; 

    private static final int CARD_WIDTH = 380;
    private static final int CARD_HEIGHT = 480;
    private static final int GAP = 30;
    private static final int COLUMNS = 3;

    public UserDashboard(int userId, String username) {
        this.userId = userId;
        this.username = username;
        initUI();
        loadCars();
    }

    private void initUI() {
        setTitle("DriveElite - Welcome " + username);
        setSize(1460, 950); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(249, 250, 251));

        JPanel sidebar = createSidebar();
        mainContainer.add(sidebar, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(249, 250, 251));

        JPanel header = createHeader();
        rightPanel.add(header, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout()); 
        contentWrapper.setBackground(new Color(249, 250, 251));
        
        carsPanel = new JPanel();
        carsPanel.setLayout(new GridLayout(0, COLUMNS, GAP, GAP)); 
        carsPanel.setBackground(new Color(249, 250, 251));
        
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(new Color(249, 250, 251));
        
        JPanel centerFlow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerFlow.setBackground(new Color(249, 250, 251));
        centerFlow.add(carsPanel);

        // ⭐️⭐️ التعديل هنا لضبط المسافات (أعلى: 30، يسار: 30، أسفل: 30، يمين: 30) ⭐️⭐️
        // لزيادة المسافة العلوية بين الـ Header والكروت، نضع قيمة مناسبة بدلاً من الصفر
        int TOP_PADDING = 30; 
        int HORIZONTAL_PADDING = 30;
        
        centerContainer.setBorder(BorderFactory.createEmptyBorder(TOP_PADDING, HORIZONTAL_PADDING, 30, HORIZONTAL_PADDING)); 
        centerContainer.add(centerFlow, BorderLayout.CENTER);


        JScrollPane scroll = new JScrollPane(centerContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(new Color(249, 250, 251));

        rightPanel.add(scroll, BorderLayout.CENTER);
        mainContainer.add(rightPanel, BorderLayout.CENTER);

        add(mainContainer);
        setVisible(true);
    }
    
    // ... (بقية دوال UserDashboard.java: createSidebar, createMenuButton, createLogoutButton, loadIcon, createHeader, filterCars, loadCars, createCarCard) ...
    // ملاحظة: بما أنك لم ترسل الدوال كاملة، سأفترض أنها موجودة وصحيحة في ملفك.

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BorderLayout()); 
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        JPanel topSidebar = new JPanel();
        topSidebar.setLayout(new BorderLayout());
        topSidebar.setBackground(Color.WHITE);
        
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 30));
        
        JLabel logoIcon;
        try {
            URL logoUrl = getClass().getResource("/images/logo.png");
            if (logoUrl != null) {
                logoIcon = new JLabel(new ImageIcon(logoUrl));
            } else {
                logoIcon = new JLabel("🚗");
            }
        } catch (Exception e) {
            logoIcon = new JLabel("🚗");
        }
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JPanel logoTextPanel = new JPanel();
        logoTextPanel.setLayout(new BoxLayout(logoTextPanel, BoxLayout.Y_AXIS));
        logoTextPanel.setBackground(Color.WHITE);
        
        JLabel logoTitle = new JLabel("DriveElite");
        logoTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoTitle.setForeground(new Color(30, 30, 30));
        
        JLabel logoSubtitle = new JLabel("Car Rental");
        logoSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoSubtitle.setForeground(new Color(120, 120, 120));
        
        logoTextPanel.add(logoTitle);
        logoTextPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        logoTextPanel.add(logoSubtitle);
        
        JPanel logoContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        logoContainer.setBackground(Color.WHITE);
        logoContainer.add(logoIcon);
        logoContainer.add(logoTextPanel);
        logoPanel.add(logoContainer);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton showCarsBtn = createMenuButton("Show Cars", "car.png", true);
        JButton editInfoBtn = createMenuButton("Edit Info", "edit.png", false);
        
        // ⭐️ تصحيح استدعاء EditProfile
        editInfoBtn.addActionListener(e -> {
            try {
                Class.forName("car_rental_system.EditProfile");
                // ⭐️ تمرير مرجع النافذة الحالية (this) لـ EditProfile
                new EditProfile(userId, username, this).setVisible(true);
                // ⭐️ لا نغلق هذه النافذة، EditProfile سيقوم بإخفائها ثم إظهارها عند الانتهاء
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "EditProfile form not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        menuPanel.add(showCarsBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(editInfoBtn);

        topSidebar.add(logoPanel, BorderLayout.NORTH);
        topSidebar.add(menuPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        
        JButton logoutBtn = createLogoutButton("Logout", "logout.png");
        
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Logout", 
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    Class.forName("car_rental_system.LoginForm");
                    new LoginForm().setVisible(true);
                    this.dispose();
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "LoginForm not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        bottomPanel.add(logoutBtn);

        sidebar.add(topSidebar, BorderLayout.NORTH);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createMenuButton(String text, String iconName, boolean selected) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        
        ImageIcon icon = loadIcon(iconName, 18, 18);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(10);
        }

        if (selected) {
            btn.setBackground(new Color(33, 150, 243));
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(100, 100, 100));
            btn.setContentAreaFilled(false);
        }
        
        return btn;
    }
    
    private JButton createLogoutButton(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(220, 53, 69));
        btn.setBackground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        
        ImageIcon icon = loadIcon(iconName, 18, 18);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(5);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
        } else {
            btn.setHorizontalAlignment(SwingConstants.LEFT);
        }

        return btn;
    }
    
    private ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            URL iconUrl = getClass().getResource("/images/" + iconName);
            if (iconUrl != null) {
                ImageIcon originalIcon = new ImageIcon(iconUrl);
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
        }
        return null;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(249, 250, 251));
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(249, 250, 251));

        JLabel title = new JLabel("Available Cars");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(30, 30, 30));

        JLabel subtitle = new JLabel("Browse our premium collection of rental vehicles");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(120, 120, 120));

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(new Color(249, 250, 251));

        searchField = new JTextField(25);
        final String placeholder = "Search by model or description...";
        searchField.setText(placeholder);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(220, 220, 220), 1, 8, 0), // تعديل: تطبيق حواف دائرية على حقل البحث
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(new Color(30, 30, 30));
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterCars();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterCars();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterCars();
            }
        });
        
        JButton filterBtn = new RoundedButton("All Fuel Types", 8); // تعديل: استخدام RoundedButton لزر الفلتر
        filterBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterBtn.setForeground(new Color(80, 80, 80));
        filterBtn.setBackground(Color.WHITE);
        filterBtn.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8, 10)); // border with padding
        filterBtn.setContentAreaFilled(false);
        filterBtn.setFocusPainted(false);
        filterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(searchField);
        searchPanel.add(filterBtn);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        return header;
    }
    
    private void filterCars() {
        String searchText = searchField.getText().trim();
        if (searchText.equals("Search by model or description...")) {
            searchText = "";
        }
        
        carsPanel.removeAll();
        loadCars(searchText); 
        
        carsPanel.revalidate();
        carsPanel.repaint();
    }

    private void loadCars() {
        loadCars(null);
    }
    
    private void loadCars(String filterText) {
        carsPanel.removeAll();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            Class.forName("car_rental_system.DBConnection");
            
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM cars WHERE status = 'Available'";
            
            if (filterText != null && !filterText.isEmpty()) {
                sql += " AND (model_name LIKE ? OR description LIKE ?)";
                pst = conn.prepareStatement(sql);
                String pattern = "%" + filterText + "%";
                pst.setString(1, pattern);
                pst.setString(2, pattern);
            } else {
                pst = conn.prepareStatement(sql);
            }
            
            rs = pst.executeQuery();

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String model = rs.getString("model_name");
                String desc = rs.getString("description");
                double price = rs.getDouble("daily_price");
                String status = rs.getString("status");
                String imgPath = rs.getString("image_path");
                
                int seats = 5;
                String fuelType = "Gasoline";
                String transmission = "Automatic";
                String year = "2024";
                
                try {
                    seats = rs.getInt("seats");
                    fuelType = rs.getString("fuel_type");
                    transmission = rs.getString("transmission");
                    year = rs.getString("year");
                } catch (SQLException e) {
                }

                JPanel carCard = createCarCard(carId, model, desc, price, status, imgPath, 
                                             seats, fuelType, transmission, year);
                carsPanel.add(carCard);
            }
            
            int cardCount = carsPanel.getComponentCount();
            
            int totalWidth = COLUMNS * CARD_WIDTH + (COLUMNS - 1) * GAP;
            int rows = (int) Math.ceil(cardCount / (double)COLUMNS);
            int totalHeight = rows * CARD_HEIGHT + (rows > 0 ? (rows - 1) * GAP : 0) + 30;

            carsPanel.setPreferredSize(new Dimension(totalWidth, totalHeight));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading cars or DB connection missing: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    private JPanel createCarCard(int id, String model, String desc, double price, String status, 
                                 String imgPath, int seats, String fuelType, 
                                 String transmission, String year) {
        // ⭐️⭐️ استخدام RoundedPanel للكارت الرئيسي (الحواف الدائرية) ⭐️⭐️
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        
        // تطبيق حدود خفيفة حول الكارت
        card.setBorder(new RoundedBorder(new Color(230, 230, 230), 1, 12, 0));
        
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // حاوية الصورة الخارجية
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(CARD_WIDTH, 280)); 
        imageContainer.setBackground(new Color(245, 247, 250)); // خلفية الصورة

        // ⭐️⭐️ استخدام ImagePanel لتطبيق الحواف الدائرية على الصورة نفسها ⭐️⭐️
        ImagePanel roundedImagePanel = new ImagePanel(12);
        roundedImagePanel.setLayout(new BorderLayout());
        roundedImagePanel.setBackground(new Color(245, 247, 250));
        imageContainer.add(roundedImagePanel, BorderLayout.CENTER);

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);
        
        // منطق تحميل الصورة
        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                ImageIcon icon = null;
                File imageFile = new File(imgPath);
                
                if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
                    icon = new ImageIcon(new URL(imgPath));
                } else if (imageFile.exists()) {
                    icon = new ImageIcon(imgPath);
                } else {
                    URL imageUrl = getClass().getResource("/" + imgPath); 
                    if (imageUrl != null) {
                        icon = new ImageIcon(imageUrl);
                    }
                }
                
                if (icon != null && icon.getIconWidth() != -1) {
                    // تحجيم الصورة لتناسب مساحة الكارد
                    Image img = icon.getImage().getScaledInstance(CARD_WIDTH, 280, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                } else {
                    imgLabel.setText("Image not found: " + imgPath);
                    imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    imgLabel.setForeground(new Color(220, 38, 38));
                }
            } catch (Exception e) {
                imgLabel.setText("Image Load Error ❌");
                imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                imgLabel.setForeground(new Color(220, 38, 38));
                e.printStackTrace(); 
            }
        } else {
            imgLabel.setText("🚗");
            imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            imgLabel.setForeground(new Color(180, 180, 180));
        }
        
        roundedImagePanel.add(imgLabel, BorderLayout.CENTER);


        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(model);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(new Color(30, 30, 30));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String displayDesc = desc;
        if (desc.length() > 80) {
            displayDesc = desc.substring(0, 77) + "...";
        }
        
        JLabel descLabel = new JLabel("<html>" + displayDesc + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(descLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel priceLabel = new JLabel("$" + (int)price + " /day");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(new Color(33, 150, 243));

        RoundedButton bookBtn = new RoundedButton("Book Now", 8);
        bookBtn.setBackground(new Color(33, 150, 243));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        bookBtn.setPreferredSize(new Dimension(CARD_WIDTH - 40, 42)); 
        bookBtn.setFocusPainted(false);
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        bookBtn.addActionListener(e -> {
            try {
                Class.forName("car_rental_system.BookingForm");
                new BookingForm(userId, id, model, price, imgPath, desc, 
                            seats, fuelType, transmission, year).setVisible(true);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "BookingForm form not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(priceLabel, BorderLayout.NORTH);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        bottomPanel.add(bookBtn, BorderLayout.SOUTH);

        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        detailsPanel.add(bottomPanel, BorderLayout.SOUTH);

        card.add(imageContainer, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserDashboard(1, "John Doe");
        });
    }

    // ⭐️⭐️ الفئات المساعدة (تم تحديث ImagePanel) ⭐️⭐️
    
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        
        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
            setLayout(new BorderLayout());
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    class ImagePanel extends JPanel {
        private int cornerRadius;
        
        public ImagePanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
            setLayout(new BorderLayout());
        }
        
        // ⭐️ تعديل: قص الصورة لإظهار الحواف الدائرية
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // قص المحتوى الداخلي (الصورة)
            g2.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            
            super.paint(g2);
            g2.dispose();
        }
        
        // ⭐️ تم تعديل paintComponent لضمان رسم الخلفية الدائرية بشكل صحيح
         @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            
            // نرسم فقط الجزء العلوي المستدير للصورة داخل الكارد
            g2.fillRoundRect(0, 0, getWidth(), getHeight() + cornerRadius, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }

    class RoundedBorder extends AbstractBorder {
        private Color color;
        private int thickness;
        private int radius;
        private int padding;
        
        public RoundedBorder(Color color, int thickness, int radius, int padding) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
            this.padding = padding;
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
            return new Insets(padding, padding, padding, padding);
        }
    }
    
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
                g2.setColor(new Color(25, 118, 210));
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(66, 165, 245));
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}