package car_rental_system;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class RegisterForm extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;
    private JButton loginTab;
    private JButton registerTab;

    public RegisterForm() {
        setTitle("DriveElite - Car Rental System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                String imagePath = "images/logen.jpg"; 
                
                try {
                    ImageIcon icon = null;
                    URL imageUrl = getClass().getResource("/" + imagePath);
                    
                    if (imageUrl != null) {
                        icon = new ImageIcon(imageUrl);
                    } else {
                        throw new Exception("Resource not found or failed to load");
                    }
                    
                    Image img = icon.getImage();
                    g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    
                    g2d.setColor(new Color(0, 0, 0, 80));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } catch (Exception e) {
                    g2d.setColor(new Color(30, 40, 50));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        leftPanel.setBounds(0, 0, 650, 700);
        leftPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Premium Car Rental");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 580, 500, 45);
        leftPanel.add(titleLabel);

        add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(650, 0, 550, 700);
        rightPanel.setLayout(null);

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(33, 150, 243));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setBounds(120, 80, 55, 55);
        logoPanel.setLayout(new BorderLayout());

        JLabel logoIcon = new JLabel("🚗", SwingConstants.CENTER);
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoIcon.setForeground(Color.WHITE);
        logoPanel.add(logoIcon);
        rightPanel.add(logoPanel);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBounds(185, 85, 250, 50);

        JLabel appName = new JLabel("DriveElite");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appName.setForeground(new Color(30, 30, 30));
        appName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel appSubtitle = new JLabel("Car Rental System");
        appSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appSubtitle.setForeground(new Color(120, 120, 120));
        appSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(appName);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        titlePanel.add(appSubtitle);
        rightPanel.add(titlePanel);

        JPanel tabPanel = new JPanel();
        tabPanel.setBackground(Color.WHITE);
        tabPanel.setBounds(120, 180, 310, 50);
        tabPanel.setLayout(null);

        loginTab = new JButton("Login");
        loginTab.setBounds(0, 0, 155, 50);
        loginTab.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        loginTab.setForeground(new Color(120, 120, 120));
        loginTab.setBackground(Color.WHITE);
        loginTab.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        loginTab.setFocusPainted(false);
        loginTab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginTab.setContentAreaFilled(false);
        loginTab.setOpaque(true);
        
        loginTab.addActionListener(e -> switchToLogin());
        
        tabPanel.add(loginTab);

        registerTab = new JButton("Register");
        registerTab.setBounds(155, 0, 155, 50);
        registerTab.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerTab.setForeground(new Color(33, 150, 243));
        registerTab.setBackground(Color.WHITE);
        registerTab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(33, 150, 243)));
        registerTab.setFocusPainted(false);
        registerTab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerTab.setContentAreaFilled(false);
        registerTab.setOpaque(true);
        tabPanel.add(registerTab);

        rightPanel.add(tabPanel);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(60, 60, 60));
        userLabel.setBounds(120, 260, 100, 20);
        rightPanel.add(userLabel);

        JPanel userFieldPanel = new RoundedPanel(8);
        userFieldPanel.setBackground(Color.WHITE);
        userFieldPanel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8));
        userFieldPanel.setBounds(120, 285, 310, 48);
        userFieldPanel.setLayout(new BorderLayout());
        
        // تحميل صورة user.jpeg
        JLabel userIcon = new JLabel();
        try {
            URL userUrl = getClass().getResource("/images/user.png");
            if (userUrl != null) {
                ImageIcon originalIcon = new ImageIcon(userUrl);
                Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                userIcon.setIcon(new ImageIcon(img));
                userIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
            } else {
                userIcon.setText(" 👤 ");
                userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                userIcon.setForeground(new Color(150, 150, 150));
                userIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
            }
        } catch (Exception e) {
            userIcon.setText(" 👤 ");
            userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            userIcon.setForeground(new Color(150, 150, 150));
            userIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        }

        userFieldPanel.add(userIcon, BorderLayout.WEST);

        userField = new JTextField("Enter your username");
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userField.setForeground(new Color(180, 180, 180));
        userField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        userField.setOpaque(false);

        userField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (userField.getText().equals("Enter your username")) {
                    userField.setText("");
                    userField.setForeground(new Color(33, 33, 33));
                }
                userFieldPanel.setBorder(new RoundedBorder(new Color(33, 150, 243), 2, 8));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (userField.getText().isEmpty()) {
                    userField.setText("Enter your username");
                    userField.setForeground(new Color(180, 180, 180));
                }
                userFieldPanel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8));
            }
        });

        userFieldPanel.add(userField, BorderLayout.CENTER);
        rightPanel.add(userFieldPanel);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setForeground(new Color(60, 60, 60));
        passLabel.setBounds(120, 350, 100, 20);
        rightPanel.add(passLabel);

        JPanel passFieldPanel = new RoundedPanel(8);
        passFieldPanel.setBackground(Color.WHITE);
        passFieldPanel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8));
        passFieldPanel.setBounds(120, 375, 310, 48);
        passFieldPanel.setLayout(new BorderLayout());

        // تحميل صورة lock.png
        JLabel passIcon = new JLabel();
        try {
            URL lockUrl = getClass().getResource("/images/lock.png");
            if (lockUrl != null) {
                ImageIcon originalIcon = new ImageIcon(lockUrl);
                Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                passIcon.setIcon(new ImageIcon(img));
                passIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
            } else {
                passIcon.setText(" 🔒 ");
                passIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                passIcon.setForeground(new Color(150, 150, 150));
                passIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
            }
        } catch (Exception e) {
            passIcon.setText(" 🔒 ");
            passIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            passIcon.setForeground(new Color(150, 150, 150));
            passIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        }

        passFieldPanel.add(passIcon, BorderLayout.WEST);

        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        passField.setOpaque(false);
        passField.setEchoChar('•');
        
        passField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passFieldPanel.setBorder(new RoundedBorder(new Color(33, 150, 243), 2, 8));
            }

            @Override
            public void focusLost(FocusEvent e) {
                passFieldPanel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8));
            }
        });
        
        passFieldPanel.add(passField, BorderLayout.CENTER);
        rightPanel.add(passFieldPanel);

        // Confirm Password
        JLabel confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmPassLabel.setForeground(new Color(60, 60, 60));
        confirmPassLabel.setBounds(120, 440, 120, 20);
        rightPanel.add(confirmPassLabel);

        JPanel confirmPassFieldPanel = new RoundedPanel(8);
        confirmPassFieldPanel.setBackground(Color.WHITE);
        confirmPassFieldPanel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8));
        confirmPassFieldPanel.setBounds(120, 465, 310, 48);
        confirmPassFieldPanel.setLayout(new BorderLayout());

        // تحميل صورة lock.png
        JLabel confirmPassIcon = new JLabel();
        try {
            URL lockUrl = getClass().getResource("/images/lock.png");
            if (lockUrl != null) {
                ImageIcon originalIcon = new ImageIcon(lockUrl);
                Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                confirmPassIcon.setIcon(new ImageIcon(img));
                confirmPassIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
            } else {
                confirmPassIcon.setText(" 🔒 ");
                confirmPassIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                confirmPassIcon.setForeground(new Color(150, 150, 150));
                confirmPassIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
            }
        } catch (Exception e) {
            confirmPassIcon.setText(" 🔒 ");
            confirmPassIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            confirmPassIcon.setForeground(new Color(150, 150, 150));
            confirmPassIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        }

        confirmPassFieldPanel.add(confirmPassIcon, BorderLayout.WEST);

        confirmPassField = new JPasswordField();
        confirmPassField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmPassField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        confirmPassField.setOpaque(false);
        confirmPassField.setEchoChar('•');
        
        confirmPassField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                confirmPassFieldPanel.setBorder(new RoundedBorder(new Color(33, 150, 243), 2, 8));
            }

            @Override
            public void focusLost(FocusEvent e) {
                confirmPassFieldPanel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 8));
            }
        });
        
        confirmPassFieldPanel.add(confirmPassField, BorderLayout.CENTER);
        rightPanel.add(confirmPassFieldPanel);

        // Create Account Button
        JButton createAccountBtn = new RoundedButton("Create Account", 8);
        createAccountBtn.setBounds(120, 540, 310, 48);
        createAccountBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        createAccountBtn.setForeground(Color.WHITE);
        createAccountBtn.setBackground(new Color(33, 150, 243));
        createAccountBtn.setFocusPainted(false);
        createAccountBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        createAccountBtn.addActionListener(e -> handleRegister());
        confirmPassField.addActionListener(e -> handleRegister());

        rightPanel.add(createAccountBtn);

        // Login Link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBounds(120, 605, 310, 30);

        JLabel haveAccountLabel = new JLabel("Already have an account?");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        haveAccountLabel.setForeground(new Color(100, 100, 100));
        loginPanel.add(haveAccountLabel);

        JLabel loginLink = new JLabel("Login here");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setForeground(new Color(33, 150, 243));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchToLogin();
            }
        });
        loginPanel.add(loginLink);
        rightPanel.add(loginPanel);

        add(rightPanel);
        setVisible(true);
    }

    private void switchToLogin() {
        new LoginForm().setVisible(true);
        this.dispose();
    }

    private void handleRegister() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        String confirmPassword = new String(confirmPassField.getPassword());

        if (username.isEmpty() || username.equals("Enter your username")) {
            showStyledMessage(
                "Please enter your username!",
                "Missing Username",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (username.length() < 3) {
            showStyledMessage(
                "Username must be at least 3 characters long.",
                "Invalid Username",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (password.isEmpty()) {
            showStyledMessage(
                "Please enter your password!",
                "Missing Password",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (password.length() < 6) {
            showStyledMessage(
                "Password must be at least 6 characters long.",
                "Weak Password",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            showStyledMessage(
                "Passwords do not match! Please try again.",
                "Password Mismatch",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // محاولة التسجيل في قاعدة البيانات
        boolean success = UserDAO.registerUser(username, password);

        if (success) {
            showStyledMessage(
                "Account created successfully!\n\n" +
                "Username: " + username + "\n" +
                "You can now login to DriveElite.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            Timer timer = new Timer(500, e -> switchToLogin());
            timer.setRepeats(false);
            timer.start();
        } else {
            showStyledMessage(
                "This username is already taken.\n" +
                "Please choose a different username.",
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE
            );
            userField.setText("");
            passField.setText("");
            confirmPassField.setText("");
            userField.requestFocus();
        }
    }

    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", new Color(30, 30, 30));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.background", new Color(33, 150, 243));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            messageType
        );
        
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.font", null);
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;
        
        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterForm());
    }
}