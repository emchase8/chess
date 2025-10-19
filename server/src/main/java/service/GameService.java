package service;


import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;

public class GameService {
    public Result clear() {
        MemoryGameDAO gameMem = new MemoryGameDAO();
        try {
            gameMem.clear();
            return new Result("");
        } catch (DataAccessException e) {
            //figure out how exceptions work
            return new Result("Error: unable to clear games");
        }
    }
}
