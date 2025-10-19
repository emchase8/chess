package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private static List<GameData> games = new ArrayList<>();

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
