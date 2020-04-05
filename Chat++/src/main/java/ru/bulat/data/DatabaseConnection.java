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

    public static void writeToDatabaseNewUser(String email, String password, String nickname) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO clients(email, password, position, nickname) VALUES (? , ?, ?, ?)")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, "Client");
            preparedStatement.setString(4, nickname);
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

    public static void deletingUser(String nickname){
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("DELETE from clients where nickname = ?")) {
            preparedStatement.setString(1, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public static int nickIsMatchCheck(String nickname){
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT id FROM nicknames where nickname = ?")) {
            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return Integer.parseInt(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return -1;
    }

    public static void writeNewNickname(String nickname){
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO nicknames(nickname) values(?)")) {
            preparedStatement.setString(1, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public static String gettingEmailByNickname(String nickname){
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT email FROM clients where nickname = ?")) {
            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("email");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }

    public static void changePassword(String password, String nickname){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("UPDATE clients set password = ? where nickname = ?")) {
            preparedStatement.setString(1, hash);
            preparedStatement.setString(2, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
