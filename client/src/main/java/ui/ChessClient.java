package ui;

import chess.*;
import com.google.gson.Gson;
import model.GameListData;
import model.requests.*;
import model.results.*;
import sharedservice.ServerFacade;

import java.util.*;

public class ChessClient {
    private final ServerFacade facade;
    private ClientState currentState = ClientState.PRELOGIN;
    private String clientAuth;
    private int currentGame;
    private boolean isPlayer;
    private final HashMap<String, Integer> colConverter = new HashMap<>() {{
        put("a", 1);
        put("b", 2);
        put("c", 3);
        put("d", 4);
        put("e", 5);
        put("f", 6);
        put("g", 7);
        put("h", 8);
    }};
    private ChessGame.TeamColor currentTeam;

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

    private String printGreenSquare(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.SET_BG_COLOR_GREEN + "   ";
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return EscapeSequences.SET_BG_COLOR_GREEN + EscapeSequences.SET_TEXT_COLOR_RED + " " + piece.toString().toUpperCase() + " ";
        } else {
            return EscapeSequences.SET_BG_COLOR_GREEN + EscapeSequences.SET_TEXT_COLOR_BLUE + " " + piece.toString().toUpperCase() + " ";
        }
    }

    private String printDarkGreenSquare(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.SET_BG_COLOR_DARK_GREEN + "   ";
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_RED + " " + piece.toString().toUpperCase() + " ";
        } else {
            return EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_BLUE + " " + piece.toString().toUpperCase() + " ";
        }
    }

    private String printYellowSquare(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.SET_BG_COLOR_YELLOW + "   ";
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return EscapeSequences.SET_BG_COLOR_YELLOW + EscapeSequences.SET_TEXT_COLOR_RED + " " + piece.toString().toUpperCase() + " ";
        } else {
            return EscapeSequences.SET_BG_COLOR_YELLOW + EscapeSequences.SET_TEXT_COLOR_BLUE + " " + piece.toString().toUpperCase() + " ";
        }
    }

    private boolean isStart(ChessPosition start, int row, int col) {
        if (start.getRow() == row && start.getColumn() == col) {
            return true;
        } return false;
    }

    private boolean isPossible(List<ChessPosition> possible, int row, int col) {
        for (ChessPosition item : possible) {
            if (item.getColumn() == col && item.getRow() == row) {
                return true;
            }
        }
        return false;
    }

    private String printBlackHighlightVersion(ChessPiece piece, int row, int col, ChessPosition start, List<ChessPosition> possible) {
        if (isStart(start, row, col)) {
            return printYellowSquare(piece);
        } else if (isPossible(possible, row, col)) {
            return printDarkGreenSquare(piece);
        } else {
            return printBlackSquare(piece);
        }
    }

    private String printWhiteHighlightVersion(ChessPiece piece, int row, int col, ChessPosition start, List<ChessPosition> possible) {
        if (isStart(start, row, col)) {
            return printYellowSquare(piece);
        } else if (isPossible(possible, row, col)) {
            return printGreenSquare(piece);
        } else {
            return printWhiteSquare(piece);
        }
    }

    private String printBlackHighlighted(ChessBoard board, ChessPosition start, List<ChessPosition> possible) {
        String blackBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"H", "G", "F", "E", "D", "C", "B", "A"};
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 8; j >= 1; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (j%2 == 0 && i%2 == 0) {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                } else if (j%2 == 0 && i%2 != 0) {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                } else if (j%2 != 0 && i%2 == 0) {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                } else {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                }
            }
            blackBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        blackBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return blackBoardStr;
    }

    private String printBlackBoard(ChessBoard board) {
        String blackBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"H", "G", "F", "E", "D", "C", "B", "A"};
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
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
            blackBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        blackBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return blackBoardStr;
    }

    private String printWhiteHighlighted(ChessBoard board, ChessPosition start, List<ChessPosition> possible) {
        String whiteBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (i%2 == 0 && j%2 != 0) {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                } else if (i%2 == 0 && j%2 == 0) {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                } else if (i%2 != 0 && j%2 != 0) {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                } else {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                }
            }
            whiteBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        whiteBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return whiteBoardStr;
    }

    private String printWhiteBoard(ChessBoard board) {
        String whiteBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
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
            whiteBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        whiteBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return whiteBoardStr;
    }

    private ChessPosition strToPosition(String posRep) throws Exception {
        try {
            int col = colConverter.get(String.valueOf(posRep.charAt(0)));
            int row = Character.getNumericValue(posRep.charAt(1));
            return new ChessPosition(row, col);
        } catch (Exception e) {
            throw new Exception("Error: incorrectly formatted position, ex: A1 or F7");
        }
    }

    private List<ChessPosition> makeNice(Collection<ChessMove> mine) {
        List<ChessPosition> newList = new ArrayList<ChessPosition>();
        for (ChessMove item : mine) {
            newList.add(item.getEndPosition());
        }
        return newList;
    }

    private boolean wantToResign() {
        System.out.println("Are you sure you want to resign from your game? This action will end the game for all players.");
        System.out.println("Respond with yes or no.");
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String response = scanner.nextLine();
        if (response.toLowerCase().equals("yes")) {
            return true;
        } else {
            return false;
        }
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
        scanner.close();
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
                //ALSO A LOT OF THESE NEED USERNAMES FOR WS WHICH IS FUN
                case "join" -> join(neededParams);
                case "observe" -> observe(neededParams);
                case "quitgame" -> quitGame(neededParams);
                //ANYTHING THAT HAS A GAME ID PASSED IN NEEDS TO HAVE THE CHECK TO SEE IF THAT GAME ID IS VALID
                case "redraw" -> redrawBoard(neededParams);
                case "leave" -> leaveGame(neededParams);
                case "move" -> makeMove(neededParams);
                case "resign" -> resignGame(neededParams);
                case "highlight" -> highlightLegal(neededParams);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
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
        throw new Exception("Expected: quit\n");
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
                return e.getMessage() + "\n";
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
                //fix error messages
                return e.getMessage() + "\n";
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
                return e.getMessage() + "\n";
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
                return e.getMessage() + "\n";
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
                return e.getMessage() + "\n";
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to see the games.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You cannot list the possible games from within gameplay. Please quitGame if you wish to see the list.\n");
        }
        throw new Exception("Expected: list\n");
    }

    public String quitGame(String[] params) throws Exception {
        if (currentState == ClientState.GAMEPLAY && params.length == 0 && !isPlayer) {
            //make so you can only quit as an observer!!!!
            currentState = ClientState.POSTLOGIN;
            return "Thank you for visiting game play. See you again soon :)\n";
        } else if (isPlayer) {
            throw new Exception("You must use the leave or resign command as a player.\n");
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
                    throw new Exception("Invalid game number, please list games and try again.\n");
                }
            } catch (Exception e) {
                return e.getMessage() + "\n";
            }
            JoinRequest myRequest = new JoinRequest(clientAuth, teamColor, publicGameID);
            try {
                JoinResult success = facade.join(myRequest);
                currentState = ClientState.GAMEPLAY;
                currentGame = success.gameID();
                isPlayer = true;
                currentTeam = teamColor;
                //write functionality in phase 6 to get the correct chess board
                //joinResult now has a json form of the game in it that we can pass into the ws, as well as username and game id
                //do WS stuff!!!!
                ChessGame placeholder = new Gson().fromJson(success.jsonGame(), ChessGame.class);
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    return printWhiteBoard(placeholder.getBoard());
                } else {
                    return printBlackBoard(placeholder.getBoard());
                }
            } catch (Exception e) {
                return e.getMessage() + "\n";
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
            int publicGameNum = Integer.parseInt(params[0]);
            ListRequest myRequest = new ListRequest(clientAuth);
            try {
                ListResult lst = facade.listGames(myRequest);
                if (publicGameNum <= 0 || publicGameNum > lst.games().size()) {
                    throw new Exception("Invalid game number, please list games and try again.\n");
                }
            } catch (Exception e) {
                return e.getMessage() + "\n";
            }
            //write functionality in phase 6 to get the correct chess board
            ObserveRequest myRequest2 = new ObserveRequest(clientAuth, publicGameNum);
            try {
                ObserveResult result = facade.observe(myRequest2);
                currentGame = result.gameID();
                //result has all the stuff needed for the ws to work
                //do WS stuff!!!!!
                currentState = ClientState.GAMEPLAY;
                isPlayer = false;
                //REPLACE WITH THE ACUTAL GAME!!!!
                ChessGame placeholder = new Gson().fromJson(result.jsonGame(), ChessGame.class);
                return printWhiteBoard(placeholder.getBoard());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to observe a chess game.\n");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You are already viewing a game, exit with quitGame and then try again\n");
        }
        throw new Exception("Expected: observe <game number>\n");
    }

    public String leaveGame(String[] params) throws Exception {
        if (params.length == 0 && currentState == ClientState.GAMEPLAY) {
            LeaveRequest myRequest = new LeaveRequest(clientAuth, currentGame);
            try {
                LeaveResult result = facade.leave(myRequest);
                //do WS stuff!!!!
                currentState = ClientState.POSTLOGIN;
                String msg = String.format("You have now left game number %d. Hope to see you again soon.\n", currentGame);
                currentGame = -1;
                isPlayer = false;
                currentTeam = null;
                return msg;
            } catch (Exception e) {
                return e.getMessage() + "\n";
            }
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must either be a game player or be observing a game in order to leave a game.\n");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in and in a game to leave a game.\n");
        }
        throw new Exception("Expected: leave\n");
    }

    public String resignGame(String[] params) throws Exception {
        if (params.length == 0 && currentState == ClientState.GAMEPLAY) {
            boolean doubleCheck = wantToResign();
            if (doubleCheck && isPlayer) {
                ResignRequest myRequest = new ResignRequest(clientAuth, currentGame);
                try {
                    ResignResult result = facade.resign(myRequest);
                    //do WS stuff!!!!
                    currentState = ClientState.POSTLOGIN;
                    String msg = String.format("You have resigned from game number %d and this game is no longer active. Hope to see you again soon.\n", currentGame);
                    currentGame = -1;
                    return msg;
                } catch (Exception e) {
                    return e.getMessage() + "\n";
                }
            } else if (!isPlayer && doubleCheck) {
                return "You must be a player to resign from the game.\n";
            } else {
                return "This is why we double check! Go ahead and keep enjoying the game!\n";
            }
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must be a player in a game to resign.\n");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in and a player in a game in order to resign.\n");
        }
        throw new Exception("Expected: resign\n");
    }

    public String makeMove(String[] params) throws Exception {
        if (params.length == 3 && currentState == ClientState.GAMEPLAY) {
            ChessPosition start = strToPosition(params[0]);
            ChessPosition end = strToPosition(params[1]);
            //this promote will only be access if the piece at the starting spot is a pawn
            //the team doesn't matter because the ChessGame will take care of all those checks, IN THEORY!!!!
            ChessPiece.PieceType promote = null;
            if (params[2].toLowerCase().equals("queen")) {
                promote = ChessPiece.PieceType.QUEEN;
            } else if (params[2].toLowerCase().equals("knight")) {
                promote = ChessPiece.PieceType.KNIGHT;
            } else if (params[2].toLowerCase().equals("bishop")) {
                promote = ChessPiece.PieceType.BISHOP;
            } else if (params[2].toLowerCase().equals("rook")) {
                promote = ChessPiece.PieceType.ROOK;
            }
            ChessMove currentMove = new ChessMove(start, end, promote);
            MoveRequest myRequest = new MoveRequest(clientAuth, currentGame, currentMove);
            try {
                //WHAT THE HECK IS HAPPENING WHEN I DEBUG WITH A BAD MOVE!!!!
                MoveResult result = facade.move(myRequest);
                ChessGame temp = new Gson().fromJson(result.jsonGame(), ChessGame.class);
                if (currentTeam == ChessGame.TeamColor.BLACK) {
                    return printBlackBoard(temp.getBoard());
                } else {
                    return printWhiteBoard(temp.getBoard());
                }
                //do WS stuff!!!!
                //HOW TO REDRAW THE BOARD FOR EVERYONE!!!! (i'm assuming WS does that but I just hadn't though of that before
            } catch (Exception e) {
                return e.getMessage() + "\n";
            }
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must be a player in a game to make a move.\n");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in and a player in a game to make a move.\n");
        }
        throw new Exception("Expected: move <starting position> <ending position> <promotion piece (if pawn) or n/a>\n");
    }

    public String redrawBoard(String[] params) throws Exception {
        if (params.length == 0 && currentState == ClientState.GAMEPLAY) {
            RedrawRequest myRequest = new RedrawRequest(clientAuth, currentGame);
            try {
                RedrawResult result = facade.redraw(myRequest);
                ChessGame temp = new Gson().fromJson(result.jsonGame(), ChessGame.class);
                if (currentTeam == ChessGame.TeamColor.BLACK) {
                    return printBlackBoard(temp.getBoard());
                } else {
                    return printWhiteBoard(temp.getBoard());
                }
            } catch (Exception e) {
                return e.getMessage() + "\n";
            }
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must be a player or an observer to redraw a board.\n");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in and either a player or an observer in order to redraw a board.\n");
        }
        throw new Exception("Expected: redraw\n");
    }

    public String highlightLegal(String[] params) throws Exception {
        if (params.length == 1 && currentState == ClientState.GAMEPLAY) {
            ChessPosition mine = strToPosition(params[0]);
            RedrawRequest myRequest = new RedrawRequest(clientAuth, currentGame);
            try {
                RedrawResult result = facade.redraw(myRequest);
                ChessGame temp = new Gson().fromJson(result.jsonGame(), ChessGame.class);
                Collection<ChessMove> possible = temp.validMoves(mine);
                List<ChessPosition> nicerPossible = makeNice(possible);
                if (currentTeam == ChessGame.TeamColor.BLACK) {
                    return printBlackHighlighted(temp.getBoard(), mine, nicerPossible);
                } else {
                    return printWhiteHighlighted(temp.getBoard(), mine, nicerPossible);
                }
            } catch (Exception e) {
                return e.getMessage() + "\n";
            }
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must be a player or an observer to redraw a board with piece moves highlighted.\n");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in and either a player or an observer to redraw a board with piece moves highlighted.\n");
        }
        throw new Exception("Expected: highlight <piece position>");
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
                    """;
        } else if (currentState == ClientState.GAMEPLAY){
            return """
                    Here are your current options:
                    - to list all possible options: help
                    - to redraw the gameboard: redraw
                    - to leave the game: leave
                    - to make a chess move: move <starting position> <ending position> <promotion piece (if pawn) or n/a>
                    - to resign from a game: resign
                    - to highlight legal moves: highlight <piece position>
                    - to quit the game: quitgame
                    """;
        }
        return "I'm not sure how you got here. Please contact the dev\n";
    }
}
