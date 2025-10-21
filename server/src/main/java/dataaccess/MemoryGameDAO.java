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
}
