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
                if (!team.equals("observer")) {
                    gameDAO.removePlayer(request.gameID(), team);
                }
                return new LeaveResult(user, request.gameID());
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
                String user = authSQL.getUser(request.authToken());
                return new ResignResult(user, request.gameID());
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            } catch (DataAccessException e) {
                return new ErrorResult(e.getMessage());
            }
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
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
                return new ConnectResult(e.getMessage(), null, null, null, null);
            }
        } catch (DataAccessException e) {
            return new ConnectResult(e.getMessage(), null, null, null, null);
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

    private boolean checkMoveWorked(String jsonGame, ChessMove move, ChessGame.TeamColor teamColor) {
        ChessGame temp = new Gson().fromJson(jsonGame, ChessGame.class);
        if (temp.getBoard().getPiece(move.getEndPosition()) != null && temp.getBoard().getPiece(move.getEndPosition()).getTeamColor() == teamColor) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWhoseTurn(String jsonGame, ChessGame.TeamColor teamColor, ChessMove move) {
        ChessGame temp = new Gson().fromJson(jsonGame, ChessGame.class);
        if (temp.getBoard().getPiece(move.getEndPosition()).getTeamColor() != teamColor) {
            return true;
        } else {
            return false;
        }
    }

    public ExtraMoveResult extraMoveResult(String authToken, int gameID, ChessMove move) {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            SQLGameDAO gameDAO = new SQLGameDAO();
            try {
                authSQL.checkAuth(authToken);
                if (gameID < 1) {
                    return new ExtraMoveResult("Error: bad request", null, null, false, false, false, null);
                }
                String user = authSQL.getUser(authToken);
                String jsonGame = gameDAO.getJsonGame(gameID);
                String gameState = gameDAO.getGameState(gameID);
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
                String team = gameDAO.getPlayerTeam(gameID, user);
                ChessGame.TeamColor officialTeam = null;
                if (team.equals("black")) {
                    officialTeam = ChessGame.TeamColor.BLACK;
                } else {
                    officialTeam = ChessGame.TeamColor.WHITE;
                }
                if (!checkMoveWorked(jsonGame, move, officialTeam)) {
                    return new ExtraMoveResult("Error: move didn't work", null, null, false, false, false, null);
                }
                if (!checkWhoseTurn(jsonGame, officialTeam, move)) {
                    return new ExtraMoveResult("Error: not your turn", null, null, false, false, false, null);
                }
                return new ExtraMoveResult("", user, jsonGame, inCheck, inCheckmate, inStalemate, officialTeam);
            } catch (NotAuthException n) {
                return new ExtraMoveResult("Error: unauthorized", null, null, false, false, false, null);
            } catch (Exception e) {
                return new ExtraMoveResult(e.getMessage(), null, null, false, false, false, null);
            }
        } catch (DataAccessException e) {
            return new ExtraMoveResult(e.getMessage(), null, null, false, false, false, null);
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
