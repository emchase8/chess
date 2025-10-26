package dataaccess;

import chess.ChessGame;
import model.GameListData;

import java.sql.SQLException;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabaseForGames();
    }

    private final String[] createGameTable = {
            """
        CREATE TABLE IF NOT EXISTS games (
        id INT NOT NULL AUTO_INCREMENT,
        game JSON NOT NULL,
        PRIMARY KEY (id)
        )
       """
    };

    private void configureDatabaseForGames() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to setup database");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    };

    @Override
    public List<GameListData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public void joinGame(String user, ChessGame.TeamColor team, int gameID) throws DataAccessException {};
}
