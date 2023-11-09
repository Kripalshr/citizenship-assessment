package com.example.citizenshipassessment.database;
import com.example.citizenshipassessment.model.CitizenshipData;
import com.example.citizenshipassessment.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {
    private Connection connection;

    public DatabaseConnector(String url, String username, String password) {
        // Initialize the database connection here
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    // Insert user data into the database
    public void insertUser(User user) {
        try {
//            String sql = "INSERT INTO users (f_name, l_name, username, email, password) VALUES (?, ?, ?, ?, ?)";
            String sql = "INSERT INTO users (f_name, l_name, username, email, password, gender, country, DOB, nationality, citizenship_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getGender());
            statement.setString(7, user.getCountry()); // Assuming this is the country field
            statement.setDate(8, user.getDob());
            statement.setString(9, user.getNationality());
            statement.setString(10, user.getCitizenshipNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user data.");
        }
    }

    // Retrieve user data from the database by username
    public User getUserByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user data.");
        }

        return null; // User not found
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
