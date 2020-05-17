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
            String url = "jdbc:postgresql://localhost:5433/chat";
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

    public static void writeToDatabaseNewUser(String email, String password, String nickname, int id) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO client(email, password, position, nickname_id) VALUES (? , ?, ?, ?)")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, "Client");
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public static String userVerification(String email, String password) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT password, nickname_id FROM client where email = ?")) {
            preparedStatement.setString(1, email.trim().toLowerCase());
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String normalPassword = resultSet.getString("password");
                int id = resultSet.getInt("nickname_id");
                if ((encoder.matches(password, normalPassword))) {
                    return findNicknameById(id);
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }

    public static int emailVerification(String email) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT id FROM client where email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return Integer.parseInt(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return -1;
    }

    public static String findNicknameById(int id) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT nickname FROM nicknames where id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }

    public static int nickIsMatchCheck(String nickname) {
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

    public static int writeNewNickname(String nickname) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO nicknames(nickname) values(?) returning id")) {
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

    public static int insertMessage(String message) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT id FROM messages where message = ?")) {
            preparedStatement.setString(1, message);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return -1;
    }

    public static void updateCount(String message) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("UPDATE messages set count = count+1 where message = ?")) {
            preparedStatement.setString(1, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public static void insertNewMessage(String message) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO messages(message, count) values(?, ?)")) {
            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
