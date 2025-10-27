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
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameSQL = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                try {
                    List<GameListData> list = gameSQL.listGames();
                    return new ListResult(list);
                } catch (DataAccessException e) {
                    return new ErrorResult("Error: database error");
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: database error");
        }
    }

    public MostBasicResult createGame(CreateRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameSQL = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameName() == null) {
                    return new ErrorResult("Error: bad request");
                }
                try {
                    int gameID = gameSQL.createGame(request.gameName());
                    return new CreateResult(gameID);
                } catch (DataAccessException e) {
                    return new ErrorResult("Error: database error");
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: database error");
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: database error");
        }
    }

    public MostBasicResult joinGame(JoinRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameSQL = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.team() == null || request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String user = authSQL.getUser(request.authToken());
                try {
                    gameSQL.joinGame(user, request.team(), request.gameID());
                    return new JoinResult();
                } catch (DataAccessException e) {
                    return new ErrorResult("Error: already taken");
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: database error");
        }
    }
}
