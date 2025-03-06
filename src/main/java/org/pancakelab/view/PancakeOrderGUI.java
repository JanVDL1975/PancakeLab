package org.pancakelab.view;

import org.pancakelab.PancakeOrderWorkflow;
import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.ingredients.IngredientsListContainer;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.pancakes.PancakeListModel;
import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.model.recipes.RecipeIngredientsListModel;
import org.pancakelab.model.recipes.RecipeListModel;
import org.pancakelab.service.PancakeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PancakeOrderGUI extends JFrame {
    private PancakeOrderWorkflow workflow;
    private JTextArea orderDetailsArea, orderHistoryArea;
    private JComboBox<String> pancakeComboBox = new JComboBox<>();
    private JTextField buildingField, roomField;
    private JButton createOrderButton, addPancakeButton, removePancakeButton, requestMenuButton, addRecipeButton, addNewPancakeButton;

    private JTextField recipeNameField = new JTextField(15);
    private JTextField newPancakeNameField;
    private JTextField listNameField;
    private JTabbedPane tabbedPane;  // Tabbed Pane for switching between tabs
    private JPanel listNamePanel;
    private JPanel ingredientContainerPanel = new JPanel();
    IngredientsList recipeIngredientModel = new IngredientsList("Custom List");
    RecipeIngredientsListModel recipeIngredientsListModel = new RecipeIngredientsListModel();
    RecipeListModel recipeListModel = new RecipeListModel();
    PancakeListModel pancakeListModel = new PancakeListModel();
    IngredientsListContainer ingredientsListContainer = new IngredientsListContainer();

    /* This is Add New Recipe Panel */
    private IngredientListSelectionPanel<IngredientsList> recipeIngredientListSelectionPanel =
            new IngredientListSelectionPanel<IngredientsList>(
                    ingredientsListContainer.getAllIngredientsLists(),
                    "Available Ingredients Lists",
                    "Selected Ingredients List",
                    false);

    private IngredientSelectionPanel<Ingredient> recipeIngredientsSelectionPanel =
            new IngredientSelectionPanel<Ingredient>(
                    recipeIngredientModel.getList(),
                    recipeIngredientListSelectionPanel);




    private RecipeSelectionPanel<Recipe> recipeSelectionPanel = new RecipeSelectionPanel<Recipe>(
            recipeListModel.getRecipesList());

    private PancakeSelectionPanel pancakeSelectionPanel = new PancakeSelectionPanel(pancakeListModel.getPancakeList());

    List<Pancake> pancakeList = new ArrayList<>();
    JList<Pancake> pancakeJList = new JList<>(pancakeListModel);
    JPanel orderDetailsPanel;
    JPanel recipeNamePanel;
    JLabel pancakeImageLabel;
    JPanel orderPanel;
    JLabel deliveryImageLabel;

    static PancakeService pancakeService;

    private void updateOrderDetailsPanel(String details) {
        SwingUtilities.invokeLater(() -> {
            orderDetailsArea.append(details);
            //orderDetailsArea.setText(details);
            orderDetailsArea.repaint();
            orderDetailsArea.revalidate();

            // Refresh parent panel (if applicable)
            if (orderDetailsArea.getParent() != null) {
                orderDetailsArea.getParent().revalidate();
                orderDetailsArea.getParent().repaint();
            }
        });
    }

    private JPanel createBuildingPanel() {
        JPanel buildingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel buildingLabel = new JLabel("Building: ");
        buildingField = new JTextField(15); // Adjust the field width as needed

        buildingPanel.add(buildingLabel);
        buildingPanel.add(buildingField);

        return buildingPanel;
    }

    private JPanel createRoomPanel() {
        JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel roomLabel = new JLabel("Room:      ");
        roomField = new JTextField(15);

        roomPanel.add(roomLabel);
        roomPanel.add(roomField);

        return roomPanel;
    }

    public PancakeOrderGUI(PancakeService pancakeService) throws SQLException {
        workflow = new PancakeOrderWorkflow(pancakeService);

        boolean isAppInitialised = workflow.initializePancakeOrderSystem();
        isAppInitialised = true;

        if(!isAppInitialised) {
            System.out.println("PancakeOrderGUI: if(!isAppInitialised)...if: ");
            JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Fatal Error ", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            System.out.println("PancakeOrderGUI: if(!isAppInitialised)...else: ");

            for (Ingredient ingredient : pancakeService.getIngredientList()) {
                recipeIngredientModel.addIngredient(ingredient);
            }

            System.out.println("Updated Ingredient Model: " + recipeIngredientModel.getList());
            System.out.println("Ingredients: " + pancakeService.getIngredientList());
            recipeIngredientsSelectionPanel.setAvailableList(pancakeService.getIngredientList());
            System.out.println("PancakeOrderGUI: recipeIngredientsSelectionPanel.setAvailableList() done...");
            SwingUtilities.invokeLater(() -> {
                //recipeIngredientsSelectionPanel.removeAll();
                recipeIngredientsSelectionPanel.revalidate();
                recipeIngredientsSelectionPanel.repaint();
            });

        }

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
        JPanel orderAndPancakeSelectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Stretch components both ways
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding
        gbc.weightx = 1.0; // Allow horizontal resizing

// Order Panel (Takes Half of the Space)
        gbc.gridy = 0; // First row
        gbc.weighty = 0.3; // 50% height

        // Create the order panel
        /*JPanel orderPanel = createOrderPanel();
        GridBagLayout layout = new GridBagLayout();
        orderPanel.setLayout(layout);*/

        orderPanel = new JPanel();
        orderPanel.setLayout(new GridBagLayout());

// Create and scale the image
        ImageIcon pancakeImage = new ImageIcon(getClass().getResource("/Logo.jpg"));

        if (pancakeImage != null) {
            Image img = pancakeImage.getImage();
            Image scaledImg = img.getScaledInstance(300, 250, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);
            pancakeImageLabel = new JLabel(scaledIcon);
            pancakeImageLabel.setOpaque(true);

            // Position the image in the first row, spanning across all columns
            GridBagConstraints gbcForImage = new GridBagConstraints();
            gbcForImage.gridx = 0;
            gbcForImage.gridy = 0;
            gbcForImage.gridwidth = 3; // Span across multiple columns if needed
            gbcForImage.insets = new Insets(10, 10, 10, 10); // Optional: add padding around the image
            orderPanel.add(pancakeImageLabel, gbcForImage);
        } else {
            System.out.println("Image resource not found!");
        }

// Position the Building panel below the image
        JPanel buildingPanel = createBuildingPanel();  // Assuming this is already created
        GridBagConstraints gbcBuilding = new GridBagConstraints();
        gbcBuilding.gridx = 0;
        gbcBuilding.gridy = 1;
        orderPanel.add(buildingPanel, gbcBuilding);

// Position the Room panel below the Building panel
        JPanel roomPanel = createRoomPanel();  // Assuming this is already created
        GridBagConstraints gbcRoom = new GridBagConstraints();
        gbcRoom.gridx = 0;
        gbcRoom.gridy = 2;
        orderPanel.add(roomPanel, gbcRoom);

// Position the Create Order button below the Room panel
        JButton createOrderButton = new JButton("Create Order");
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 3;
        orderPanel.add(createOrderButton, gbcButton);

// Update the layout
        orderPanel.revalidate();
        orderPanel.repaint();

        createOrderButton.addActionListener(new CreateOrderAction());
        orderAndPancakeSelectionPanel.add(orderPanel, gbc);

        // Pancake Selection Panel (Takes the Other Half)
        gbc.gridy = 1; // Second row
        gbc.weighty = 0.6; // 50% height
        JPanel pancakeSelectionPanel = createPancakeSelectionPanel();
        orderAndPancakeSelectionPanel.add(pancakeSelectionPanel, gbc);

        // Create a panel to hold both buttons side by side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // 10px horizontal gap

        JButton submitOrderButton = new JButton("Submit Order");
        JButton newOrderButton = new JButton("New Order");

        buttonPanel.add(submitOrderButton);
        buttonPanel.add(newOrderButton);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 4;
        gbcButtons.gridwidth = 2; // Span across two columns
        gbcButtons.anchor = GridBagConstraints.CENTER; // Center align
        orderAndPancakeSelectionPanel.add(buttonPanel, gbcButtons);

// Add action listeners
        submitOrderButton.addActionListener(new SubmitOrderAction());
        newOrderButton.addActionListener(new NewOrderAction());

        JScrollPane pancakeSelectionScrollPane = new JScrollPane(orderAndPancakeSelectionPanel);


// Add to Tab
        tabbedPane.addTab("Create Order", pancakeSelectionScrollPane);

        // Maintenance Panel
        JPanel maintenancePanel = createMaintenancePanel();

        JPanel maintenanceSplitPanel = new JPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

// Left side: Ingredients panel
        JPanel ingredientsPanel = new JPanel(new FlowLayout());
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("Ingredients Section"));

        // Field for naming the ingredient list
        listNamePanel = new JPanel();
        listNamePanel.add(new JLabel("Ingredient List Name:"));
        listNameField = new JTextField(15);
        listNamePanel.add(listNameField);

        JPanel newIngredientsPanel = createNewIngredientsPanel();
        ingredientsPanel.add(newIngredientsPanel);

        JPanel ingredientsListCreatorPanel = new JPanel();
        ingredientsListCreatorPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredients To List"));
        ingredientsListCreatorPanel.setLayout(new BoxLayout(ingredientsListCreatorPanel, BoxLayout.Y_AXIS));
        ingredientsListCreatorPanel.add(listNamePanel);
        ingredientsListCreatorPanel.add(recipeIngredientsSelectionPanel);

        recipeIngredientsSelectionPanel.setBackground(Color.blue); // TODO: REmove

        // Button to add the ingredient list to a recipe
        JButton addListToRecipeButton = new JButton("Add Ingredients to Ingredient List");
        ingredientsListCreatorPanel.add(addListToRecipeButton);
        ingredientsPanel.add(ingredientsListCreatorPanel);

        // Action listener for adding the list to recipe
        addListToRecipeButton.addActionListener(e -> {

                    String listName = listNameField.getText().trim();
                    if (listName.isEmpty()) {
                        JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please provide a name for the ingredient list.");
                        return;
                    }

                    setRecipeNameOnRecipeNamePanel(listName);



            // Get selected ingredients from UI
            List<Ingredient> selectedIngredients = recipeIngredientsSelectionPanel.getSelectedQuantities()
                    .keySet()  // Extracts only the ingredients (ignores quantities)
                    .stream()
                    .toList();

// Create a new IngredientsList
            IngredientsList newIngredientsList = new IngredientsList("Custom List");

// Add ingredients to the new list TODO: Remove
            /*for (Ingredient ingredient : selectedIngredients) {
                newIngredientsList.addIngredient(ingredient);
            }*/

            //recipeIngredientsSelectionPanel.addAvailableItem(newIngredientsList);

    /*        recipeIngredientListSelectionPanel.setBackground(Color.red);   TODO: Remove

            recipeIngredientListSelectionPanel.getAvailableModel().addElement(newIngredientsList);
            recipeIngredientListSelectionPanel.revalidate();
            recipeIngredientListSelectionPanel.repaint();*/




                    // Transfer ingredients to recipe (You can implement recipe logic here)
                    addIngredientsToRecipe(listName, newIngredientsList);
                    listNameField.setText("");
                    //Looks like wrong model is being updated? Why is this added to the Create Pancake panel?
            System.out.println("recipeIngredientsSelectionPanel.resetModels(recipeIngredientModel.getIngredientList())");
             recipeIngredientsSelectionPanel.resetModels(recipeIngredientModel.getIngredientList());
                });

        ingredientsPanel.add(addListToRecipeButton);

        JPanel recipePanel = createRecipePanel();
        recipePanel.setBorder(BorderFactory.createTitledBorder("Add New Recipe"));
        recipePanel.setLayout(new FlowLayout());

        JPanel ingredientAndNewRecipePanel = new JPanel();
        ingredientAndNewRecipePanel.setLayout(new BoxLayout(ingredientAndNewRecipePanel, BoxLayout.Y_AXIS));
        ingredientAndNewRecipePanel.add(ingredientsPanel);
        ingredientAndNewRecipePanel.add(recipePanel);


// Right side: Original Maintenance panel
        JPanel originalMaintenancePanel = createMaintenancePanel();
        originalMaintenancePanel.setBorder(BorderFactory.createTitledBorder("Original Maintenance"));

        splitPane.setLeftComponent(ingredientAndNewRecipePanel);
        splitPane.setRightComponent(originalMaintenancePanel);
        splitPane.setDividerLocation(0.5);  // Split the panels equally

// Add to the tabbed pane
        tabbedPane.addTab("Maintenance", splitPane);

        // Bottom Panel: Order Details and History
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2));
        orderDetailsPanel = new JPanel(new BorderLayout());
        orderDetailsPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderDetailsArea = new JTextArea(10, 30);
        JScrollPane orderDetailsPane = new JScrollPane(orderDetailsArea);
        orderDetailsPanel.add(orderDetailsPane);

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

    private JPanel createPancakeSelectionPanel() throws SQLException {
        JPanel pancakePanel = new JPanel();
        pancakePanel.setBorder(BorderFactory.createTitledBorder("Add Pancakes"));
        pancakePanel.setPreferredSize(new Dimension(400, 70)); // Adjust height as needed
        pancakePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        String[] pancakeArray = new String[0];

        //pancakeComboBox = new JComboBox<>(pancakeListModel.getPancakeNames().toArray(pancakeArray)); TODO: Remove

        // Initialize recipe selection panel properly
        PancakeListModel pancakeListModel = new PancakeListModel();
        pancakeSelectionPanel = new PancakeSelectionPanel(pancakeListModel.getPancakeList());

        recipeIngredientListSelectionPanel.setPreferredSize(new Dimension(825, 150));
        recipeIngredientListSelectionPanel.setBackground(Color.GREEN);
        addPancakeButton = new JButton("Add Pancake");
        removePancakeButton = new JButton("Remove Pancake");

        pancakePanel.add(pancakeSelectionPanel);
        pancakePanel.add(addPancakeButton);
        pancakePanel.add(removePancakeButton);

        // Event listeners
        addPancakeButton.addActionListener(new AddPancakeAction());
        removePancakeButton.addActionListener(new RemovePancakeAction());
        return pancakePanel;
    }

    void addIngredientsListToContainer(IngredientsList ingredientList) {
        ingredientsListContainer.addIngredientsList(ingredientList);
    }

    // Method to add ingredients to the recipe (this should be implemented as per your logic)
    private void addIngredientsToRecipe(String listName, IngredientsList ingredientList) {
        addIngredientsListToContainer(ingredientList); // This adds to the Build New Pancake Available Ingredients Lists - right panle On Maintenance Tab.

        // Here we transfer the ingredient list to the recipe (this part can be updated based on your application's logic)
        System.out.println("Adding ingredients to recipe: " + listName);
        for (int i = 0; i < ingredientList.getIngredientList().size(); i++) {
            Ingredient ingredient = ingredientList.getElementAt(i);
            // Add ingredient to the recipe (You can implement your logic to add this to the recipe here)
            System.out.println("Ingredient: " + ingredient);
        }
        // Optionally, clear the list after adding to recipe
        //ingredientListModel.clear();

        JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Ingredients added to recipe: " + listName);
    }

    JPanel createNewIngredientsPanel() {
        // Ingredients Panel
        JPanel newIngredientPanel = new JPanel();
        newIngredientPanel.setPreferredSize(new Dimension(850, 100));

        newIngredientPanel.setBorder(BorderFactory.createTitledBorder("Add New Item"));

        // Name field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setSize(50,20);
        newIngredientPanel.add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setMinimumSize(new Dimension(150, 20));
        nameField.setPreferredSize(new Dimension(150, 20));
        newIngredientPanel.add(nameField);

        // Quantity field
        newIngredientPanel.add(new JLabel("Quantity:"));
        JTextField quantityField = new JTextField();
        quantityField.setMinimumSize(new Dimension(50, 20));
        quantityField.setPreferredSize(new Dimension(50, 20));
        newIngredientPanel.add(quantityField);

        // Unit field
        newIngredientPanel.add(new JLabel("Units:"));
        JTextField unitField = new JTextField();
        unitField.setMinimumSize(new Dimension(25, 20));
        unitField.setPreferredSize(new Dimension(25, 20));
        newIngredientPanel.add(unitField);

        // Button to create ingredient
        JButton createIngredientButton = new JButton("Add Item");
        createIngredientButton.setPreferredSize(new Dimension(85, 20));
        newIngredientPanel.add(createIngredientButton);

        // Button to add new ingredient item to the list
        createIngredientButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String quantity = quantityField.getText().trim();
            String unit = unitField.getText().trim();

            Ingredient ingredient = new Ingredient(name,Double.parseDouble(quantity),unit);

            if (!name.isEmpty() && !quantity.isEmpty() && !unit.isEmpty()) {
                //String ingredient = name + " - " + quantity + " " + unit;
                recipeIngredientModel.addElement(ingredient);  // Add to the list model
                //TODO: MARK THIS
                List<Ingredient> ingredientList = recipeIngredientModel.getIngredientList();
                recipeIngredientsSelectionPanel.setAvailableList(ingredientList);

                System.out.println("createNewIngredientsPanel(): ");
                System.out.println("ingredientList: " + ingredientList);

                SwingUtilities.invokeLater(() -> {
                    recipeIngredientsSelectionPanel.repaint();
                    recipeIngredientsSelectionPanel.revalidate();

                    // Refresh parent panel (if applicable)
                    if (recipeIngredientsSelectionPanel.getParent() != null) {
                        recipeIngredientsSelectionPanel.getParent().revalidate();
                        recipeIngredientsSelectionPanel.getParent().repaint();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "All fields must be filled out.");
            }
        });

        return newIngredientPanel;
    }

    // Ingredients Section
    JPanel createIngredientsPanel() {
        // Ingredient list
        JPanel ingredientListPanel = new JPanel(new BorderLayout());
        ingredientListPanel.setPreferredSize(new Dimension(550, 150));
        ingredientListPanel.setBorder(BorderFactory.createTitledBorder("Ingredients List"));

        //DefaultListModel<String> ingredientListModel = new DefaultListModel<>();
        System.out.println("createIngredientsPanel: ");
        JList<IngredientsList> ingredientList = new JList<>((ListModel) recipeIngredientModel);
        ingredientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ingredientListPanel.add(new JScrollPane(ingredientList), BorderLayout.CENTER);

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
            addIngredientsToRecipe(listName, recipeIngredientModel);
        });

        AvailableIngredientPanel availableIngredientPanel = new AvailableIngredientPanel();
        availableIngredientPanel.setBorder(BorderFactory.createTitledBorder("Available Ingredients:"));

        // Panel to contain both the ingredient entry and the list
        //ingredientContainerPanel = new JPanel();
        ingredientContainerPanel.setLayout(new BoxLayout(ingredientContainerPanel, BoxLayout.Y_AXIS));
        ingredientContainerPanel.add(createNewIngredientsPanel());
        //ingredientContainerPanel.add(new JLabel("Available Ingredients:"), BorderLayout.NORTH);
        ingredientContainerPanel.add(availableIngredientPanel);

        ingredientContainerPanel.add(listNamePanel);
        ingredientContainerPanel.add(ingredientListPanel);
        ingredientContainerPanel.add(addListToRecipeButton);
        //ingredientContainerPanel.add(availableIngredientPanel);

        return ingredientContainerPanel;
    }

    void setRecipeNameOnRecipeNamePanel(String name){
        recipeNameField.setText(name);
    }

    JPanel createRecipeNamePanel() {
        recipeNamePanel = new JPanel();
        recipeNamePanel.add(new JLabel("Recipe Name:"));
        //recipeNameField = new JTextField(20);
        recipeNamePanel.add(recipeNameField);

        return recipeNamePanel;
    }

    private JPanel createRecipePanel() {
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        recipePanel.setBorder(BorderFactory.createTitledBorder("Add New Recipe"));

        // Recipe Name Panel
        JPanel recipeNamePanel = createRecipeNamePanel();

        // Initialize recipe selection panel properly
        RecipeIngredientsListModel recipeIngredientsListModel = new RecipeIngredientsListModel();
        recipeIngredientListSelectionPanel = new IngredientListSelectionPanel<IngredientsList>(
                ingredientsListContainer.getAllIngredientsLists(),
                "Available Ingredients Lists",
                "Selected Ingredients List",
                false);
        recipeIngredientListSelectionPanel.setPreferredSize(new Dimension(825, 200));
        recipeIngredientListSelectionPanel.setBackground(Color.MAGENTA);

        // Button to Add Recipe
        JButton addRecipeButton = new JButton("Add Ingredients List to Recipe");
        addRecipeButton.addActionListener(new AddRecipeAction());

        // Add components to the panel
        recipePanel.add(recipeNamePanel);
        recipePanel.add(recipeIngredientListSelectionPanel); //TODO: THIS IS ACTIVE
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

        // New Pancake Section
        JPanel pancakeCreationPanel = new JPanel(new FlowLayout());
        pancakeCreationPanel.setBorder(BorderFactory.createTitledBorder("Build New Pancake"));
        newPancakeNameField = new JTextField(10);

        // Initialize recipe selection panel properly
        RecipeListModel recipeListModel = new RecipeListModel();
        recipeIngredientListSelectionPanel = new IngredientListSelectionPanel<IngredientsList>(
                ingredientsListContainer.getAllIngredientsLists(),
                "Available Ingredients Lists",
                "Selected Ingredients List",
                false);
        recipeIngredientListSelectionPanel.setPreferredSize(new Dimension(825, 200));
        recipeIngredientListSelectionPanel.setBackground(Color.CYAN);

        addNewPancakeButton = new JButton("Add New Pancake");
        pancakeCreationPanel.add(new JLabel("Pancake Name:"));
        pancakeCreationPanel.add(newPancakeNameField);
        pancakeCreationPanel.add(recipeIngredientListSelectionPanel);
        pancakeCreationPanel.add(addNewPancakeButton);

        // Recipe Section TODO: Moving this to the Ingredients Section....Remove!
        //maintenancePanel.add(recipePanel);
        maintenancePanel.add(pancakeCreationPanel);

        // Event listeners
        addNewPancakeButton.addActionListener(new AddNewPancakeAction());

        return maintenancePanel;
    }

    public class NewOrderAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ImageIcon pancakeImage = new ImageIcon(getClass().getResource("/Logo.jpg"));

            if (pancakeImage != null) {
                Image img = pancakeImage.getImage();
                Image scaledImg = img.getScaledInstance(300, 250, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);

                // Position the image in the first row, spanning across all columns
                GridBagConstraints gbcForImage = new GridBagConstraints();
                gbcForImage.gridx = 0;
                gbcForImage.gridy = 0;
                gbcForImage.gridwidth = 3; // Span across multiple columns if needed
                gbcForImage.insets = new Insets(10, 10, 10, 10); // Optional: add padding around the image
                orderPanel.add(pancakeImageLabel, gbcForImage);

                orderPanel.remove(deliveryImageLabel);
                orderPanel.add(pancakeImageLabel, gbcForImage);
                orderPanel.revalidate();
                orderPanel.repaint();
            } else {
                System.out.println("Image resource not found!");
            }
        }
    }

    private class SubmitOrderAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ImageIcon deliveryIcon = new ImageIcon(getClass().getResource("/Delivery.jpg")); // Ensure this path is correct

            if (deliveryIcon != null) {
                Image img = deliveryIcon.getImage();
                Image scaledImg = img.getScaledInstance(400, 250, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);
                deliveryImageLabel = new JLabel(scaledIcon);
                deliveryImageLabel.setOpaque(true);

                // Position the image in the first row, spanning across all columns
                GridBagConstraints gbcForImage = new GridBagConstraints();
                gbcForImage.gridx = 0;
                gbcForImage.gridy = 0;
                gbcForImage.gridwidth = 3; // Span across multiple columns if needed
                gbcForImage.insets = new Insets(10, 10, 10, 10); // Optional: add padding around the image
                orderPanel.remove(pancakeImageLabel);
                orderPanel.add(deliveryImageLabel, gbcForImage);
                orderPanel.revalidate();
                orderPanel.repaint();
            } else {
                System.out.println("Image resource not found!");
            }

        }
    }

    private class CreateOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String building = buildingField.getText();
                String roomFieldText = roomField.getText();
                int room = Integer.parseInt(roomFieldText);

                buildingField.setText("");
                roomField.setText("");

                updateOrderDetailsPanel("Building: " + building + "\n");
                updateOrderDetailsPanel("Room: " + room + "\n");
                updateOrderDetailsPanel(("==================================================================\n"));
                if (!building.isEmpty() && (room != 0)) {
                    workflow.createOrder(building, room);
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Order Created");

                } else {
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Order NOT Created: Please check building and room values are supplied");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Invalid input.");
            }
        }
    }

    private class AddPancakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get all selected pancakes from pancakeSelectionPanel
            List selectedPancakes = pancakeSelectionPanel.getSelectedModelValues(); // Ensure this method exists

            // Ensure there are items in the selected list
            if (selectedPancakes.isEmpty()) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "No pancakes in the list!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder orderSummary = new StringBuilder();
            orderSummary.append("Order id: ").append("\n");
            orderSummary.append("Pancakes selected for order:").append("\n");
            orderSummary.append("==================================================================\n");

            // Iterate over all selected pancakes
            for (Object pancake : selectedPancakes) {
                int quantity = getQuantityForPancake((Pancake) pancake); // Retrieve quantity
                // Update order summary
                orderSummary.append("Name: ").append(((Pancake) pancake).getName()).append(", Quantity: ").append(quantity).append("\n");

                // Add pancake to workflow
                String wfResult = workflow.addPancakeToOrder(((Pancake) pancake).getName(), quantity);
                orderSummary.append(wfResult);

                // Update order summary TODO: Remove
                //orderSummary.append("Name: ").append(pancake.getName()).append(", Quantity: ").append(quantity).append("\n");
            }

            // Update Order Details Panel
            updateOrderDetailsPanel(orderSummary.toString());
            orderDetailsArea.repaint();
            orderDetailsArea.revalidate();

            // Show success message
            JOptionPane.showMessageDialog(PancakeOrderGUI.this, orderSummary);
        }

        // Retrieve the selected quantity for a pancake
        private int getQuantityForPancake(Pancake pancake) {
            Map<Pancake, Integer> selectedQuantities = pancakeSelectionPanel.getSelectedQuantities();
            return selectedQuantities.getOrDefault(pancake, 1); // Default to 1 if not set
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
            if (!recipeName.isEmpty()) {
                List<IngredientsList> selection = recipeIngredientListSelectionPanel.getSelectedModelValues();
                if(!selection.isEmpty()) {
                    // Call workflow method to add the recipe (you should implement this method)
                    workflow.addRecipe(recipeName, recipeIngredientListSelectionPanel.getSelectedModelValues());
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Recipe added.");
                }
                else {
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "No recipe selected.");
                }
            }
            else {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Recipe Name cannot be empty.");
            }
            /*
            if (!recipeName.isEmpty() && ingredients != null && !ingredients.isEmpty()) {
                // Call workflow method to add the recipe (you should implement this method)
                workflow.addRecipe(recipeName, ingredients);
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Recipe added.");
            }*/
        }
    }

    private class AddNewPancakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String pancakeName = newPancakeNameField.getText();
            if (!pancakeName.isEmpty()) {
                List<Pancake> selectedPancakes = pancakeSelectionPanel.getSelectedModelValues();
                if (!selectedPancakes.isEmpty()) {
                    // Call workflow method to build a new pancake (you should implement this method)
                    workflow.buildNewPancake(pancakeName);
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "New Pancake built.");
                }
                else {
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "No Pancake selected.");
                }

            }
            else {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please enter a new pancake name.");
            }
        }
    }

    private void updateOrderHistory() {
        List<String> orderDetails = workflow.viewOrder();
        orderHistoryArea.setText(String.join("\n", orderDetails));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            pancakeService = null;
            try {
                pancakeService = new PancakeService();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                new PancakeOrderGUI(pancakeService);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
