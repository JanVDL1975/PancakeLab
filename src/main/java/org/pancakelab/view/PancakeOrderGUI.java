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
    private JButton createOrderButton, addPancakeButton, removePancakeButton, requestMenuButton, addRecipeButton, addNewPancakeButton;

    private JTextField recipeNameField, recipeIngredientsField;
    private JTextField newPancakeNameField;

    private JTabbedPane tabbedPane;  // Tabbed Pane for switching between tabs

    public PancakeOrderGUI(PancakeService pancakeService) {
        workflow = new PancakeOrderWorkflow(pancakeService);

        setTitle("Pancake Order System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize input fields
        buildingField = new JTextField(10);
        roomField = new JTextField(5);
        createOrderButton = new JButton("Create Order");

        // Tabbed Pane: Create Order Tab, Pancake Selection Tab, Maintenance Tab
        tabbedPane = new JTabbedPane();

        // Order Creation Panel
        JPanel orderPanel = createOrderPanel();
        tabbedPane.addTab("Create Order", orderPanel);

        // Pancake Selection Panel
        JPanel pancakePanel = createPancakeSelectionPanel();
        tabbedPane.addTab("Pancake Selection", pancakePanel);

        // Maintenance Panel
        JPanel maintenancePanel = createMaintenancePanel();
        tabbedPane.addTab("Maintenance", maintenancePanel);

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
        add(tabbedPane, BorderLayout.CENTER);  // Add tabbed pane to center
        add(detailsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new GridBagLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Create Order"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add the order input fields
        gbc.gridx = 0; gbc.gridy = 0;
        orderPanel.add(new JLabel("Building:"), gbc);
        gbc.gridx = 1;
        orderPanel.add(buildingField, gbc);
        gbc.gridx = 2;
        orderPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 3;
        orderPanel.add(roomField, gbc);
        gbc.gridx = 4;
        orderPanel.add(createOrderButton, gbc);

        // Event listeners
        createOrderButton.addActionListener(new CreateOrderAction());
        return orderPanel;
    }

    private JPanel createPancakeSelectionPanel() {
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

        // Event listeners
        addPancakeButton.addActionListener(new AddPancakeAction());
        removePancakeButton.addActionListener(new RemovePancakeAction());
        return pancakePanel;
    }

    private JPanel createMaintenancePanel() {
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridLayout(3, 1));

        // Recipe Section
        JPanel recipePanel = new JPanel(new FlowLayout());
        recipePanel.setBorder(BorderFactory.createTitledBorder("Add New Recipe"));
        recipeNameField = new JTextField(10);
        recipeIngredientsField = new JTextField(15);
        addRecipeButton = new JButton("Add Recipe");
        recipePanel.add(new JLabel("Recipe Name:"));
        recipePanel.add(recipeNameField);
        recipePanel.add(new JLabel("Ingredients:"));
        recipePanel.add(recipeIngredientsField);
        recipePanel.add(addRecipeButton);

        // New Pancake Section
        JPanel pancakeCreationPanel = new JPanel(new FlowLayout());
        pancakeCreationPanel.setBorder(BorderFactory.createTitledBorder("Build New Pancake"));
        newPancakeNameField = new JTextField(10);
        addNewPancakeButton = new JButton("Add New Pancake");
        pancakeCreationPanel.add(new JLabel("Pancake Name:"));
        pancakeCreationPanel.add(newPancakeNameField);
        pancakeCreationPanel.add(addNewPancakeButton);

        maintenancePanel.add(recipePanel);
        maintenancePanel.add(pancakeCreationPanel);

        // Event listeners
        addRecipeButton.addActionListener(new AddRecipeAction());
        addNewPancakeButton.addActionListener(new AddNewPancakeAction());

        return maintenancePanel;
    }

    private class CreateOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int building = Integer.parseInt(buildingField.getText());
                int room = Integer.parseInt(roomField.getText());
                workflow.createOrder(building, room);
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Order Created");

                // After creating the order, show the pancake selection tab
                tabbedPane.setSelectedIndex(1);  // Switch to "Pancake Selection" tab
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

    private class AddRecipeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String recipeName = recipeNameField.getText();
            String ingredients = recipeIngredientsField.getText();
            if (recipeName != null && !recipeName.isEmpty() && ingredients != null && !ingredients.isEmpty()) {
                // Call workflow method to add the recipe (you should implement this method)
                workflow.addRecipe(recipeName, ingredients);
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Recipe added.");
            }
        }
    }

    private class AddNewPancakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String pancakeName = newPancakeNameField.getText();
            if (pancakeName != null && !pancakeName.isEmpty()) {
                // Call workflow method to build a new pancake (you should implement this method)
                workflow.buildNewPancake(pancakeName);
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "New Pancake built.");
            }
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
