package car_rental_system;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.geom.RoundRectangle2D; // لتطبيق الحواف الدائرية على الصور

public class AdminDashboard extends JFrame {

    private JPanel carsPanel;
    private JPanel scrollContainer;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private String selectedMenu = "Manage Cars";
    private JPanel sidebar;

    private static final int CARD_WIDTH = 340;
    private static final int CARD_HEIGHT = 420;
    private static final int GAP = 20;

    // ⭐️ دالة ثابتة لتحميل الأيقونات من مسار الموارد (Resources)
    private static ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            URL iconUrl = AdminDashboard.class.getResource("/images/" + iconName);
            if (iconUrl != null) {
                ImageIcon originalIcon = new ImageIcon(iconUrl);
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
             // System.out.println("Error loading icon: " + iconName); 
        }
        return null;
    }

    public AdminDashboard() {
        setTitle("DriveElite - Admin Panel");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 250, 252));

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(248, 250, 252));

        sidebar = createSidebar();
        JPanel contentArea = createContentArea();

        mainContainer.add(sidebar, BorderLayout.WEST);
        mainContainer.add(contentArea, BorderLayout.CENTER);

        add(mainContainer);

        showCars();

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 900));
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(229, 231, 235)));

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] menuTexts = { "Manage Cars", "View Bookings", "Manage Users" };
        String[] iconNames = { "car-icon.png", "booking-icon.png", "user-icon.png" };
        
        for (int i = 0; i < menuTexts.length; i++) {
            String text = menuTexts[i];
            String iconName = iconNames[i];
            
            JPanel menuItem = createMenuItem(text, iconName, text.equals(selectedMenu), () -> {
                updateMenuSelection(text);
                switch (text) {
                    case "Manage Cars":
                        showCars();
                        break;
                    case "View Bookings":
                        openViewBookings();
                        break;
                    case "Manage Users":
                        showUsers();
                        break;
                }
            });
            sidebar.add(menuItem);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebar.add(Box.createVerticalGlue());

        // ⭐️ زر تسجيل الخروج مع أيقونة logout.png
        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(Color.WHITE);
        logoutPanel.setMaximumSize(new Dimension(220, 60));
        logoutPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton logoutBtn = createSidebarLogoutButton("Logout", "logout.png"); 

        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                try {
                    Class.forName("car_rental_system.LoginForm");
                    new LoginForm().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "LoginForm class not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutPanel.add(logoutBtn);
        sidebar.add(logoutPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }
    
    // ⭐️ دالة مُعدلة لإنشاء أزرار القائمة الجانبية (مع الأيقونات)
    private JPanel createMenuItem(String text, String iconName, boolean selected, Runnable action) {
        JPanel menuItem = new JPanel();
        menuItem.setMaximumSize(new Dimension(220, 45));
        menuItem.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));

        Color selectedBg = new Color(239, 246, 255);
        Color defaultBg = Color.WHITE;
        Color selectedFg = new Color(59, 130, 246);
        Color defaultFg = new Color(71, 85, 105);

        menuItem.setBackground(selected ? selectedBg : defaultBg);
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon icon = loadIcon(iconName, 18, 18);
        JLabel iconLabel = new JLabel(icon);
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        if (selected) {
            label.setForeground(selectedFg);
            label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        } else {
            label.setForeground(defaultFg);
        }

        menuItem.add(iconLabel);
        menuItem.add(Box.createRigidArea(new Dimension(5, 0)));
        menuItem.add(label);

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!text.equals(selectedMenu)) menuItem.setBackground(new Color(249, 250, 252));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!text.equals(selectedMenu)) menuItem.setBackground(defaultBg);
            }
        });

        return menuItem;
    }
    
    private JButton createSidebarLogoutButton(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(new Color(239, 68, 68));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        ImageIcon icon = loadIcon(iconName, 18, 18);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }

        return btn;
    }
    
    private void updateMenuSelection(String newSelection) {
        selectedMenu = newSelection;
        Container parent = sidebar.getParent();
        if (parent != null) {
            parent.remove(sidebar);
            sidebar = createSidebar();
            parent.add(sidebar, BorderLayout.WEST);
            parent.revalidate();
            parent.repaint();
        }
    }

    private JPanel createContentArea() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(248, 250, 252));
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 250, 252));
        header.setMaximumSize(new Dimension(1400, 120));

        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setBackground(new Color(248, 250, 252));

        JLabel headerLogo = new JLabel("🚗 DriveElite");
        headerLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLogo.setForeground(new Color(59, 130, 246));
        headerLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleSection.add(headerLogo);
        titleSection.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel title = new JLabel("Manage All Cars");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(15, 23, 42));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Add, edit, or remove vehicles from your rental fleet");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleSection.add(title);
        titleSection.add(Box.createRigidArea(new Dimension(0, 5)));
        titleSection.add(subtitle);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        controlsPanel.setBackground(new Color(248, 250, 252));

        // ⭐️ Search field setup with RoundedBorder
        searchField = new JTextField("Search by model, ID, or status...");
        final String placeholder = "Search by model, ID, or status...";
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(148, 163, 184));
        searchField.setPreferredSize(new Dimension(350, 42));
        searchField.setBorder(new RoundedBorder(new Color(226, 232, 240), 1, 8, 10)); 

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(new Color(30, 30, 30));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(new Color(148, 163, 184));
                }
            }
        });

        // ⭐️ تفعيل الفلترة عند تغيير النص
        searchField.addActionListener(e -> loadAllCars());
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { loadAllCars(); }
            public void removeUpdate(DocumentEvent e) { loadAllCars(); }
            public void insertUpdate(DocumentEvent e) { loadAllCars(); }
        });

        String[] statuses = {"All Status", "Available", "Booked", "Maintenance", "Inactive"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.setPreferredSize(new Dimension(140, 42));
        statusFilter.setBackground(Color.WHITE);
        statusFilter.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        statusFilter.addActionListener(e -> loadAllCars());

        // ⭐️ زر Add New Car بحواف دائرية
        JButton addBtn = new RoundedButton("+ Add New Car", 8); 
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(new Color(59, 130, 246));
        addBtn.setPreferredSize(new Dimension(150, 42));
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> openAddNewCar());

        controlsPanel.add(searchField);
        controlsPanel.add(statusFilter);
        controlsPanel.add(addBtn);

        header.add(titleSection, BorderLayout.WEST);
        header.add(controlsPanel, BorderLayout.EAST);

        // ⭐️ تغيير التنسيق إلى GridLayout لعرض 3 كروت في الصف مع تحجيم مناسب
        carsPanel = new JPanel();
        carsPanel.setLayout(new GridLayout(0, 3, GAP, GAP)); 
        carsPanel.setBackground(new Color(248, 250, 252));
        
        JPanel centerFlow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerFlow.setBackground(new Color(248, 250, 252));
        centerFlow.add(carsPanel);

        JScrollPane scroll = new JScrollPane(centerFlow);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(new Color(248, 250, 252));

        scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setBackground(new Color(248, 250, 252));
        scrollContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 
        scrollContainer.add(scroll, BorderLayout.CENTER);

        content.add(header, BorderLayout.NORTH);
        content.add(scrollContainer, BorderLayout.CENTER);

        return content;
    }

    public void loadAllCars() {
        carsPanel.removeAll();

        String searchText = searchField.getText().trim();
        final String placeholder = "Search by model, ID, or status...";
        if (searchText.equals(placeholder)) searchText = ""; 

        String selectedStatus = (String) statusFilter.getSelectedItem();
        boolean hasStatusFilter = selectedStatus != null && !selectedStatus.equals("All Status");

        try (Connection conn = DBConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("SELECT * FROM cars");
            List<Object> params = new ArrayList<>();
            List<String> where = new ArrayList<>();

            if (!searchText.isEmpty()) {
                where.add("(model_name LIKE ? OR CAST(car_id AS CHAR) = ? OR status LIKE ?)");
                String like = "%" + searchText + "%";
                params.add(like);
                params.add(searchText);
                params.add(like);
            }

            if (hasStatusFilter) {
                where.add("status = ?");
                params.add(selectedStatus);
            }

            if (!where.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", where));
            }

            sql.append(" ORDER BY car_id DESC");

            PreparedStatement pst = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String model = rs.getString("model_name");
                String desc = rs.getString("description");
                double price = rs.getDouble("daily_price");
                String status = rs.getString("status");
                String imgPath = rs.getString("image_path");

                JPanel carCard = createCarCard(carId, model, desc, price, status, imgPath);
                carsPanel.add(carCard);
            }
            
            // ⭐️ ضبط حجم الـ carsPanel
            int cardCount = carsPanel.getComponentCount();
            int columns = 3; 
            int rows = (int) Math.ceil(cardCount / (double)columns);
            int totalWidth = columns * CARD_WIDTH + (columns - 1) * GAP;
            int totalHeight = rows * CARD_HEIGHT + (rows > 0 ? (rows - 1) * GAP : 0);
            carsPanel.setPreferredSize(new Dimension(totalWidth, totalHeight));

            carsPanel.revalidate();
            carsPanel.repaint();
            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cars: " + e.getMessage());
        }
    }

    private JPanel createCarCard(int id, String model, String desc, double price, String status, String imgPath) {
        // ⭐️ RoundedPanel
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new RoundedBorder(new Color(226, 232, 240), 1, 12, 0));

        Dimension cardSize = new Dimension(CARD_WIDTH, CARD_HEIGHT);
        card.setPreferredSize(cardSize);
        card.setMinimumSize(cardSize);
        card.setMaximumSize(cardSize);

        // ⭐️ ImagePanel للحواف الدائرية للصورة
        ImagePanel imagePanel = new ImagePanel(12, true); 
        imagePanel.setPreferredSize(new Dimension(CARD_WIDTH, 180));
        imagePanel.setBackground(new Color(241, 245, 249));
        imagePanel.setLayout(new BorderLayout());

        JLabel statusBadge = new JLabel(getStatusBadgeText(status));
        statusBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statusBadge.setForeground(getStatusColor(status));
        statusBadge.setBackground(getStatusBgColor(status));
        statusBadge.setOpaque(true);
        statusBadge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBadge.putClientProperty("JComponent.roundRect", Boolean.TRUE); 

        JPanel badgeContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        badgeContainer.setOpaque(false);
        badgeContainer.add(statusBadge);

        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                ImageIcon icon = null;
                
                if (imgPath.startsWith("http://") || imgPath.startsWith("https://")) {
                    icon = new ImageIcon(new URL(imgPath));
                } 
                
                // ⭐️ التحميل من موارد المشروع
                if (icon == null) {
                    URL imageUrl = getClass().getResource("/" + imgPath); 
                    if (imageUrl != null) {
                        icon = new ImageIcon(imageUrl);
                    }
                }
                
                // التحميل كملف عادي
                if (icon == null) {
                    File imageFile = new File(imgPath);
                    if (imageFile.exists()) {
                         icon = new ImageIcon(imgPath);
                    }
                }
                
                if (icon != null && icon.getIconWidth() != -1) {
                    Image img = icon.getImage().getScaledInstance(CARD_WIDTH, 180, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(img));
                    imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imagePanel.add(imgLabel, BorderLayout.CENTER);
                } else {
                    JLabel placeholder = new JLabel("❌ Image Not Found", SwingConstants.CENTER);
                    placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    placeholder.setForeground(new Color(239, 68, 68));
                    imagePanel.add(placeholder, BorderLayout.CENTER);
                }
            } catch (Exception e) {
                JLabel placeholder = new JLabel("🚗", SwingConstants.CENTER);
                placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 55));
                placeholder.setForeground(new Color(148, 163, 184));
                imagePanel.add(placeholder, BorderLayout.CENTER);
            }
        } else {
            JLabel placeholder = new JLabel("🚗", SwingConstants.CENTER);
            placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 55));
            placeholder.setForeground(new Color(148, 163, 184));
            imagePanel.add(placeholder, BorderLayout.CENTER);
        }

        imagePanel.add(badgeContainer, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel nameLabel = new JLabel(model);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(15, 23, 42));

        JTextArea descArea = new JTextArea(desc);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setForeground(new Color(100, 116, 139));
        descArea.setBorder(BorderFactory.createEmptyBorder(6, 0, 12, 0));
        descArea.setRows(2);

        JLabel priceLabel = new JLabel("$" + (int) price + " /day");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLabel.setForeground(new Color(59, 130, 246));

        JLabel statusLabel = new JLabel(getStatusText(status));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 116, 139)); 

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        // ⭐️ زر Edit مع أيقونة edit.png
        JButton editBtn = new RoundedButton("Edit", 8);
        ImageIcon editIcon = loadIcon("edit.png", 14, 14); 
        if (editIcon != null) editBtn.setIcon(editIcon);
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBackground(new Color(59, 130, 246));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> editCar(id));

        // ⭐️ زر Delete مع أيقونة delete.png
        JButton deleteBtn = new RoundedButton("Delete", 8);
        ImageIcon deleteIcon = loadIcon("delete.png", 14, 14); 
        if (deleteIcon != null) deleteBtn.setIcon(deleteIcon);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteBtn.setForeground(new Color(239, 68, 68));
        deleteBtn.setBackground(Color.WHITE);
        deleteBtn.setBorder(new RoundedBorder(new Color(239, 68, 68), 1, 8, 7));
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteCar(id));

        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);

        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setBackground(Color.WHITE);
        topInfo.add(nameLabel, BorderLayout.NORTH);
        topInfo.add(descArea, BorderLayout.CENTER);

        JPanel priceStatusPanel = new JPanel(new BorderLayout());
        priceStatusPanel.setBackground(Color.WHITE);
        priceStatusPanel.add(priceLabel, BorderLayout.WEST);
        priceStatusPanel.add(statusLabel, BorderLayout.EAST);

        infoPanel.add(topInfo, BorderLayout.NORTH);
        infoPanel.add(priceStatusPanel, BorderLayout.CENTER);
        infoPanel.add(buttonsPanel, BorderLayout.SOUTH);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private String getStatusBadgeText(String status) {
        if (status == null) return "Inactive";
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }

    private Color getStatusColor(String status) {
        if (status == null) return new Color(100, 116, 139);
        switch (status.toLowerCase()) {
            case "available":
                return new Color(22, 163, 74); 
            case "booked":
                return new Color(59, 130, 246); 
            case "maintenance":
                return new Color(234, 179, 8); 
            default:
                return new Color(100, 116, 139);
        }
    }

    private Color getStatusBgColor(String status) {
        if (status == null) return new Color(241, 245, 249);
        switch (status.toLowerCase()) {
            case "available":
                return new Color(220, 252, 231);
            case "booked":
                return new Color(219, 234, 254);
            case "maintenance":
                return new Color(254, 249, 195);
            default:
                return new Color(241, 245, 249);
        }
    }

    private String getStatusText(String status) {
        if (status == null) return "Inactive";
        switch (status.toLowerCase()) {
            case "available":
                return "Currently Available";
            case "booked":
                return "On Rental"; 
            case "maintenance":
                return "Under Maintenance";
            default:
                return "Inactive";
        }
    }

    // ⭐️ استعادة كود فتح EditCarDialog
    private void editCar(int carId) {
        try {
            Class.forName("car_rental_system.EditCarDialog");
            EditCarDialog dialog = new EditCarDialog(this, carId);
            dialog.setVisible(true);
            showCars();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "EditCarDialog form not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCar(int carId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this car?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pst = conn.prepareStatement("DELETE FROM cars WHERE car_id = ?")) {
                pst.setInt(1, carId);
                pst.executeUpdate();
                showCars();
                JOptionPane.showMessageDialog(this, "Car deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting car: " + ex.getMessage());
            }
        }
    }

    private void showCars() {
        loadAllCars();

        scrollContainer.removeAll();
        scrollContainer.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(carsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scrollContainer.add(scroll, BorderLayout.CENTER);
        scrollContainer.revalidate();
        scrollContainer.repaint();
    }

    // ⭐️ استعادة كود فتح AddNewCarDialog
    private void openAddNewCar() {
        try {
            Class.forName("car_rental_system.AddNewCarDialog");
            AddNewCarDialog dlg = new AddNewCarDialog(this);
            dlg.setModal(true);
            dlg.setVisible(true);
            loadAllCars();
            showCars();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "AddNewCarDialog form not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open Add New Car page: " + ex.getMessage());
        }
    }

    // ⭐️ استعادة كود فتح ViewBookingsPanel
    private void openViewBookings() {
        try {
            Class<?> cls = Class.forName("car_rental_system.ViewBookingsPanel");
            Object obj = cls.getDeclaredConstructor().newInstance();
            if (obj instanceof JPanel) {
                JPanel bookingsPanel = (JPanel) obj;
                scrollContainer.removeAll();
                scrollContainer.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.NORTH);
                JScrollPane scroll = new JScrollPane(bookingsPanel);
                scroll.setBorder(null);
                scroll.getVerticalScrollBar().setUnitIncrement(16);
                scrollContainer.add(scroll, BorderLayout.CENTER);
                scrollContainer.revalidate();
                scrollContainer.repaint();
            } else if (obj instanceof JFrame) {
                ((JFrame) obj).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "ViewBookingsPanel found but is not a JPanel or JFrame.");
            }
        } catch (ClassNotFoundException cnf) {
            JOptionPane.showMessageDialog(this, "ViewBookingsPanel class not found in project.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening View Bookings: " + ex.getMessage());
        }
    }

    // ⭐️ استعادة كود فتح ManageUsersPanel
    private void showUsers() {
        try {
            Class.forName("car_rental_system.ManageUsersPanel");
            ManageUsersPanel usersPanel = new ManageUsersPanel();
            scrollContainer.removeAll();
            scrollContainer.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.NORTH);
            JScrollPane scroll = new JScrollPane(usersPanel);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scrollContainer.add(scroll, BorderLayout.CENTER);
            scrollContainer.revalidate();
            scrollContainer.repaint();
        } catch (ClassNotFoundException cnf) {
             JOptionPane.showMessageDialog(this, "ManageUsersPanel class not found in project.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening User Dashboard: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard();
        });
    }

    // ⭐️⭐️ الفئات المساعدة للجماليات (الحواف الدائرية) ⭐️⭐️

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
        private boolean topOnly;
        
        public ImagePanel(int radius, boolean topOnly) {
            this.cornerRadius = radius;
            this.topOnly = topOnly;
            setOpaque(false);
            setLayout(new BorderLayout());
        }
        
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (topOnly) {
                 g2.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() + cornerRadius, cornerRadius, cornerRadius));
            } else {
                 g2.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            }
            
            super.paint(g2);
            g2.dispose();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            
            if (topOnly) {
                 g2.fillRoundRect(0, 0, getWidth(), getHeight() + cornerRadius, cornerRadius, cornerRadius);
            } else {
                 g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            }
            
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
                g2.setColor(new Color(45, 110, 200)); 
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