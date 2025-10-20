package dataaccess;

import model.GameListData;

import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;
    List<GameListData> listGames() throws DataAccessException;
}
