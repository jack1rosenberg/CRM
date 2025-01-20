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
    private JPanel bottomPanel;
    private JScrollPane scrollPane;
    private JList<String> customerList;
    private ArrayList<Customer> customers;
    private String fileName;

    public gui(String fileName) throws IOException {
        this.fileName = fileName;
        customers = new ArrayList<>();
        loadCustomers();
    
        frame = new JFrame("Junk CRM");
    
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
    
        // Buttons
        JButton btnAdd = new JButton("Confirm Customer");
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
        bottomPanel.add(buttonPanel, BorderLayout.PAGE_END);
    

        // Initialize the customer list
        customerList = new JList<>();
        updateCustomerList();

        // Add the customer list to a scroll pane
        scrollPane = new JScrollPane(customerList);
        JPanel midPanel = new JPanel(new BorderLayout());
        midPanel.add(scrollPane, BorderLayout.CENTER);
        

        // Layout setup
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(panel, BorderLayout.NORTH);
        frame.add(midPanel, BorderLayout.CENTER); // Add midPanel to the frame
        frame.add(bottomPanel, BorderLayout.PAGE_END);
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
    
    private void updateCustomerList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Customer customer : customers) {
            listModel.addElement(customer.getName() + " - " + customer.getPhone());
        }
        customerList.setModel(listModel);
    }

    private void addCustomer() throws IOException {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        if (!name.isEmpty() && !phone.isEmpty()) {
            Customer customer = new Customer(name, email, phone, address);
            customers.add(customer);

            saveCustomersToFile();
            updateCustomerList();
            JOptionPane.showMessageDialog(frame, "Customer added successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill out more information.");
        }
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

    // Get the selected customer from the customer list
    String selectedCustomer = customerList.getSelectedValue();
    if (selectedCustomer == null) {
        JOptionPane.showMessageDialog(frame, "Please select a customer to edit.");
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
}

        

    private void deleteCustomer() {
    // Check if there are customers to delete
    if (customers.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "No customers available to edit.");
        return;
    }

    // Get the selected customer from the customer list
    String selectedCustomer = customerList.getSelectedValue();
    if (selectedCustomer == null) {
        JOptionPane.showMessageDialog(frame, "Please select a customer to edit.");
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

    // Remove the customer from the list
    JOptionPane.showConfirmDialog(frame, "Delete Customer?", "Cancel", JOptionPane.YES_NO_OPTION);
    customers.remove(customerToEdit);
    updateCustomerList();
    saveCustomersToFile();
    JOptionPane.showMessageDialog(frame, "Customer deleted successfully.");
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