package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabaseForAuth();
    }

    private final String[] createAuthTable = {
        """
        CREATE TABLE IF NOT EXISTS auths (
        auth VARCHAR(255) NOT NULL,
        username VARCHAR(255) NOT NULL,
        PRIMARY KEY (auth)
        )
       """
    };

    private void configureDatabaseForAuth() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createAuthTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to setup database");
        }
    }

    @Override
    public void createAuth(String username, AuthData auth) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO auths (auth, username) VALUES (?, ?)")) {
            preparedStatement.setString(1, auth.authToken());
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };

    @Override
    public void clear() throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };

    @Override
    public void updateAuth(String username, String authToken) throws NotAuthException, DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT auth, username FROM auths WHERE auth=?")) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var dbUsername = rs.getString("username");
                    if (!dbUsername.equals(username)) {
                        throw new NotAuthException("Error: unauthorized");
                    }
                }
                createAuth(username, new AuthData(authToken, username));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };

    @Override
    public void checkAuth(String authToken) throws NotAuthException, DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT auth FROM auths WHERE auth=?")) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    throw new NotAuthException("Error: unauthorized");
                }
                while (rs.next()) {
                    var dbAuth = rs.getString("auth");
                    if (!dbAuth.equals(authToken)) {
                        throw new NotAuthException("Error: unauthorized");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try {
            checkAuth(authToken);
        } catch (NotAuthException e) {
            throw new DataAccessException("Error: database error");
        }
        try (var preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE auth=?")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };

    @Override
    public String getUser(String authToken) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT auth, username FROM auths WHERE auth=?")) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    throw new NotAuthException("Error: bad request");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    }
}
