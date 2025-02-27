package org.pancakelab.view;

import org.pancakelab.PancakeOrderWorkflow;
import org.pancakelab.service.PancakeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Set;

public class PancakeOrderGUI extends JFrame {
    private PancakeOrderWorkflow workflow;
    private JTextArea orderDetailsArea;
    private JComboBox<String> pancakeComboBox;
    private JTextField buildingField;
    private JTextField roomField;
    private JButton createOrderButton;
    private JButton addPancakeButton;
    private JButton viewOrderButton;
    private JButton requestMenuButton;

    public PancakeOrderGUI(PancakeService pancakeService) {
        workflow = new PancakeOrderWorkflow(pancakeService);

        setTitle("Pancake Order System");
        setSize(800, 1050);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,4));

        JPanel orderAndMenuPanel = new JPanel(new GridLayout(2, 3));

        // Panel for viewing the menu
        JPanel viewMenuPanel = new JPanel(new FlowLayout());
        viewMenuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        viewMenuPanel.setSize(new Dimension(500,600));

        JButton requestMenuButton = new JButton("Add Pancake To Menu");

        orderDetailsArea = new JTextArea(10, 30);
        orderDetailsArea.setEditable(false);
        JScrollPane orderDetailsPane = new JScrollPane(orderDetailsArea);
        viewMenuPanel.add(orderDetailsPane);
        viewMenuPanel.add(requestMenuButton);

        orderAndMenuPanel.add(viewMenuPanel);

        // Panel for creating an order
        //JPanel orderPanel = new JPanel(new GridLayout(2, 1));
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        orderPanel.setSize(new Dimension(550, 150));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Create Order"));

        JPanel orderTextPanel = new JPanel();
        orderTextPanel.setLayout(new BoxLayout(orderTextPanel, BoxLayout.X_AXIS));
        orderTextPanel.setSize(new Dimension(100, 30));

        buildingField = new JTextField(50);
        buildingField.setMinimumSize(new Dimension(20, 20));
        buildingField.setPreferredSize(new Dimension(20, 20));



        roomField = new JTextField(3);
        roomField.setMinimumSize(new Dimension(25, 20));
        roomField.setPreferredSize(new Dimension(25, 20));

        JTextField dummy = new JTextField(50);
        JTextField dummy2 = new JTextField(50);
        JTextField dummy3 = new JTextField(50);

        dummy.setOpaque(false);
        dummy2.setOpaque(false);
        dummy3.setOpaque(false);

        dummy.setBorder(BorderFactory.createEmptyBorder());
        dummy2.setBorder(BorderFactory.createEmptyBorder());
        dummy3.setBorder(BorderFactory.createEmptyBorder());

        orderTextPanel.add(dummy);
        orderTextPanel.add(dummy2);
        orderTextPanel.add(new JLabel("Building:"));
        orderTextPanel.add(buildingField);
        orderTextPanel.add(new JLabel("Room:"));
        orderTextPanel.add(roomField);

        orderTextPanel.add(dummy3);



        orderPanel.add(orderTextPanel);

        // Panel for adding pancakes to the order
        JPanel pancakePanel = new JPanel(new FlowLayout());
        pancakePanel.setBorder(BorderFactory.createTitledBorder("Add Pancakes"));

        pancakeComboBox = new JComboBox<>();
        pancakePanel.add(pancakeComboBox);

        addPancakeButton = new JButton("Add Pancake");
        pancakePanel.add(addPancakeButton);

        orderPanel.add(pancakePanel);

        createOrderButton = new JButton("Create Order");
        orderPanel.add(createOrderButton);
        orderAndMenuPanel.add(orderPanel);

        //add(orderAndMenuPanel);

        // Panel for viewing the order
        JPanel viewOrderPanel = new JPanel(new FlowLayout());
        viewOrderPanel.setSize(new Dimension(550, 250));
        viewOrderButton = new JButton("View Order");


        orderDetailsArea = new JTextArea(10, 30);
        orderDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderDetailsArea);
        viewOrderPanel.add(scrollPane);
        viewOrderPanel.add(viewOrderButton);
        orderAndMenuPanel.add(viewOrderPanel);

        add(orderAndMenuPanel);

        // Event listeners
        createOrderButton.addActionListener(new CreateOrderAction());
        addPancakeButton.addActionListener(new AddPancakeAction());
        viewOrderButton.addActionListener(new ViewOrderAction());
        requestMenuButton.addActionListener(new NewMenuItemAction());

        setVisible(true);
    }

    private class  CloseNewMenuItemAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    private class NewMenuItemAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("New Pancake Creator");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new FlowLayout()); // Explicit layout for control

                // Panel for adding a new ingredient
                JPanel newIngredientPanel = new JPanel();
                newIngredientPanel.setPreferredSize(new Dimension(550, 50));
                newIngredientPanel.setBorder(BorderFactory.createTitledBorder("Add New Item"));
                newIngredientPanel.setLayout(new FlowLayout());

                newIngredientPanel.add(new JLabel("Name:"));
                JTextField nameField = new JTextField();
                nameField.setPreferredSize(new Dimension(150, 20));
                newIngredientPanel.add(nameField);

                newIngredientPanel.add(new JLabel("Quantity:"));
                JTextField quantityField = new JTextField();
                quantityField.setPreferredSize(new Dimension(50, 20));
                newIngredientPanel.add(quantityField);

                newIngredientPanel.add(new JLabel("Units:"));
                JTextField unitField = new JTextField();
                unitField.setPreferredSize(new Dimension(50, 20));
                newIngredientPanel.add(unitField);

                JButton createIngredientButton = new JButton("Add Item");
                newIngredientPanel.add(createIngredientButton);

                // Panel for menu display
                JPanel newMenuItemPanel = new JPanel();
                newMenuItemPanel.setBorder(BorderFactory.createTitledBorder("Ingredients for new Pancake"));

                JTextArea newPancakeDetailsArea = new JTextArea(5, 30);
                newPancakeDetailsArea.setEditable(false);
                JScrollPane orderDetailsPane = new JScrollPane(newPancakeDetailsArea);
                newMenuItemPanel.add(orderDetailsPane);

                JButton newMenuItemButton = new JButton("Add Pancake To Menu");
                newMenuItemPanel.add(newMenuItemButton);

                // Main panel
                JPanel newPancakePanel = new JPanel();
                newPancakePanel.setLayout(new BoxLayout(newPancakePanel, BoxLayout.Y_AXIS)); // Better layout
                newPancakePanel.add(newIngredientPanel);
                newPancakePanel.add(newMenuItemPanel);
                newPancakePanel.add(newMenuItemButton);

                frame.add(newPancakePanel);

                // **Correct size handling**
                frame.pack(); // Adjusts size based on components
                frame.setSize(600, 250); // Enforce a reasonable size
                frame.setLocationRelativeTo(null); // Center on screen
                frame.setVisible(true);
            });
        }
    }

    // Action listener for creating an order
    private class CreateOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int building = Integer.parseInt(buildingField.getText());
                int room = Integer.parseInt(roomField.getText());

                workflow.createOrder(building, room);
                updatePancakeMenu();

                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Order Created: " + workflow.currentOrder.getId());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please enter valid numbers for building and room.");
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, ex.getMessage());
            }
        }
    }

    // Action listener for adding a pancake to the order
    private class AddPancakeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String pancakeName = (String) pancakeComboBox.getSelectedItem();
                if (pancakeName == null) {
                    JOptionPane.showMessageDialog(PancakeOrderGUI.this, "Please select a pancake from the menu.");
                    return;
                }

                workflow.addPancakeToOrder(pancakeName, 1); // Add 1 pancake of the selected type
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, pancakeName + " added to the order.");
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, ex.getMessage());
            }
        }
    }

    // Action listener for viewing the order
    private class ViewOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<String> orderDetails = workflow.viewOrder();
                orderDetailsArea.setText(String.join("\n", orderDetails));
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(PancakeOrderGUI.this, ex.getMessage());
            }
        }
    }

    // Updates the pancake menu
    private void updatePancakeMenu() {
        pancakeComboBox.removeAllItems();
        Set<String> availablePancakes = workflow.listAvailablePancakes();
        for (String pancake : availablePancakes) {
            pancakeComboBox.addItem(pancake);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PancakeService pancakeService = new PancakeService();
            new PancakeOrderGUI(pancakeService);
        });
    }
}

