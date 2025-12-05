package car_rental_system;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class ManageUsersPanel extends JPanel {

    private JTable usersTable;
    private DefaultTableModel tableModel;

    public ManageUsersPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Manage Users");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 30, 30));

        JLabel subtitle = new JLabel("View users and change their roles");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 120, 120));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(title);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 5)));
        leftHeader.add(subtitle);
        header.add(leftHeader, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        try {
            createTable();
            loadUsers();
        } catch (Throwable t) {
            // إذا حصل أي خطأ أثناء بناء اللوحة أو تحميل المستخدمين
            t.printStackTrace();
            removeAll();
            JLabel err = new JLabel("<html><b>Error loading Manage Users panel:</b><br>" 
                    + escapeHtml(t.getClass().getSimpleName() + ": " + t.getMessage()) 
                    + "<br>Check console for full stack trace.</html>");
            err.setForeground(Color.RED);
            err.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            err.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            add(err, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }

    private void createTable() {
        String[] cols = {"User ID", "Username", "Role", "Actions"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // allow editing of role column only and actions
                return column == 2 || column == 3;
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setRowHeight(48);
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usersTable.setShowGrid(true);
        usersTable.setGridColor(new Color(230, 230, 230));

        // role column: use JComboBox editor
        String[] roles = {"USER", "ADMIN"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        DefaultCellEditor roleEditor = new DefaultCellEditor(roleCombo);
        usersTable.getColumnModel().getColumn(2).setCellEditor(roleEditor);

        // actions column: show Save button
        usersTable.getColumnModel().getColumn(3).setCellRenderer(new ActionsRenderer());
        usersTable.getColumnModel().getColumn(3).setCellEditor(new ActionsEditor());

        JScrollPane scroll = new JScrollPane(usersTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scroll, BorderLayout.CENTER);
    }

    public void loadUsers() throws SQLException {
        tableModel.setRowCount(0);
        String sql = "SELECT user_id, username, role FROM users ORDER BY user_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    "Save"
                };
                tableModel.addRow(row);
            }
        }
    }

    // Renderer for the Actions column (shows a Save button)
    class ActionsRenderer extends DefaultTableCellRenderer {
        private final JButton btn = new JButton("Save");
        public ActionsRenderer() {
            btn.setBackground(new Color(59, 130, 246));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return btn;
        }
    }

    // Editor for Actions column (handles saving role change)
    class ActionsEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton btn = new JButton("Save");

        public ActionsEditor() {
            btn.setBackground(new Color(59, 130, 246));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            btn.addActionListener(e -> {
                int row = usersTable.getSelectedRow();
                if (row >= 0) {
                    int userId = (int) tableModel.getValueAt(row, 0);
                    String newRole = (String) tableModel.getValueAt(row, 2);
                    updateUserRole(userId, newRole, row);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return btn;
        }

        @Override
        public Object getCellEditorValue() {
            return "Save";
        }
    }

    private void updateUserRole(int userId, String role, int row) {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, role);
            pst.setInt(2, userId);
            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Role updated successfully for user ID " + userId, "Success", JOptionPane.INFORMATION_MESSAGE);
                // reload users to reflect DB state
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "No rows updated. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating role: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}