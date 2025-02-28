package org.pancakelab.view;

import org.pancakelab.PancakeOrderWorkflow;
import org.pancakelab.service.PancakeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

public class PancakeOrderGUI extends JFrame {
    private PancakeOrderWorkflow workflow;
    private JTextArea orderDetailsArea, orderHistoryArea;
    private JComboBox<String> pancakeComboBox;
    private JTextField buildingField, roomField;
    private JButton createOrderButton, addPancakeButton, viewOrderButton, removePancakeButton, requestMenuButton;

    public PancakeOrderGUI(PancakeService pancakeService) {
        workflow = new PancakeOrderWorkflow(pancakeService);

        setTitle("Pancake Order System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize input fields BEFORE adding them
        buildingField = new JTextField(10);
        roomField = new JTextField(5);
        createOrderButton = new JButton("Create Order");

        // Top Panel: Order Creation (Now Includes Menu)
        JPanel orderPanel = new JPanel(new GridBagLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Create Order"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

// Adding the menu panel before the order form
        JPanel viewMenuPanel = new JPanel(new BorderLayout());
        viewMenuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        viewMenuPanel.setPreferredSize(new Dimension(400, 100));  // Adjust size to fit in orderPanel

        JTextArea menuArea = new JTextArea(5, 30);
        menuArea.setEditable(false);
        JScrollPane menuScrollPane = new JScrollPane(menuArea);
        viewMenuPanel.add(menuScrollPane, BorderLayout.CENTER);

        requestMenuButton = new JButton("Add Pancake To Menu");
        viewMenuPanel.add(requestMenuButton, BorderLayout.SOUTH);

// Adding menu panel to `orderPanel` (Before other fields)
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 5; // Span across 5 columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        orderPanel.add(viewMenuPanel, gbc);

// Now add the existing fields below the menu panel
        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; // Reset fill property
        orderPanel.add(new JLabel("Building:"), gbc);
        gbc.gridx = 1;
        orderPanel.add(buildingField, gbc);
        gbc.gridx = 2;
        orderPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 3;
        orderPanel.add(roomField, gbc);
        gbc.gridx = 4;
        orderPanel.add(createOrderButton, gbc);

        // Middle Panel: Pancake Selection
        JPanel pancakePanel = new JPanel();
        pancakePanel.setBorder(BorderFactory.createTitledBorder("Add Pancakes"));
        pancakePanel.setPreferredSize(new Dimension(400, 70)); // Adjust height as needed
        pancakePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pancakeComboBox = new JComboBox<>();
        addPancakeButton = new JButton("Add Pancake");
        removePancakeButton = new JButton("Remove Pancake");
        pancakePanel.add(pancakeComboBox);
        pancakePanel.add(addPancakeButton);
        pancakePanel.add(removePancakeButton);

        // Bottom Panel: Order Details and History
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2));
        JPanel orderDetailsPanel = new JPanel(new BorderLayout());
        orderDetailsPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderDetailsArea = new JTextArea(10, 30);
        JScrollPane orderDetailsPane = new JScrollPane(orderDetailsArea);

        JPanel orderHistoryPanel = new JPanel(new BorderLayout());
        orderHistoryPanel.setBorder(BorderFactory.createTitledBorder("Order History"));
        orderHistoryArea = new JTextArea(10, 30);
        orderHistoryArea.setEditable(false);
        orderHistoryPanel.add(new JScrollPane(orderHistoryArea), BorderLayout.CENTER);

        detailsPanel.add(orderDetailsPanel);
        detailsPanel.add(orderHistoryPanel);

        // Adding Components to Frame
        add(orderPanel, BorderLayout.NORTH);
        add(pancakePanel, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);

        // Event listeners
        createOrderButton.addActionListener(new CreateOrderAction());
        addPancakeButton.addActionListener(new AddPancakeAction());
        //viewOrderButton.addActionListener(new ViewOrderAction());
        removePancakeButton.addActionListener(new RemovePancakeAction());

        setVisible(true);
    }

    private class CreateOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int building = Integer.parseInt(buildingField.getText());
                int room = Integer.parseInt(roomField.getText());
                workflow.createOrder(building, room);
                updatePancakeMenu();
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Order Created");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Invalid input.");
            }
        }
    }

    private class AddPancakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String pancakeName = (String) pancakeComboBox.getSelectedItem();
            if (pancakeName != null) {
                workflow.addPancakeToOrder(pancakeName, 1);
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Pancake added.");
            }
        }
    }

    private class ViewOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> orderDetails = workflow.viewOrder();
            orderDetailsArea.setText(String.join("\n", orderDetails));
        }
    }

    private class RemovePancakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String pancakeDescription = JOptionPane.showInputDialog("Enter Pancake Description to Remove:");
            if (pancakeDescription != null && !pancakeDescription.trim().isEmpty()) {
                workflow.removePancakeFromOrder(pancakeDescription, 1);
                updateOrderHistory();
            }
        }
    }

    private void updatePancakeMenu() {
        pancakeComboBox.removeAllItems();
        Set<String> availablePancakes = workflow.listAvailablePancakes();
        for (String pancake : availablePancakes) {
            pancakeComboBox.addItem(pancake);
        }
    }

    private void updateOrderHistory() {
        List<String> orderDetails = workflow.viewOrder();
        orderHistoryArea.setText(String.join("\n", orderDetails));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PancakeService pancakeService = new PancakeService();
            new PancakeOrderGUI(pancakeService);
        });
    }
}

