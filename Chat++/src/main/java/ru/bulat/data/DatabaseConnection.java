package ru.bulat.data;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.*;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5433/postgres";
            String password = "543216789";
            String username = "postgres";
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    private Connection getConnection() {
        return connection;
    }

    private static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public static void writeToDatabaseNewUser(String email, String password) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO clients(email, password, position) VALUES (? , ?, ?)")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, "Client");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

    }

    public static String userVerification(String email, String password) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT password, id, nickname FROM clients where email = ?")) {
            preparedStatement.setString(1, email.trim().toLowerCase());
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String normalPassword = resultSet.getString("password");
                String id = resultSet.getString("id");
                String nickname = resultSet.getString("nickname");
                if ((encoder.matches(password, normalPassword))) {
                    return nickname;
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }
}
