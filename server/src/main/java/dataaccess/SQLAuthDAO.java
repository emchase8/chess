package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS auths (
        id INT NOT NULL AUTO_INCREMENT,
        username VARCHAR(255) NOT NULL,
        auth_list ??????????????????????????????????????????????????????????????????????????,
        PRIMARY KEY (id)
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
    public void createAuth(String username, AuthData auth) throws DataAccessException {};

    @Override
    public void clear() throws DataAccessException {};

    @Override
    public void updateAuth(String username, String authToken) throws NotAuthException {};

    @Override
    public void checkAuth(String authToken) throws NotAuthException {};

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {};

    @Override
    public String getUser(String authToken) {
        return "";
    }
}
