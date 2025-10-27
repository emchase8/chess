package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameListData;

import java.sql.SQLException;
import java.util.ArrayList;
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
        List<GameListData> gameList = new ArrayList<>();
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT game_id, white_user, black_user, game_name FROM real_games")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int gameId = rs.getInt("game_id");
                    String whiteUser = rs.getString("white_user");
                    String blackUser = rs.getString("black_user");
                    String gameName = rs.getString("game_name");
                    gameList.add(new GameListData(gameId, whiteUser, blackUser, gameName));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
        return gameList;
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
    public void joinGame(String user, ChessGame.TeamColor team, int gameID) throws DataAccessException, AlreadyTakenException {
        var conn = DatabaseManager.getConnection();
        boolean joinGameWhite = false;
        boolean joinGameBlack = false;
        try (var preparedStatement = conn.prepareStatement("SELECT game_id, white_user, black_user FROM real_games WHERE game_id=?")) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                String whiteUser = null;
                String blackUser = null;
                if (rs.next()) {
                    whiteUser = rs.getString("white_user");
                    blackUser = rs.getString("black_user");
                    if (whiteUser == null && team == ChessGame.TeamColor.WHITE) {
                        joinGameWhite = true;
                    } else if (blackUser == null && team == ChessGame.TeamColor.BLACK) {
                        joinGameBlack = true;
                    } else {
                        throw new AlreadyTakenException("Error: already taken");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: database error");
        }
        if (joinGameWhite) {
            try (var joinWhite = conn.prepareStatement("UPDATE real_games SET white_user=? WHERE game_id=?")) {
                joinWhite.setString(1, user);
                joinWhite.setInt(2, gameID);
                joinWhite.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: database error");
            }
        } else if (joinGameBlack) {
            try (var joinBlack = conn.prepareStatement("UPDATE real_games SET black_user=? WHERE game_id=?")) {
                joinBlack.setString(1, user);
                joinBlack.setInt(2, gameID);
                joinBlack.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: database error");
            }
        }
    };
}
