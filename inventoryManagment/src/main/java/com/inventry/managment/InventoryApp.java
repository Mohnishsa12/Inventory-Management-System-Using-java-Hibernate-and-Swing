
package com.inventry.managment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.inventry.managment.HibernateCFGcode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class InventoryApp extends JFrame {
    private JTextField pnameField, priceField, quantityField;
    private DefaultTableModel tableModel;
    private JTable table;

    public InventoryApp() {
        // Show login screen first
        if (!login()) {
            System.exit(0); // Exit if login fails
        }

        setTitle("Inventory Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Product Name:"), gbc);

        pnameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(pnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Price:"), gbc);

        priceField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Quantity:"), gbc);

        quantityField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addButton = new JButton("Add Product");
        buttonPanel.add(addButton);

        JButton showButton = new JButton("Show Products");
        buttonPanel.add(showButton);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[] { "ID", "Product Name", "Price", "Quantity" }, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Action Buttons Panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");

        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        add(actionPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProductTable();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        setVisible(true);
    }

    // Simple login method
    private boolean login() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        int option = JOptionPane.showConfirmDialog(null, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                HibernateCFGcode cfg = new HibernateCFGcode();
                Session session = cfg.getSession();
                Transaction tx = session.beginTransaction();

                String hql = "FROM login WHERE username = :uname AND password = :pass";
                List result = session.createQuery(hql).setParameter("uname", username).setParameter("pass", password)
                        .list();

                tx.commit();
                session.close();

                return !result.isEmpty(); // Return true if user is found, otherwise false

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error during login: " + e.getMessage());
            }
        }
        return false; // Return false if cancel or wrong credentials
    }

    // Adding product
    private void addProduct() {
        product p = new product(pnameField.getText(), Double.parseDouble(priceField.getText()),
                Integer.parseInt(quantityField.getText()));
        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            Session s = cfg.getSession();
            Transaction tx = s.beginTransaction();
            s.save(p);
            tx.commit();
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage());
        }
    }

    // Loading products in the table
    private void loadProductTable() {
        try {
            HibernateCFGcode cfg = new HibernateCFGcode();
            Session s = cfg.getSession();
            Transaction tx = s.beginTransaction();
            List<product> productList = s.createQuery("from product", product.class).list();
            tableModel.setRowCount(0);
            for (product p : productList) {
                Vector<Object> row = new Vector<>();
                row.add(p.getPid());
                row.add(p.getPname());
                row.add(p.getPrice());
                row.add(p.getPquantity());
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    // Update product
    private void updateProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int pid = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            try {
                HibernateCFGcode cfg = new HibernateCFGcode();
                Session s = cfg.getSession();
                Transaction tx = s.beginTransaction();
                product p = s.get(product.class, pid);
                if (p != null) {
                    p.setPname(pnameField.getText());
                    p.setPrice(Double.parseDouble(priceField.getText()));
                    p.setPquantity(Integer.parseInt(quantityField.getText()));
                    s.update(p);
                    tx.commit();
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
        }
    }

    // Delete product
    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int pid = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            try {
                HibernateCFGcode cfg = new HibernateCFGcode();
                Session s = cfg.getSession();
                Transaction tx = s.beginTransaction();
                product p = s.get(product.class, pid);
                if (p != null) {
                    s.delete(p);
                    tx.commit();
                    JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                    loadProductTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
        }
    }

    private void clearForm() {
        pnameField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryApp());
    }
}
