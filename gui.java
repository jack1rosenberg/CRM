import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class gui {
    private JFrame frame;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextArea txtDisplay;
    private ArrayList<Customer> customers;

    public gui() {
        customers = new ArrayList<>();
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

        // Text Area to display customers
        txtDisplay = new JTextArea(10, 30);
        txtDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtDisplay);

        // Buttons
        JButton btnAdd = new JButton("Add Customer");
        JButton btnView = new JButton("View Customers");
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
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnView);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnClear);

        // Layout setup
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCustomers();
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
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addCustomer() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            Customer customer = new Customer(name, email, phone, address);
            customers.add(customer);
            JOptionPane.showMessageDialog(frame, "Customer added successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out!");
        }
    }

    private void viewCustomers() {
        txtDisplay.setText("");
        for (Customer customer : customers) {
            txtDisplay.append(customer.toString() + "\n\n");
        }
    }

    private void deleteCustomer() {
        String nameToDelete = JOptionPane.showInputDialog(frame, "Enter the name of the customer to delete:");

        boolean found = false;
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getName().equalsIgnoreCase(nameToDelete)) {
                customers.remove(i);
                JOptionPane.showMessageDialog(frame, "Customer deleted successfully!");
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(frame, "Customer not found!");
        }
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
                new gui();
            }
        });
    }
}
