package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.repository.interfaces.RecipeRepository;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeRepositoryImpl implements RecipeRepository {
    private Connection connection = DatabaseService.getConnection();

    public RecipeRepositoryImpl() throws SQLException {
    }

    public void addRecipe(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO Recipe (name, description) VALUES (?, ?) RETURNING id";

        try {
            // Using DatabaseService to perform the insert operation
            DatabaseService.executeQuery(sql, rs -> {
                if (rs.next()) {
                    recipe.setId(rs.getInt("id")); // Retrieve the 'id' correctly
                }
            }, recipe.getName(), recipe.getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT id, name, description FROM Recipe";

        try {
            // Using DatabaseService to execute the query
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    recipes.add(new Recipe(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            null));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public void saveRecipe(String name) {
        String sql = "INSERT INTO pancakes (name, description) VALUES (?, ?)";

        try {
            // Using DatabaseService to perform the insert operation
            DatabaseService.executeUpdate(sql, name, "Custom pancake recipe");
            System.out.println("Recipe added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIngredientToRecipe(String pancakeName, String ingredientName) {
        String sql = """
            INSERT INTO pancake_ingredients (pancake_id, ingredient_id)
            VALUES (
                (SELECT id FROM pancakes WHERE name = ?),
                (SELECT id FROM ingredients WHERE name = ?)
            )
        """;

        try {
            // Using DatabaseService to perform the insert operation
            DatabaseService.executeUpdate(sql, pancakeName, ingredientName);
            System.out.println("Ingredient " + ingredientName + " added to " + pancakeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getIngredientsForPancake(String pancakeName) {
        String sql = """
            SELECT i.name FROM ingredients i
            JOIN pancake_ingredients pi ON i.id = pi.ingredient_id
            JOIN pancakes p ON p.id = pi.pancake_id
            WHERE p.name = ?
        """;
        List<String> ingredients = new ArrayList<>();

        try {
            // Using DatabaseService to execute the query
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    ingredients.add(rs.getString("name"));
                }
            }, pancakeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public List<Ingredient> getIngredientsForRecipe(UUID recipeId) throws SQLException {
        String sql = """
        SELECT i.name, ri.quantity, ri.unit 
        FROM ingredients i
        JOIN recipe_ingredient ri ON i.id = ri.ingredient_id
        WHERE ri.recipe_id = ?
    """;

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");  // Use "name" from ingredients table
                    double quantity = rs.getDouble("quantity");
                    String unit = rs.getString("unit");

                    ingredients.add(new Ingredient(name, quantity, unit));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
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
                            findIngredientsForRecipe(id) // Fetch ingredients list
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }

    @Override
    public List<Recipe> findAll() {
        String sql = "SELECT id, name, description FROM recipe";
        List<Recipe> recipes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        findIngredientsForRecipe(resultSet.getInt("id")) // Fetch ingredients
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
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

            saveRecipeIngredients(recipe); // Save associated ingredients
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

    private IngredientsList findIngredientsForRecipe(int recipeId) throws SQLException {
        String sql = "SELECT ingredient_id, quantity, unit FROM recipe_ingredient WHERE recipe_id = ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recipeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ingredients.add(new Ingredient(
                            resultSet.getInt("ingredient_id"),
                            null, // Name can be fetched separately if needed
                            resultSet.getDouble("quantity"),
                            resultSet.getString("unit")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new IngredientsList(recipeId, "Recipe Ingredients", ingredients);
    }

    private void saveRecipeIngredients(Recipe recipe) {
        String deleteSql = "DELETE FROM recipe_ingredient WHERE recipe_id = ?";
        String insertSql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, recipe.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                insertStatement.setInt(1, recipe.getId());
                insertStatement.setInt(2, ingredient.getId());
                insertStatement.setDouble(3, ingredient.getQuantity());
                insertStatement.setString(4, ingredient.getUnit());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



