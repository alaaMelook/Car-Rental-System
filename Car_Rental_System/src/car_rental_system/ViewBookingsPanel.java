package car_rental_system;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class ViewBookingsPanel extends JPanel {
private JTable bookingsTable;
private DefaultTableModel tableModel;
private JTextField searchField;
private JComboBox<String> statusFilter;
// optional label to show how many rows are displayed
private JLabel showingLabel;

public ViewBookingsPanel() {
    setLayout(new BorderLayout());
    setBackground(new Color(249, 250, 251));
    setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    // Header Panel
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(249, 250, 251));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

    // Title Section
    JPanel titleSection = new JPanel();
    titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
    titleSection.setBackground(new Color(249, 250, 251));

    JLabel titleLabel = new JLabel("View Bookings");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
    titleLabel.setForeground(new Color(30, 30, 30));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel subtitleLabel = new JLabel("Manage and track all rental bookings");
    subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitleLabel.setForeground(new Color(120, 120, 120));
    subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    titleSection.add(titleLabel);
    titleSection.add(Box.createRigidArea(new Dimension(0, 5)));
    titleSection.add(subtitleLabel);

    // Search and Filter Panel
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    searchPanel.setBackground(new Color(249, 250, 251));

    searchField = new JTextField("Search by booking ID, customer name, or car...");
    searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    searchField.setForeground(new Color(150, 150, 150));
    searchField.setPreferredSize(new Dimension(350, 42));
    searchField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    searchField.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (searchField.getText().equals("Search by booking ID, customer name, or car...")) {
                searchField.setText("");
                searchField.setForeground(new Color(30, 30, 30));
            }
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (searchField.getText().isEmpty()) {
                searchField.setText("Search by booking ID, customer name, or car...");
                searchField.setForeground(new Color(150, 150, 150));
            }
        }
    });

    String[] statuses = {"All Status", "Confirmed", "Completed", "Cancelled"};
    statusFilter = new JComboBox<>(statuses);
    statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    statusFilter.setPreferredSize(new Dimension(140, 42));
    statusFilter.setBackground(Color.WHITE);
    statusFilter.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

    searchPanel.add(searchField);
    searchPanel.add(statusFilter);

    headerPanel.add(titleSection, BorderLayout.WEST);
    headerPanel.add(searchPanel, BorderLayout.EAST);

    add(headerPanel, BorderLayout.NORTH);

    // Table Panel
    createTable();
    loadBookings();
}

private void createTable() {
    String[] columnNames = {
        "Booking ID", "Customer Name", "Car Model", 
        "Start Date", "End Date", "Total Price", "Status", "Actions"
    };

    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 7; // Only Actions column is editable
        }
    };

    bookingsTable = new JTable(tableModel);
    bookingsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    bookingsTable.setRowHeight(55);
    bookingsTable.setGridColor(new Color(240, 240, 240));
    bookingsTable.setSelectionBackground(new Color(239, 246, 255));
    bookingsTable.setSelectionForeground(new Color(30, 30, 30));
    bookingsTable.setShowVerticalLines(true);
    bookingsTable.setShowHorizontalLines(true);

    // Header Style
    JTableHeader header = bookingsTable.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setBackground(new Color(249, 250, 251));
    header.setForeground(new Color(60, 60, 60));
    header.setPreferredSize(new Dimension(header.getWidth(), 45));
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

    // Column Widths
    bookingsTable.getColumnModel().getColumn(0).setPreferredWidth(100);  // Booking ID
    bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Customer
    bookingsTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Car Model
    bookingsTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // Start Date
    bookingsTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // End Date
    bookingsTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Price
    bookingsTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // Status
    bookingsTable.getColumnModel().getColumn(7).setPreferredWidth(140);  // Actions

    // Status Column Renderer
    bookingsTable.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

    // Actions Column Renderer
    bookingsTable.getColumnModel().getColumn(7).setCellRenderer(new ActionsCellRenderer());
    bookingsTable.getColumnModel().getColumn(7).setCellEditor(new ActionsCellEditor());

    JScrollPane scrollPane = new JScrollPane(bookingsTable);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
    scrollPane.getViewport().setBackground(Color.WHITE);

    // Pagination Panel
    JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
    paginationPanel.setBackground(Color.WHITE);

    showingLabel = new JLabel("Showing 0 bookings");
    showingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    showingLabel.setForeground(new Color(120, 120, 120));

    JButton prevBtn = createPaginationButton("Previous");
    JButton page1Btn = createPaginationButton("1");
    page1Btn.setBackground(new Color(33, 150, 243));
    page1Btn.setForeground(Color.WHITE);
    JButton page2Btn = createPaginationButton("2");
    JButton nextBtn = createPaginationButton("Next");

    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftPanel.setBackground(Color.WHITE);
    leftPanel.add(showingLabel);

    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    rightPanel.setBackground(Color.WHITE);
    rightPanel.add(prevBtn);
    rightPanel.add(page1Btn);
    rightPanel.add(page2Btn);
    rightPanel.add(nextBtn);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBackground(Color.WHITE);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    bottomPanel.add(leftPanel, BorderLayout.WEST);
    bottomPanel.add(rightPanel, BorderLayout.EAST);

    JPanel tableContainer = new JPanel(new BorderLayout());
    tableContainer.setBackground(Color.WHITE);
    tableContainer.add(scrollPane, BorderLayout.CENTER);
    tableContainer.add(bottomPanel, BorderLayout.SOUTH);

    add(tableContainer, BorderLayout.CENTER);
}

