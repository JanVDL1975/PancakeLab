package org.pancakelab.view;

import org.pancakelab.PancakeOrderWorkflow;
import org.pancakelab.service.PancakeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

        JPanel maintenanceSplitPanel = new JPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

// Left side: Ingredients panel
        JPanel ingredientsPanel = new JPanel(new FlowLayout());
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("Ingredients Section"));
        ingredientsPanel.add(createIngredientsPanel());

// Right side: Original Maintenance panel
        JPanel originalMaintenancePanel = createMaintenancePanel();
        originalMaintenancePanel.setBorder(BorderFactory.createTitledBorder("Original Maintenance"));

        splitPane.setLeftComponent(ingredientsPanel);
        splitPane.setRightComponent(originalMaintenancePanel);
        splitPane.setDividerLocation(0.5);  // Split the panels equally

// Add to the tabbed pane
        tabbedPane.addTab("Maintenance", splitPane);

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

    // Method to add ingredients to the recipe (this should be implemented as per your logic)
    private void addIngredientsToRecipe(String listName, DefaultListModel<String> ingredientListModel) {
        // Here we transfer the ingredient list to the recipe (this part can be updated based on your application's logic)
        System.out.println("Adding ingredients to recipe: " + listName);
        for (int i = 0; i < ingredientListModel.size(); i++) {
            String ingredient = ingredientListModel.getElementAt(i);
            // Add ingredient to the recipe (You can implement your logic to add this to the recipe here)
            System.out.println("Ingredient: " + ingredient);
        }
        // Optionally, clear the list after adding to recipe
        ingredientListModel.clear();
        JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Ingredients added to recipe: " + listName);
    }

    // Ingredients Section
    JPanel createIngredientsPanel() {
        // Ingredients Panel
        JPanel newIngredientPanel = new JPanel();
        newIngredientPanel.setPreferredSize(new Dimension(550, 100));
        newIngredientPanel.setBorder(BorderFactory.createTitledBorder("Add New Item"));

        // Name field
        newIngredientPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        nameField.setMinimumSize(new Dimension(150, 20));
        nameField.setPreferredSize(new Dimension(150, 20));
        newIngredientPanel.add(nameField);

        // Quantity field
        newIngredientPanel.add(new JLabel("Quantity:"));
        JTextField quantityField = new JTextField();
        quantityField.setMinimumSize(new Dimension(25, 20));
        quantityField.setPreferredSize(new Dimension(25, 20));
        newIngredientPanel.add(quantityField);

        // Unit field
        newIngredientPanel.add(new JLabel("Units:"));
        JTextField unitField = new JTextField();
        unitField.setMinimumSize(new Dimension(25, 20));
        unitField.setPreferredSize(new Dimension(25, 20));
        newIngredientPanel.add(unitField);

        // Button to create ingredient
        JButton createIngredientButton = new JButton("Add Item");
        newIngredientPanel.add(createIngredientButton);

        // Ingredient list
        JPanel ingredientListPanel = new JPanel(new BorderLayout());
        ingredientListPanel.setPreferredSize(new Dimension(550, 150));
        ingredientListPanel.setBorder(BorderFactory.createTitledBorder("Ingredients List"));

        DefaultListModel<String> ingredientListModel = new DefaultListModel<>();
        JList<String> ingredientList = new JList<>(ingredientListModel);
        ingredientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ingredientListPanel.add(new JScrollPane(ingredientList), BorderLayout.CENTER);

        // Field for naming the ingredient list
        JPanel listNamePanel = new JPanel();
        listNamePanel.add(new JLabel("List Name:"));
        JTextField listNameField = new JTextField(15);
        listNamePanel.add(listNameField);

        // Button to add the ingredient list to a recipe
        JButton addListToRecipeButton = new JButton("Add List to Recipe");

        // Action listener for adding the list to recipe
        addListToRecipeButton.addActionListener(e -> {
            String listName = listNameField.getText().trim();
            if (listName.isEmpty()) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please provide a name for the ingredient list.");
                return;
            }

            // Transfer ingredients to recipe (You can implement recipe logic here)
            addIngredientsToRecipe(listName, ingredientListModel);
        });



        // Button to add new ingredient item to the list
        createIngredientButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String quantity = quantityField.getText().trim();
            String unit = unitField.getText().trim();

            if (!name.isEmpty() && !quantity.isEmpty() && !unit.isEmpty()) {
                String ingredient = name + " - " + quantity + " " + unit;
                ingredientListModel.addElement(ingredient);  // Add to the list model
            } else {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "All fields must be filled out.");
            }
        });

        // Panel to contain both the ingredient entry and the list
        JPanel ingredientContainerPanel = new JPanel();
        ingredientContainerPanel.setLayout(new BoxLayout(ingredientContainerPanel, BoxLayout.Y_AXIS));
        ingredientContainerPanel.add(newIngredientPanel);
        ingredientContainerPanel.add(ingredientListPanel);
        ingredientContainerPanel.add(listNamePanel);
        ingredientContainerPanel.add(addListToRecipeButton);

        return ingredientContainerPanel;
    }

    // Panel for Adding New Recipes
    JPanel createRecipePanel() {
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        recipePanel.setBorder(BorderFactory.createTitledBorder("Add New Recipe"));

        // Recipe Name
        JPanel recipeNamePanel = new JPanel();
        recipeNamePanel.add(new JLabel("Recipe Name:"));
        JTextField recipeNameField = new JTextField(20);
        recipeNamePanel.add(recipeNameField);

        // Ingredients Display Area (Replacing TextField)
        JPanel recipeIngredientsPanel = new JPanel();
        recipeIngredientsPanel.setLayout(new BorderLayout());
        recipeIngredientsPanel.setBorder(BorderFactory.createTitledBorder("Selected Ingredients"));

        JTextArea recipeIngredientsDisplay = new JTextArea(5, 30);
        recipeIngredientsDisplay.setEditable(false);
        recipeIngredientsDisplay.setLineWrap(true);
        recipeIngredientsDisplay.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(recipeIngredientsDisplay);
        recipeIngredientsPanel.add(scrollPane, BorderLayout.CENTER);

        // Button to Add Recipe (Without Manual Ingredients Entry)
        JButton addRecipeButton = new JButton("Add Recipe");
        addRecipeButton.addActionListener(e -> {
            String recipeName = recipeNameField.getText().trim();
            String ingredientsText = recipeIngredientsDisplay.getText().trim();

            if (recipeName.isEmpty()) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please enter a recipe name.");
                return;
            }
            if (ingredientsText.isEmpty()) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please add ingredients before saving the recipe.");
                return;
            }

            // Save the recipe (You may implement logic to store recipes in your database or list)
            System.out.println("Recipe Created: " + recipeName);
            System.out.println("Ingredients: \n" + ingredientsText);

            JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Recipe Added: " + recipeName);

            // Optionally, clear fields after adding the recipe
            recipeNameField.setText("");
            recipeIngredientsDisplay.setText("");
        });

        recipePanel.add(recipeNamePanel);
        recipePanel.add(recipeIngredientsPanel);
        recipePanel.add(addRecipeButton);

        return recipePanel;
    }

    // Modify the addIngredientsToRecipe method to update the display area
    private void addIngredientsToRecipe(String listName, DefaultListModel<String> ingredientListModel, JTextArea recipeIngredientsDisplay) {
        StringBuilder ingredientText = new StringBuilder("[" + listName + "]\n");

        for (int i = 0; i < ingredientListModel.size(); i++) {
            ingredientText.append("- ").append(ingredientListModel.getElementAt(i)).append("\n");
        }

        recipeIngredientsDisplay.setText(ingredientText.toString());
        JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Ingredients added to recipe: " + listName);
    }

    private JPanel createMaintenancePanel() {
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridLayout(3, 1));

        // Recipe Section
        JPanel recipePanel = createRecipePanel();
        recipePanel.setBorder(BorderFactory.createTitledBorder("Add New Recipe"));
        recipePanel.setLayout(new FlowLayout());

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
        //addRecipeButton.addActionListener(new AddRecipeAction());
        //addNewPancakeButton.addActionListener(new AddNewPancakeAction());

        return maintenancePanel;
    }

    private class CreateOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String building = buildingField.getText();
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
