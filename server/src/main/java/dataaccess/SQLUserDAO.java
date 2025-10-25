package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS users (
        username VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        PRIMARY KEY (username)
        )
       """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to setup database");
        }
    }

    @Override
    public void getUser(String username) throws AlreadyTakenException, DataAccessException {
        try {
            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM users WHERE username=?")) {
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var dbUser = rs.getString("username");
                        if (dbUser.equals(username)) {
                            throw new AlreadyTakenException("Error: username already taken");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error: database error");
            }
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: database error");
        }
    };
    @Override
    public void createUser(String username, UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };
    @Override
    public void clear() throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };
    @Override
    public void checkPassword(String username, String password) throws NotAuthException {}
}
