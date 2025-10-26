package service;


import dataaccess.*;
import model.GameListData;
import service.requests.CreateRequest;
import service.requests.JoinRequest;
import service.requests.ListRequest;
import service.results.*;

import java.util.List;

public class GameService {
    public Result clear() {
        try {
            SQLGameDAO gameSQL = new SQLGameDAO();
            gameSQL.clear();
            return new Result("");
        } catch (DataAccessException e) {
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
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public MostBasicResult createGame(CreateRequest request) {
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            authMem.checkAuth(request.authToken());
            if (request.gameName() == null) {
                return new ErrorResult("Error: bad request");
            }
            MemoryGameDAO gameMem = new MemoryGameDAO();
            try {
                int gameID = gameMem.createGame(request.gameName());
                return new CreateResult(gameID);
            } catch (DataAccessException e) {
                return new ErrorResult("Error: bad request");
            }
        } catch (NotAuthException n) {
            return new ErrorResult("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public MostBasicResult joinGame(JoinRequest request) {
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            authMem.checkAuth(request.authToken());
            //for some reason always null for some tests???
            if (request.team() == null || request.gameID() < 1) {
                return new ErrorResult("Error: bad request");
            }
            String user = authMem.getUser(request.authToken());
            MemoryGameDAO gameMem = new MemoryGameDAO();
            try {
                gameMem.joinGame(user, request.team(), request.gameID());
                return new JoinResult();
            } catch (DataAccessException e) {
                return new ErrorResult("Error: already taken");
            }
        } catch (NotAuthException n) {
            return new ErrorResult("Error: unauthorized");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
