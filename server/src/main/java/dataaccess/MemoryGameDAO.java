package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameListData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private static List<GameData> games = new ArrayList<>();
    private static int gameIDCounter = 1;

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public List<GameListData> listGames() throws DataAccessException {
        List<GameListData> gameList = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            GameData gameData = games.get(i);
            gameList.add(new GameListData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
        }
        return gameList;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).gameName() == gameName) {
                throw new DataAccessException("Game name already taken");
            }
        }
        GameData newGame = new GameData(gameIDCounter, "", "", gameName, new ChessGame());
        games.add(newGame);
        gameIDCounter++;
        return newGame.gameID();
    }

    @Override
    public void joinGame(String user, ChessGame.TeamColor team, int gameID) throws DataAccessException {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).gameID() == gameID) {
                if (team == ChessGame.TeamColor.WHITE && games.get(i).whiteUsername().isEmpty()) {
                    games.set(i, new GameData(gameID, user, games.get(i).blackUsername(), games.get(i).gameName(), games.get(i).game()));
                } else if (team == ChessGame.TeamColor.BLACK && games.get(i).blackUsername().isEmpty()) {
                    games.set(i, new GameData(gameID, games.get(i).whiteUsername(), user, games.get(i).gameName(), games.get(i).game()));
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            }
        }
    }
}
