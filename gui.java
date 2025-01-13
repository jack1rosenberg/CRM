import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;

public class gui {
    private JFrame frame;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextArea txtDisplay;
    private JPanel bottomPanel;
    private ArrayList<Customer> customers;
    private String fileName;

    public gui(String fileName) throws IOException {
        this.fileName = fileName;
        customers = new ArrayList<>();
        loadCustomers();
    
        frame = new JFrame("CRM System");
    
        // Labels
        JLabel lblName = new JLabel("Name:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblPhone = new JLabel("Phone:");
        JLabel lblAddress = new JLabel("Address:");
    
        // Text fields
        txtName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextField(20);
    
        /* 
        txtDisplay = new JTextArea(10, 30);
        txtDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtDisplay);
        */
    
        // Buttons
        JButton btnAdd = new JButton("Add Customer");
        JButton btnEdit = new JButton("Edit Customer");
        JButton btnDelete = new JButton("Delete Customer");
        JButton btnClear = new JButton("Clear Fields");
    
        // Panel for Form Inputs
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPhone);
        panel.add(txtPhone);
        panel.add(lblAddress);
        panel.add(txtAddress);
        panel.add(btnAdd);
    
        // Panel for Buttons and Display Area
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
    
        // Layout setup
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(panel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    
        // Action Listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addCustomer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });
    
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });
    
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    
        // Call viewCustomers to display the customer list at startup
        viewCustomers();
    
        // Final setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    

    private void loadCustomers() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                customers.add(new Customer(parts[0], parts[1], parts[2], parts[3]));
            }
        }
        reader.close();
    }

    private void addCustomer() throws IOException {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        if (!name.isEmpty() && !phone.isEmpty()) {
            Customer customer = new Customer(name, email, phone, address);
            customers.add(customer);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(name + "," + email + "," + phone + "," + address);
            writer.newLine();
            writer.close();

            JOptionPane.showMessageDialog(frame, "Customer added successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill out more information.");
        }
    }

    private void viewCustomers() {
        // Clear the bottom panel to prepare for repopulation
        bottomPanel.removeAll();
    
        // Create a DefaultListModel to hold customer data
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Customer customer : customers) {
            listModel.addElement(customer.getName() + " - " + customer.getPhone());
        }
    
        // Create a JList to display the customer data
        JList<String> customerList = new JList<>(listModel);
        customerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(customerList);
    
        // Re-add the buttons at the top of the bottom panel
        JPanel buttonPanel = new JPanel();
        JButton btnEdit = new JButton("Edit Customer");
        JButton btnDelete = new JButton("Delete Customer");
        JButton btnClear = new JButton("Clear Fields");
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
    
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Refresh the bottom panel
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }
    

    private void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Customer customer : customers) {
                writer.write(customer.getName() + "," + customer.getEmail() + "," +
                        customer.getPhone() + "," + customer.getAddress());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving customers to file: " + e.getMessage());
        }
    }
    
    
    private void editCustomer() {
            // Check if there are customers to edit
            if (customers.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No customers available to edit.");
                return;
            }
        
            // Prompt the user to select a customer
            String[] customerNames = customers.stream()
                    .map(customer -> customer.getName() + " - " + customer.getPhone())
                    .toArray(String[]::new);
        
            String selectedCustomer = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select a customer to edit:",
                    "Edit Customer",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    customerNames,
                    customerNames[0]
            );
        
            // If no customer is selected, exit
            if (selectedCustomer == null) {
                return;
            }
        
            // Find the selected customer
            Customer customerToEdit = customers.stream()
                    .filter(customer -> (customer.getName() + " - " + customer.getPhone()).equals(selectedCustomer))
                    .findFirst()
                    .orElse(null);
        
            if (customerToEdit == null) {
                JOptionPane.showMessageDialog(frame, "Customer not found.");
                return;
            }
        
            // Populate the text fields with the customer's current data
            txtName.setText(customerToEdit.getName());
            txtEmail.setText(customerToEdit.getEmail());
            txtPhone.setText(customerToEdit.getPhone());
            txtAddress.setText(customerToEdit.getAddress());
        
            // Prompt the user to confirm and save changes
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Edit the details and press OK to save changes.",
                    "Confirm Edit",
                    JOptionPane.OK_CANCEL_OPTION
            );
        
            if (result == JOptionPane.OK_OPTION) {
                // Update customer details
                customerToEdit.setName(txtName.getText());
                customerToEdit.setEmail(txtEmail.getText());
                customerToEdit.setPhone(txtPhone.getText());
                customerToEdit.setAddress(txtAddress.getText());
        
                // Refresh the customer list in the GUI
                viewCustomers();
        
                // Save changes to the file
                saveCustomersToFile();
        
                JOptionPane.showMessageDialog(frame, "Customer details updated successfully!");
            }
        
            // Clear the text fields after editing
            clearFields();
        }
        

    private void deleteCustomer() {

    }

    private void clearFields() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new gui("db.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}