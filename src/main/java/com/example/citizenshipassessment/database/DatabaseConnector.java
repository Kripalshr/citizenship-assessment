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
            String sql = "INSERT INTO users (f_name, l_name, username, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user data.");
        }
    }
    public void insertCitizenshipData(CitizenshipData citizenshipData) {
        try {
            String sql = "INSERT INTO citizenship_data (first_name, middle_name, last_name, nationality, dob, gender, father_first_name, father_middle_name, mother_first_name, mother_middle_name, mother_last_name, father_last_name, citizenship_number, issuing_country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, citizenshipData.getFirstName());
            statement.setString(2, citizenshipData.getMiddleName());
            statement.setString(3, citizenshipData.getLastName());
            statement.setString(4, citizenshipData.getNationality());
            statement.setDate(5, citizenshipData.getDob());
            statement.setString(6, citizenshipData.getGender());
            statement.setString(7, citizenshipData.getFatherFirstName());
            statement.setString(8, citizenshipData.getFatherMiddleName());
            statement.setString(9, citizenshipData.getMotherFirstName());
            statement.setString(10, citizenshipData.getMotherMiddleName());
            statement.setString(11, citizenshipData.getMotherLastName());
            statement.setString(12, citizenshipData.getFatherLastName());
            statement.setString(13, citizenshipData.getCitizenshipNumber());
            statement.setString(14, citizenshipData.getIssuingCountry());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert citizenship data.");
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
