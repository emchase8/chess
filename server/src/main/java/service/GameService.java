package service;


import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.NotAuthException;
import model.GameListData;
import service.requests.ListRequest;
import service.results.ErrorResult;
import service.results.ListResult;
import service.results.MostBasicResult;
import service.results.Result;

import java.util.List;

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

    public MostBasicResult listGames(ListRequest request) {
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            authMem.checkAuth(request.authToken());
            MemoryGameDAO gameMem = new MemoryGameDAO();
            try {
                List<GameListData> list = gameMem.listGames();
                return new ListResult(list);
            } catch (DataAccessException e) {
                return new ErrorResult("Error:");
            }
        } catch (NotAuthException n) {
            return new ErrorResult("Error: unauthorized");
        }
    }
}