private JButton createPaginationButton(String text) {
    JButton btn = new JButton(text);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    btn.setPreferredSize(new Dimension(text.length() > 2 ? 80 : 35, 35));
    btn.setBackground(Color.WHITE);
    btn.setForeground(new Color(120, 120, 120));
    btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return btn;
}

private void loadBookings() {
    tableModel.setRowCount(0);

    // Query: select booking_status as the original column name (no alias mismatch)
    String sql = "SELECT b.booking_id, u.username, c.model_name, b.start_date, b.end_date, b.total_price, b.booking_status " +
                 "FROM bookings b " +
                 "JOIN users u ON b.user_id = u.user_id " +
                 "JOIN cars c ON b.car_id = c.car_id " +
                 "ORDER BY b.booking_id DESC";

    int rowCount = 0;
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            Object[] row = {
                "BK" + String.format("%03d", rs.getInt("booking_id")),
                rs.getString("username"),
                rs.getString("model_name"),
                rs.getString("start_date"),
                rs.getString("end_date"),
                "$" + String.format("%.2f", rs.getDouble("total_price")),
                rs.getString("booking_status"), // now matches SELECT
                "View Details"
            };
            tableModel.addRow(row);
            rowCount++;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // show a non-blocking message so user can see the error if needed
        JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    showingLabel.setText("Showing " + rowCount + " bookings");
    bookingsTable.revalidate();
    bookingsTable.repaint();
}

// Status Cell Renderer
class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JLabel label = new JLabel(value == null ? "" : value.toString());
        label.setOpaque(true);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        String status = value == null ? "" : value.toString();
        switch (status) {
            case "Confirmed":
                label.setForeground(new Color(33, 150, 243));
                label.setBackground(new Color(225, 239, 254));
                break;
            case "Completed":
                label.setForeground(new Color(34, 197, 94));
                label.setBackground(new Color(220, 252, 231));
                break;
            case "Cancelled":
                label.setForeground(new Color(239, 68, 68));
                label.setBackground(new Color(254, 226, 226));
                break;
            default:
                label.setForeground(new Color(120, 120, 120));
                label.setBackground(new Color(241, 245, 249));
        }

        return label;
    }
}

// Actions Cell Renderer
class ActionsCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        JButton viewBtn = new JButton("👁 View Details");
        viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        viewBtn.setForeground(new Color(33, 150, 243));
        viewBtn.setBackground(Color.WHITE);
        viewBtn.setBorder(null);
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return viewBtn;
    }
}

// Actions Cell Editor
class ActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton viewBtn;

    public ActionsCellEditor() {
        viewBtn = new JButton("👁 View Details");
        viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        viewBtn.setForeground(new Color(33, 150, 243));
        viewBtn.setBackground(Color.WHITE);
        viewBtn.setBorder(null);
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewBtn.addActionListener(e -> {
            int row = bookingsTable.getSelectedRow();
            if (row >= 0) {
                String bookingId = (String) bookingsTable.getValueAt(row, 0);
                JOptionPane.showMessageDialog(ViewBookingsPanel.this, 
                    "Viewing details for " + bookingId, 
                    "Booking Details", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return viewBtn;
    }

    @Override
    public Object getCellEditorValue() {
        return "View Details";
    }
}
}