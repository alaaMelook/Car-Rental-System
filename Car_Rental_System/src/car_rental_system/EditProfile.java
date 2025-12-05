package car_rental_system;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL; // لإضافة أيقونات الشريط الجانبي
import java.sql.*;

public class EditProfile extends JFrame {

    private int userId;
    private String currentUsername;
    private JTextField txtUsername;
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private JFrame parentFrame; 
    
    // تعريف الألوان والثوابت
    private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color LIGHT_BG = new Color(249, 250, 251); // لون خلفية الـ Dashboard
    private static final Color BORDER_COLOR = new Color(209, 213, 219);
    private static final Color DARK_TEXT = new Color(30, 30, 30);
    private static final int FIELD_WIDTH = 340;
    private static final int FIELD_HEIGHT = 40;
    private static final int CORNER_RADIUS = 10;
    
    // حجم الشاشة مطابق لـ UserDashboard
    private static final int FULL_WIDTH = 1460;
    private static final int FULL_HEIGHT = 950;


    // الدالة الإنشائية تستقبل النافذة الأم
    public EditProfile(int userId, String username, JFrame parentFrame) {
        this.userId = userId;
        this.currentUsername = username;
        this.parentFrame = parentFrame;

        setTitle("Edit Profile - " + username);
        
        setSize(FULL_WIDTH, FULL_HEIGHT);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout()); 
        getContentPane().setBackground(LIGHT_BG);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAndShowParent();
            }
        });

        // 1. إضافة الشريط الجانبي (الغرب)
        add(createSidebar(), BorderLayout.WEST);
        
        // 2. إضافة نموذج التعديل في المنتصف مع توسيط
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        mainContent.add(createContentPanel());
        add(mainContent, BorderLayout.CENTER);

        // عند فتح هذه النافذة، قم بإخفاء النافذة الأم مؤقتاً
        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }
        
        setVisible(true);
    }
    
    // ⭐️ إنشاء Sidebar مشابه لـ UserDashboard
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));
        
        // ** Top Section (Logo + Menu) **
        JPanel topSidebar = new JPanel(new BorderLayout());
        topSidebar.setBackground(Color.WHITE);

        // Logo (Simplified)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30));
        logoPanel.setBackground(Color.WHITE);
        JLabel logoIcon = new JLabel("🚗"); 
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel logoTitle = new JLabel("DriveElite");
        logoTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JPanel logoContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        logoContainer.setBackground(Color.WHITE);
        logoContainer.add(logoIcon);
        logoContainer.add(logoTitle);
        logoPanel.add(logoContainer);

        // Menu Items
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton showCarsBtn = createMenuButton("Show Cars", "car.png", false);
        // ⭐️ زر العرض يعود إلى UserDashboard
        showCarsBtn.addActionListener(e -> closeAndShowParent()); 
        
        JButton editInfoBtn = createMenuButton("Edit Info", "edit.png", true); // حالياً مفعل
        
        menuPanel.add(showCarsBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(editInfoBtn);

        topSidebar.add(logoPanel, BorderLayout.NORTH);
        topSidebar.add(menuPanel, BorderLayout.CENTER);

        // ** Bottom Section (Logout) **
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        bottomPanel.setBackground(Color.WHITE);
        JButton logoutBtn = createLogoutButton("Logout", "logout.png");
        logoutBtn.addActionListener(e -> handleLogout());
        bottomPanel.add(logoutBtn);

        sidebar.add(topSidebar, BorderLayout.NORTH);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Title
        JLabel title = new JLabel("Edit Profile Information", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(DARK_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Update your account details", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(subtitle, gbc);
        
        // Username Field
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 2;
        panel.add(lblUsername, gbc);

        txtUsername = createStyledTextField(currentUsername);
        txtUsername.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 20, 0);
        panel.add(txtUsername, gbc);
        
        // Password Fields
        
        JLabel lblPasswordNote = new JLabel("Change Password (leave blank to keep current password)");
        lblPasswordNote.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPasswordNote.setForeground(DARK_TEXT);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(lblPasswordNote, gbc);

        // New Password
        JLabel lblNewPassword = new JLabel("New Password");
        lblNewPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(lblNewPassword, gbc);

        txtNewPassword = createStyledPasswordField();
        txtNewPassword.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 0, 15, 0);
        panel.add(txtNewPassword, gbc);

        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Confirm New Password");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(lblConfirmPassword, gbc);

        txtConfirmPassword = createStyledPasswordField();
        txtConfirmPassword.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 0, 25, 0);
        panel.add(txtConfirmPassword, gbc);

        // Buttons
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        JButton btnSave = createStyledButton("Save Changes", PRIMARY_BLUE, Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> saveChanges());
        
        JButton btnCancel = createStyledButton("Cancel", BORDER_COLOR, DARK_TEXT);
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.addActionListener(e -> closeAndShowParent());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private void closeAndShowParent() {
        dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }
    
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", 
            JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                // إغلاق النافذة الحالية
                dispose();
                // إذا كان هناك نافذة أم، نخفيها (لتجنب مشاكل العرض)
                if(parentFrame != null) {
                    parentFrame.dispose(); 
                }
                Class.forName("car_rental_system.LoginForm");
                new LoginForm().setVisible(true);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "LoginForm not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void saveChanges() {
        String newUsername = txtUsername.getText().trim();
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // 1. التحقق من اسم المستخدم
        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newUsername.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 2. التحقق من كلمة المرور
        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, "New password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // 3. التحقق النهائي قبل محاولة الاتصال بـ DB
        if (newUsername.equals(currentUsername) && newPassword.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No changes detected.", "Info", JOptionPane.INFORMATION_MESSAGE);
             closeAndShowParent();
             return;
        }
        
        // 4. محاولة الحفظ
        try {
            Class.forName("car_rental_system.DBConnection");
            
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE users SET username = ?";
            
            if (!newPassword.isEmpty()) {
                sql += ", password = ?";
            }
            
            sql += " WHERE user_id = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newUsername);
            
            if (!newPassword.isEmpty()) {
                pst.setString(2, newPassword);
                pst.setInt(3, userId);
            } else {
                pst.setInt(2, userId);
            }

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                this.currentUsername = newUsername; 
                // عند النجاح، نغلق ونعود للرئيسية
                closeAndShowParent(); 
            } else {
                 JOptionPane.showMessageDialog(this, "Failed to update profile. User ID or data issue.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.close();
        } catch (ClassNotFoundException ex) {
             JOptionPane.showMessageDialog(this, "DBConnection class not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Username is already taken. Choose another one.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // لا نغلق النافذة في حالة الخطأ
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // ------------------------------------
    // Helper Methods for Styling and Sidebar
    // ------------------------------------
    
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
            btn.setBackground(PRIMARY_BLUE);
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
    
    private JTextField createStyledTextField(String initialText) {
        JTextField field = new JTextField(initialText);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(DARK_TEXT);
        field.setBorder(new CompoundBorder(new RoundedLineBorder(BORDER_COLOR, 1, CORNER_RADIUS), new EmptyBorder(5, 10, 5, 10)));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(new RoundedLineBorder(PRIMARY_BLUE, 2, CORNER_RADIUS), new EmptyBorder(4, 9, 4, 9)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(new RoundedLineBorder(BORDER_COLOR, 1, CORNER_RADIUS), new EmptyBorder(5, 10, 5, 10)));
            }
        });
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(new RoundedLineBorder(BORDER_COLOR, 1, CORNER_RADIUS), new EmptyBorder(5, 10, 5, 10)));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(new RoundedLineBorder(PRIMARY_BLUE, 2, CORNER_RADIUS), new EmptyBorder(4, 9, 4, 9)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(new RoundedLineBorder(BORDER_COLOR, 1, CORNER_RADIUS), new EmptyBorder(5, 10, 5, 10)));
            }
        });
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(bgColor, 1, true));
        return button;
    }

    private static class RoundedLineBorder extends LineBorder {
        private int radius;

        public RoundedLineBorder(Color color, int thickness, int radius) {
            super(color, thickness, true);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getLineColor());
            
            for (int i = 0; i < getThickness(); i++) {
                g2.drawRoundRect(x + i, y + i, width - i * 2 - 1, height - i * 2 - 1, radius, radius);
            }
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int th = getThickness();
            return new Insets(th, th, th, th);
        }
    }

    public static void main(String[] args) {
        // يجب أن نرسل null هنا لأنها تعمل كدالة اختبار مستقلة
        SwingUtilities.invokeLater(() -> new EditProfile(1, "testuser123", null));
    }
}