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
            return new Result(e.getMessage());
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
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
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
                    return new ErrorResult(e.getMessage());
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
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
                    return new ErrorResult(n.getMessage());
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
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
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
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
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public MostBasicResult observeGame(ObserveRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameSQL = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String user = authSQL.getUser(request.authToken());
                String jsonGame = gameSQL.getJsonGame(request.gameID());
                return new ObserveResult(jsonGame, user, request.gameID());
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public MostBasicResult move(MoveRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameDAO = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String user = authSQL.getUser(request.authToken());
                String updatedJsonGame = gameDAO.move(request.gameID(), request.move(), user);
                String gameState = gameDAO.getGameState(request.gameID());
                boolean inCheck = false;
                boolean inCheckmate = false;
                boolean inStalemate = false;
                if (gameState.equals("check")) {
                    inCheck = true;
                } else if (gameState.equals("checkmate")) {
                    inCheckmate = true;
                } else if (gameState.equals("stalemate")) {
                    inStalemate = true;
                }
                return new MoveResult(updatedJsonGame, user, request.gameID(), inCheck, inCheckmate, inStalemate);
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (Exception e) {
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public MostBasicResult redraw(RedrawRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameDAO = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameID() < 1) {
                    return new ErrorResult("Error: bad request");
                }
                String jsonGame = gameDAO.getJsonGame(request.gameID());
                return new RedrawResult(jsonGame);
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (Exception e) {
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }
}
