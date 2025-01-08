import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class gui {
    private JFrame frame;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextArea txtDisplay;
    private ArrayList<Customer> customers;
    private String fileName;

    public gui(String fileName) throws IOException{
        customers = new ArrayList<>();
        frame = new JFrame("CRM System");
        fileName = this.fileName;

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
                try {
                    addCustomer(fileName);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addCustomer(String fileName) throws IOException
    {
        FileWriter file = new FileWriter(fileName);
        BufferedWriter myFile = new BufferedWriter(file);
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        if (!name.isEmpty() && !phone.isEmpty()){
            Customer customer = new Customer(name, email, phone, address);
            customers.add(customer);
            JOptionPane.showMessageDialog(frame, "Customer added successfully!");
            myFile.write(customer.getName() + "," + customer.getEmail() +","+ customer.getPhone()+","+customer.getAddress());
            clearFields();
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill out more information.");
        }
        myFile.close();
    }

    private void editCustomer() {

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
                try {
                    new gui("db.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
