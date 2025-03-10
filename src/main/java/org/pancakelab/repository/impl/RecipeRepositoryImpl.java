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
    private Connection connection = DatabaseService.getConnection();
    private final IngredientsListRepository ingredientsListRepository = new IngredientsListRepositoryImpl(connection);

    public RecipeRepositoryImpl() throws SQLException {

    }

    public void addRecipe(Recipe recipe) {
        String sql = "INSERT INTO Recipe (name, description) VALUES (?, ?) RETURNING id";

        try {
            DatabaseService.executeQuery(sql, rs -> {
                if (rs.next()) {
                    recipe.setId(rs.getInt("id"));
                }
            }, recipe.getName(), recipe.getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT id, name, description FROM Recipe";

        try {
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    recipes.add(new Recipe(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            ingredientsListRepository.findById(UUID.randomUUID()) // Placeholder for proper linkage
                    ));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public Recipe findById(int id) {
        String sql = "SELECT id, name, description FROM recipe WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Recipe(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            ingredientsListRepository.findById(UUID.randomUUID()) // Placeholder for proper linkage
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
        String sql = "INSERT INTO recipe (id, name, description) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, recipe.getId());
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
        String sql = "DELETE FROM recipe WHERE id = ?";

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



