package ru.bulat.data;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.bulat.model.Client;
import ru.bulat.model.Message;
import ru.bulat.model.Nickname;

import java.sql.*;
import java.util.Optional;

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

    public static Message save(Message message) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO messages(message) values(?) returning id")) {
            preparedStatement.setString(1, message.getMessage());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                message.setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return message;
    }

    public static Nickname save(Nickname nickname) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO nicknames(nickname, count_messages) values(?, ?) returning id")) {
            preparedStatement.setString(1, nickname.getNickname());
            preparedStatement.setInt(2, 0);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                nickname.setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return nickname;
    }

    public static Client save(Client client) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(client.getPassword());
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("INSERT INTO client(email, password, nickname_id) VALUES (?, ?, ?) returning id")) {
            preparedStatement.setString(1, client.getEmail());
            preparedStatement.setString(2, hash);
            preparedStatement.setLong(3, client.getNickname_id());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                client.setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return client;
    }

    public static Optional<Client> findByEmail(String email) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT id, email, password, nickname_id FROM client where email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String email1 = resultSet.getString("email");
                String password = resultSet.getString("password");
                long nickname_id = resultSet.getInt("nickname_id");
                return Optional.ofNullable(new Client().builder()
                        .id(id)
                        .email(email1)
                        .password(password)
                        .nickname_id(nickname_id)
                        .build());
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<Nickname> findById(long id) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT id, nickname, count_messages FROM nicknames where id = ?")) {
            preparedStatement.setLong(1, id);
            return getNicknameByResult(preparedStatement);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<Nickname> findByNickname(String nickname) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("SELECT id, nickname, count_messages FROM nicknames where nickname = ?")) {
            preparedStatement.setString(1, nickname);
            return getNicknameByResult(preparedStatement);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return Optional.empty();
    }

    public static void updateCount(String nickname) {
        try (PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement("UPDATE nicknames set count_messages = count_messages + 1 where nickname = ?")) {
            preparedStatement.setString(1, nickname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private static Optional<Nickname> getNicknameByResult(PreparedStatement preparedStatement){
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String nickname1 = resultSet.getString("nickname");
                long count_messages = resultSet.getInt("count_messages");
                return Optional.ofNullable(new Nickname().builder()
                        .id(id1)
                        .nickname(nickname1)
                        .count_messages(count_messages)
                        .build());
            }
        }catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return Optional.empty();
    }
}
