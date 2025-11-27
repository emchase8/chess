package service;


import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameListData;
import model.requests.*;
import model.results.*;

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
                if (request.playerColor() == null || request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String user = authSQL.getUser(request.authToken());
                try {
                    gameSQL.joinGame(user, request.playerColor(), request.gameID());
                    String jsonGame = gameSQL.getJsonGame(request.gameID());
                    return new JoinResult(jsonGame, user, request.gameID());
                } catch (AlreadyTakenException e) {
                    return new ErrorResult("Error: already taken");
                } catch (DataAccessException n) {
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

    public MostBasicResult leaveGameService(LeaveRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameDAO = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String user = authSQL.getUser(request.authToken());
                String team = gameDAO.getPlayerTeam(request.gameID(), user);
                gameDAO.removePlayer(request.gameID(), team);
                return new LeaveResult();
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: database error");
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: database error");
        }
    }

    public MostBasicResult resignGameService(ResignRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameSQL = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String jsonGame = gameSQL.getJsonGame(request.gameID());
                var serializer = new Gson();
                var game = serializer.fromJson(jsonGame, ChessGame.class);
                game.setGameActive(false);
                var newJsonGame = serializer.toJson(game);
                gameSQL.updateGame(request.gameID(), newJsonGame);
                return new ResignResult();
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: database error");
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: database error");
        }
    }
}
