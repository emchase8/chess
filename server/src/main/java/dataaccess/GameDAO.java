package dataaccess;

import chess.ChessGame;
import model.GameListData;

import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;
    List<GameListData> listGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    void joinGame(String user, ChessGame.TeamColor team, int gameID) throws DataAccessException;
}
