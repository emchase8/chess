package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameListData;
import model.requests.*;
import model.results.*;
import sharedservice.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade facade;
    private ClientState currentState = ClientState.PRELOGIN;
    private String clientAuth;

    private String printBlackSquare(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.SET_BG_COLOR_BLACK + "   ";
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_RED + " " + piece.toString().toUpperCase() + " ";
        } else {
            return EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_BLUE + " " + piece.toString().toUpperCase() + " ";
        }
    }

    private String printWhiteSquare(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.SET_BG_COLOR_WHITE + "   ";
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_RED + " " + piece.toString().toUpperCase() + " ";
        } else {
            return EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLUE + " " + piece.toString().toUpperCase() + " ";
        }
    }

    private String printBlackBoard(ChessBoard board) {
        String blackBoardStr = EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"H", "G", "F", "E", "D", "C", "B", "A"};
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            String temp = EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 8; j >= 1; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (j%2 == 0 && i%2 == 0) {
                    temp += printBlackSquare(piece);
                } else if (j%2 == 0 && i%2 != 0) {
                    temp += printWhiteSquare(piece);
                } else if (j%2 != 0 && i%2 == 0) {
                    temp += printWhiteSquare(piece);
                } else {
                    temp += printBlackSquare(piece);
                }
            }
            blackBoardStr += temp + EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        blackBoardStr += EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return blackBoardStr;
    }

    private String printWhiteBoard(ChessBoard board) {
        String whiteBoardStr = EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            String temp = EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (i%2 == 0 && j%2 != 0) {
                    temp += printWhiteSquare(piece);
                } else if (i%2 == 0 && j%2 == 0) {
                    temp += printBlackSquare(piece);
                } else if (i%2 != 0 && j%2 != 0) {
                    temp += printBlackSquare(piece);
                } else {
                    temp += printWhiteSquare(piece);
                }
            }
            whiteBoardStr += temp + EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        whiteBoardStr += EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return whiteBoardStr;
    }

    public ChessClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public void run() {
        System.out.println("Welcome to the magical chess server! Please login or register to begin!");
        System.out.print(help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("Thanks for coming, hope to see you again soon :)\n")) {
            System.out.print(">>> ");
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
                System.out.print(ui.EscapeSequences.RESET_TEXT_COLOR);
                System.out.print(EscapeSequences.RESET_BG_COLOR);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    public String eval(String line) {
        try {
            String[] params = line.toLowerCase().split(" ");
            String myCmd;
            if (params.length > 0) {
                myCmd = params[0];
            } else {
                myCmd = "help";
            }
            String[] neededParams = Arrays.copyOfRange(params, 1, params.length);
            return switch (myCmd) {
                case "register" -> register(neededParams);
                case "login" -> login(neededParams);
                case "quit" -> quit(neededParams);
                case "logout" -> logout(neededParams);
                case "create" -> create(neededParams);
                case "list" -> listGames(neededParams);
                case "join" -> join(neededParams);
                case "observe" -> observe(neededParams);
                case "quitgame" -> quitGame(neededParams);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage() + "\n";
        }
    }

    public String quit(String[] params) throws Exception {
        if (currentState == ClientState.PRELOGIN && params.length == 0) {
            return "Thanks for coming, hope to see you again soon :)\n";
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("Please log out before quitting the program.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("Please leave game play and log out before quitting.\n");
        }
        throw new Exception("Expected: quit");
    }

    public String register(String[] params) throws Exception {
        if (params.length == 3 && currentState == ClientState.PRELOGIN) {
            RegisterRequest myRequest = new RegisterRequest(params[0], params[1], params[2]);
            try {
                RegisterResult success = facade.register(myRequest);
                currentState = ClientState.POSTLOGIN;
                clientAuth = success.authToken();
                return String.format("You are now registered as %s and are logged in.\n", success.username());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState != ClientState.PRELOGIN) {
            throw new Exception("You cannot register a new user while logged in. Please logout and try again.\n");
        }
        throw new Exception("Expected: register <username> <password> <email>\n");
    }

    public String login(String[] params) throws Exception {
        if (params.length == 2 && currentState == ClientState.PRELOGIN) {
            LoginRequest myRequest = new LoginRequest(params[0], params[1]);
            try {
                LoginResult success = facade.login(myRequest);
                currentState = ClientState.POSTLOGIN;
                clientAuth = success.authToken();
                return String.format("You are now logged in as %s.\n", success.username());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState != ClientState.PRELOGIN) {
            throw new Exception("You are already logged in and do not need to do so again until you log out.\n");
        }
        throw new Exception("Expected: login <username> <password>\n");
    }

    public String logout(String[] params) throws Exception {
        if (currentState == ClientState.POSTLOGIN && params.length == 0) {
            LogoutRequest myRequest = new LogoutRequest(clientAuth);
            try {
                LogoutResult success = facade.logout(myRequest);
                currentState = ClientState.PRELOGIN;
                clientAuth = "";
                return "You are now logged out. We hope to see you again soon :)\n";
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in in order to log out.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You must first exit game play in order to logout. Use the quitGame command.\n");
        }
        throw new Exception("Expected: logout\n");
    }

    public String create(String[] params) throws Exception {
        if (params.length == 1 && currentState == ClientState.POSTLOGIN) {
            CreateRequest myRequest = new CreateRequest(clientAuth, params[0]);
            try {
                CreateResult success = facade.create(myRequest);
                return String.format("You have now created a game named %s\n", params[0]);
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to create a game.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You cannot create a game from within gameplay. Please quitGame if you wish to make a game.\n");
        }
        throw new Exception("Expected: create <game name>\n");
    }

    public String listGames(String[] params) throws Exception {
        if (currentState == ClientState.POSTLOGIN && params.length == 0) {
            ListRequest myRequest = new ListRequest(clientAuth);
            try {
                ListResult success = facade.listGames(myRequest);
                String myStr = "Here are all the current games:\n";
                for (int i = 0; i < success.games().size(); i++) {
                    GameListData game = success.games().get(i);
                    String temp = (i+1)
                            + ". game name: "
                            + game.gameName()
                            + " white player: "
                            + game.whiteUsername()
                            + " black player: "
                            + game.blackUsername()
                            + "\n";
                    myStr += temp;
                }
                return myStr;
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to see the games.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You cannot list the possible games from within gameplay. Please quitGame if you wish to see the list.\n");
        }
        throw new Exception("Expected: list\n");
    }

    public String quitGame(String[] params) throws Exception {
        if (currentState == ClientState.GAMEPLAY && params.length == 0) {
            currentState = ClientState.POSTLOGIN;
            return "Thank you for visiting game play. See you again soon :)\n";
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must be in game play in order to exit game play.\n");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must first be logged in and then in game play in order to exit game play.\n");
        }
        throw new Exception("Expected: quitGame\n");
    }

    public String join(String[] params) throws Exception {
        if (params.length == 2 && currentState == ClientState.POSTLOGIN) {
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.BLACK;
            if (params[1].equals("white")) {
                teamColor = ChessGame.TeamColor.WHITE;
            }
            int publicGameID = Integer.parseInt(params[0]);
            ListRequest myRequest2 = new ListRequest(clientAuth);
            try {
                ListResult success = facade.listGames(myRequest2);
                if (publicGameID <= 0 || publicGameID > success.games().size()) {
                    throw new Exception("Invalid game ID, please list games and try again.\n");
                }
            } catch (Exception e) {
                return e.getMessage();
            }
            //get list lenght of games and makes sure we don't go out of that list (same as private game id), alex pedersen
            JoinRequest myRequest = new JoinRequest(clientAuth, teamColor, publicGameID);
            try {
                JoinResult success = facade.join(myRequest);
                currentState = ClientState.GAMEPLAY;
                //write functionality in phase 6 to get the correct chess board
                ChessBoard placeholder = new ChessBoard();
                placeholder.resetBoard();
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    return printWhiteBoard(placeholder);
                } else {
                    return printBlackBoard(placeholder);
                }
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logger in to join a chess game.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You are already in game play mode and cannot join a game, exit with quitGame then try again.\n");
        }
        throw new Exception("Expected: join <game number> <WHITE or BLACK>\n");
    }

    public String observe(String[] params) throws Exception {
        if (params.length == 1 && currentState == ClientState.POSTLOGIN) {
            //write functionality in phase 6 to get the correct chess board
            currentState = ClientState.GAMEPLAY;
            ChessBoard placeholder = new ChessBoard();
            placeholder.resetBoard();
            return printWhiteBoard(placeholder);
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to observe a chess game.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You are already viewing a game, exit with quitGame and then try again\n");
        }
        throw new Exception("Expected: observe <game number>\n");
    }

    public String help() {
        if (currentState == ClientState.PRELOGIN) {
            return """
                    Here are your current options:
                    - to register yourself as a user and login: register <username> <password> <email>
                    - to login as an existing user: login <username> <password>
                    - to list all possible options: help
                    - to quit the program: quit
                    """;
        } else if (currentState == ClientState.POSTLOGIN) {
            return """
                    Here are your current options:
                    - to logout: logout
                    - to create a chess game: create <game name>
                    - to list all current chess games: list
                    - to join a current game: join <game number> <WHITE or BLACK>
                    - to observe a current game: observe <game number>
                    - to list all possible options: help
                    - to quit the program: quit
                    """;
        } else if (currentState == ClientState.GAMEPLAY){
            return """
                    Gameplay is still under construction, thank you for your patience.
                    Here is what you can currently do:
                    - to list all possible options: help
                    - to leave gameplay: quitGame
                    - to quit the program: quit
                    """;
        }
        return "I'm not sure how you got here. Please contact the dev\n";
    }
}
