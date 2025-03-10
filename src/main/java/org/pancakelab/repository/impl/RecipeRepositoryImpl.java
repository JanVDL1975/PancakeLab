package org.pancakelab.repository.impl;

import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.repository.interfaces.IngredientsListRepository;
import org.pancakelab.repository.interfaces.RecipeRepository;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeRepositoryImpl implements RecipeRepository {
    private final Connection connection; // Connection should be passed in
    private final IngredientsListRepository ingredientsListRepository;

    public RecipeRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.ingredientsListRepository = new IngredientsListRepositoryImpl(connection);
    }

    public void addRecipe(Recipe recipe) {
        String sql = "INSERT INTO Recipes (id, name, description) VALUES (gen_random_uuid(), ?, ?) RETURNING id"; // Generate UUID in SQL

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, recipe.getName());
            statement.setString(2, recipe.getDescription());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    recipe.setId(UUID.fromString(rs.getString(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT id, name, description FROM Recipes";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                recipes.add(new Recipe(
                        (UUID) rs.getObject("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        ingredientsListRepository.findById(UUID.randomUUID()) // Fix this for real linkage
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public Recipe findById(UUID id) {
        String sql = "SELECT id, name, description FROM recipes WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Recipe(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            ingredientsListRepository.findById(UUID.randomUUID()) // Fix this for real linkage
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Recipe> findAll() {
        return getAllRecipes();
    }

    @Override
    public void save(Recipe recipe) {
        String sql = "INSERT INTO recipes (id, name, description) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, recipe.getId().toString());
            statement.setString(2, recipe.getName());
            statement.setString(3, recipe.getDescription());
            statement.executeUpdate();

            ingredientsListRepository.save(recipe.getIngredientsList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No recipe found with ID: " + id);
            } else {
                System.out.println("Recipe deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



