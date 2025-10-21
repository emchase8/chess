package dataaccess;

import chess.ChessGame;
import model.GameListData;

import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {};

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
