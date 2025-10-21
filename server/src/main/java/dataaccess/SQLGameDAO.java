package dataaccess;

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
}
