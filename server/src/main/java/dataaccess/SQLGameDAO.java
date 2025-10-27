package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameListData;

import java.sql.SQLException;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabaseForGames();
    }

    private final String[] createGameTable = {
            """
        CREATE TABLE IF NOT EXISTS real_games (
        game_id INT NOT NULL AUTO_INCREMENT,
        white_user VARCHAR(255),
        black_user VARCHAR(255),
        game_name VARCHAR(255) NOT NULL,
        game LONGTEXT NOT NULL,
        PRIMARY KEY (game_id)
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
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE real_games")) {
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
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO real_games (white_user, black_user, game_name, game) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, gameName);
            var serializer = new Gson();
            ChessGame game = new ChessGame();
            var json = serializer.toJson(game);
            preparedStatement.setString(4, json);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
        try (var preparedStatement = conn.prepareStatement("SELECT game_id, game_name FROM real_games WHERE game_name=?")) {
            preparedStatement.setString(1, gameName);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("game_id");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
    }

    @Override
    public void joinGame(String user, ChessGame.TeamColor team, int gameID) throws DataAccessException {};
}
