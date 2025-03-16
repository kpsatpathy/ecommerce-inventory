import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class MainGUI extends JFrame {
    private JTable productsTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblSubtotal, lblGST, lblTotal;
    private JComboBox<String> searchCriteria;

    public MainGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeUI();
        loadProducts();
    }

    private void initializeUI() {
        setTitle("Inventory Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(0, 150, 136));

        JPanel addPanel = createAddPanel();
        tabbedPane.addTab("Add Products", new ImageIcon(), addPanel, "Add new products");

        JPanel viewManagePanel = createViewManagePanel();
        tabbedPane.addTab("Inventory", new ImageIcon(), viewManagePanel, "View and manage products");

        add(tabbedPane);
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        String[][] categories = {
            {"Electronics", "Laptop", "Smartphone", "Tablet", "Headphones", "Smartwatch", "Camera"},
            {"Fashion", "T-Shirt", "Jeans", "Dress", "Shoes", "Jacket", "Hat"},
            {"Home", "Furniture", "Decor", "Kitchenware", "Lighting", "Storage", "Appliances"},
            {"Beauty", "Skincare", "Makeup", "Haircare", "Fragrance", "Tools", "Bath Products"}
        };

        for (String[] category : categories) {
            JLabel categoryLabel = new JLabel(category[0]);
            categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            categoryLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

            JPanel productsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            productsRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 20, 5));

            for (int i = 1; i < category.length; i++) {
                String productName = category[i];
                double price = generatePrice(category[0], i);
                String imageUrl = "https://picsum.photos/200?random=" + productName.hashCode();
                productsRow.add(createProductCard(productName, price, category[0], imageUrl));
            }
            mainPanel.add(categoryLabel);
            mainPanel.add(productsRow);
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private double generatePrice(String category, int index) {
        return switch (category) {
            case "Electronics" -> 299.99 + (index * 150);
            case "Fashion" -> 49.99 + (index * 20);
            case "Home" -> 89.99 + (index * 30);
            case "Beauty" -> 19.99 + (index * 10);
            default -> 99.99;
        };
    }

    private JPanel createProductCard(String name, double price, String category, String imageUrl) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(220, 420));
        card.setBackground(Color.WHITE);

        JLabel imageLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon(new URL(imageUrl));
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (MalformedURLException e) {
            imageLabel.setText("Image not available");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        imageLabel.setPreferredSize(new Dimension(200, 200));
        card.add(imageLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        detailsPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("$%.2f", price), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(0, 150, 136));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel categoryLabel = new JLabel(category, SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        categoryLabel.setForeground(Color.GRAY);
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        quantityPanel.setBackground(Color.WHITE);
        quantityPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton btnMinus = new JButton("-");
        btnMinus.setPreferredSize(new Dimension(40, 30));
        JLabel lblQuantity = new JLabel("1");
        lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnPlus = new JButton("+");
        btnPlus.setPreferredSize(new Dimension(40, 30));

        btnMinus.addActionListener(e -> {
            int qty = Integer.parseInt(lblQuantity.getText());
            if(qty > 1) lblQuantity.setText(String.valueOf(qty - 1));
        });

        btnPlus.addActionListener(e -> {
            int qty = Integer.parseInt(lblQuantity.getText());
            lblQuantity.setText(String.valueOf(qty + 1));
        });

        quantityPanel.add(btnMinus);
        quantityPanel.add(lblQuantity);
        quantityPanel.add(btnPlus);

        JButton addButton = new JButton("Add to Inventory");
        addButton.setBackground(new Color(0, 150, 136));
        addButton.setForeground(Color.WHITE);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> {
            int quantity = Integer.parseInt(lblQuantity.getText());
            addProduct(name, price, quantity, category);
        });

        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(priceLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(categoryLabel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(quantityPanel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(addButton);
        detailsPanel.add(Box.createVerticalGlue());

        card.add(detailsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createViewManagePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchCriteria = new JComboBox<>(new String[]{"Name", "Category", "ID"});
        txtSearch = new JTextField(25);
        JButton btnSearch = new JButton("Search");
        JButton btnExport = new JButton("Export to CSV");
        JButton btnDelete = new JButton("Delete Selected");
        
        btnSearch.setBackground(new Color(0, 150, 136));
        btnSearch.setForeground(Color.WHITE);
        btnExport.setBackground(new Color(76, 175, 80));
        btnExport.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(255, 87, 34));
        btnDelete.setForeground(Color.WHITE);

        searchPanel.add(createLabel("Search By:"));
        searchPanel.add(searchCriteria);
        searchPanel.add(createLabel("Keyword:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnExport);
        searchPanel.add(btnDelete);

        tableModel = new DefaultTableModel();
        productsTable = new JTable(tableModel);
        productsTable.setRowHeight(30);
        productsTable.setAutoCreateRowSorter(true);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] columns = {"No.", "Product Name", "Price ($)", "Quantity", "Category", "ID"};
        for (String col : columns) tableModel.addColumn(col);

        JScrollPane tableScroll = new JScrollPane(productsTable);
        tableScroll.setPreferredSize(new Dimension(800, 400));
        productsTable.setDefaultRenderer(Object.class, new AlternatingRowColorRenderer());
        productsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel totalPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        lblSubtotal = createTotalLabel("0.00");
        lblGST = createTotalLabel("0.00");
        lblTotal = createTotalLabel("0.00");

        totalPanel.add(createLabel("Subtotal:"));
        totalPanel.add(lblSubtotal);
        totalPanel.add(createLabel("GST (18%):"));
        totalPanel.add(lblGST);
        totalPanel.add(createLabel("Total:"));
        totalPanel.add(lblTotal);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalPanel, BorderLayout.CENTER);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> searchProducts());
        btnExport.addActionListener(e -> exportToCSV());
        productsTable.getModel().addTableModelListener(e -> calculateTotals());
        
        btnDelete.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow == -1) {
                showMessage("No row selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int modelRow = productsTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 5); // Get hidden database ID
            
            try (Connection conn = DBConnector.getConnection()) {
                String sql = "DELETE FROM products WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                loadProducts(); // Refresh with new sequence
                showMessage("Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                showMessage("Error deleting product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JLabel createTotalLabel(String text) {
        JLabel label = new JLabel("$" + text, SwingConstants.RIGHT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(0, 150, 136));
        return label;
    }

    private void calculateTotals() {
        double subtotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double price = (Double) tableModel.getValueAt(i, 2);
            int quantity = (Integer) tableModel.getValueAt(i, 3);
            subtotal += price * quantity;
        }
        double gst = subtotal * 0.18;
        double total = subtotal + gst;

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblGST.setText(String.format("$%.2f", gst));
        lblTotal.setText(String.format("$%.2f", total));
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV");
        fileChooser.setSelectedFile(new File("inventory.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                // Write headers (skip hidden ID column)
                for (int i = 0; i < tableModel.getColumnCount() - 1; i++) {
                    writer.write(tableModel.getColumnName(i) + (i < tableModel.getColumnCount() - 2 ? "," : "\n"));
                }
                
                // Write data (skip hidden ID column)
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount() - 1; col++) {
                        Object value = tableModel.getValueAt(row, col);
                        writer.write(value.toString() + (col < tableModel.getColumnCount() - 2 ? "," : "\n"));
                    }
                }
                
                JOptionPane.showMessageDialog(this, "Data exported successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addProduct(String name, double price, int quantity, String category) {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "INSERT INTO products (name, price, quantity, category) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, category);
            pstmt.executeUpdate();
            loadProducts();
            showMessage("Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            showMessage("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnector.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY id");
            int displayId = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    displayId++,          // Display sequence number
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getInt("id")       // Hidden database ID
                });
            }
            hideDatabaseIdColumn(); // Hide the actual database ID column
        } catch (SQLException ex) {
            showMessage("Error loading products: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hideDatabaseIdColumn() {
        int idColumnIndex = 5; // Index of the ID column
        if (productsTable.getColumnModel().getColumnCount() > idColumnIndex) {
            TableColumn column = productsTable.getColumnModel().getColumn(idColumnIndex);
            productsTable.removeColumn(column);
        }
    }

    private void searchProducts() {
        String searchTerm = txtSearch.getText().trim();
        String criteria = (String) searchCriteria.getSelectedItem();
        tableModel.setRowCount(0);

        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT * FROM products WHERE ";
            PreparedStatement pstmt;

            switch (criteria) {
                case "ID":
                    sql += "id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Name":
                    sql += "name LIKE ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "%" + searchTerm + "%");
                    break;
                case "Category":
                    sql += "category LIKE ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "%" + searchTerm + "%");
                    break;
                default:
                    sql += "name LIKE ? OR category LIKE ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "%" + searchTerm + "%");
                    pstmt.setString(2, "%" + searchTerm + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            int displayId = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    displayId++,          // Display sequence number
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getInt("id")       // Hidden database ID
                });
            }
            hideDatabaseIdColumn(); // Hide the actual database ID column
        } catch (SQLException | NumberFormatException ex) {
            showMessage("Search Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    static class AlternatingRowColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
            }
            return c;
        }
    }

    public static void main(String[] args) {
        new MainGUI().setVisible(true);
    }
}