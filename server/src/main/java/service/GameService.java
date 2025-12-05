package service;


import chess.ChessGame;
import chess.ChessMove;
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
            return new Result("Error: " + e.getMessage());
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
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
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
                    return new ErrorResult("Error: " + e.getMessage());
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
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
                    return new ErrorResult("Error: " + n.getMessage());
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
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
                if (!team.equals("observer")) {
                    gameDAO.removePlayer(request.gameID(), team);
                }
                return new LeaveResult(user, request.gameID());
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
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
                String user = authSQL.getUser(request.authToken());
                String team = gameSQL.getPlayerTeam(request.gameID(), user);
                if (!team.equals("observer")) {
                    String jsonGame = gameSQL.getJsonGame(request.gameID());
                    var serializer = new Gson();
                    var game = serializer.fromJson(jsonGame, ChessGame.class);
                    if (game.isGameActive()) {
                        game.setGameActive(false);
                    } else {
                        return new ErrorResult("Error: this game has already been declared over");
                    }
                    var newJsonGame = serializer.toJson(game);
                    gameSQL.updateGame(request.gameID(), newJsonGame);
                    return new ResignResult(user, request.gameID());
                } else {
                    return new ErrorResult("Error: you can only resign if you are a player");
                }
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
        }
    }

    public ConnectResult connectGame(int gameID, String authToken) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameSQL = new SQLGameDAO();
            try {
                authSQL.checkAuth(authToken);
                if (gameID < 1) {
                    return new ConnectResult("Error: bad request", null, null, null, null);
                }
                String user = authSQL.getUser(authToken);
                String jsonGame = gameSQL.getJsonGame(gameID);
                String team = gameSQL.getPlayerTeam(gameID, user);
                ChessGame.TeamColor officialTeam = null;
                String connectType = "";
                if (team.equals("black")) {
                    officialTeam = ChessGame.TeamColor.BLACK;
                    connectType = "join";
                } else if (team.equals("white")) {
                    officialTeam = ChessGame.TeamColor.WHITE;
                    connectType = "join";
                } else {
                    connectType = "observe";
                }
                return new ConnectResult("", jsonGame, user, connectType, officialTeam);
            } catch (NotAuthException n) {
                return new ConnectResult("Error: unauthorized", null, null, null, null);
            } catch (DataAccessException e) {
                return new ConnectResult("Error: " + e.getMessage(), null, null, null, null);
            }
        } catch (DataAccessException e) {
            return new ConnectResult("Error: " + e.getMessage(), null, null, null, null);
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
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
        }
    }

    public MoveResult move(MoveRequest request) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameDAO = new SQLGameDAO();
            try {
                authSQL.checkAuth(request.authToken());
                if (request.gameID() < 1) {
                    return new MoveResult("Error: bad request", null, null, 0, false, false, false);
                }
                String user = authSQL.getUser(request.authToken());
                String updatedJsonGame = gameDAO.move(request.gameID(), request.move(), user);
                String gameState = gameDAO.getGameState(request.gameID());
                boolean inCheck = false;
                boolean inCheckmate = false;
                boolean inStalemate = false;
                if (gameState.equals("check")) {
                    inCheck = true;
                }
                if (gameState.equals("checkmate")) {
                    inCheckmate = true;
                }
                if (gameState.equals("stalemate")) {
                    inStalemate = true;
                }
                return new MoveResult("", updatedJsonGame, user, request.gameID(), inCheck, inCheckmate, inStalemate);
            } catch (NotAuthException n) {
                return new MoveResult("Error: unauthorized", null, null, 0, false, false, false);

            } catch (Exception e) {
                return new MoveResult("Error: " + e.getMessage(), null, null, 0, false, false, false);
            }
        } catch (DataAccessException e) {
            return new MoveResult("Error: " + e.getMessage(), null, null, 0, false, false, false);
        }
    }

    public String getOtherPlayer(int gameID, String currentUser) {
        try {
            SQLGameDAO gameSQL = new SQLGameDAO();
            return gameSQL.getOtherPlayer(gameID, currentUser);
        } catch (DataAccessException e) {
            return "Error: database error";
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
                return new ErrorResult("Error: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult("Error: " + e.getMessage());
        }
    }
}
