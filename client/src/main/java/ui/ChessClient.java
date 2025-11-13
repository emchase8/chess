package ui;

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

    public ChessClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public void run() {
        System.out.println("Welcome to the magical chess server! Please login or register to begin!");
        System.out.print(help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>>");
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
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
                case "quit" -> "Thanks for coming, hope to see you again soon :)";
                case "logout" -> logout();
                case "create" -> create(neededParams);
                case "list" -> listGames();
                case "join" -> join(neededParams);
                case "observe" -> observe(neededParams);
                case "quitGame" -> quitGame();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String register(String[] params) throws Exception {
        if (params.length == 3 && currentState == ClientState.PRELOGIN) {
            RegisterRequest myRequest = new RegisterRequest(params[0], params[1], params[2]);
            try {
                RegisterResult success = facade.register(myRequest);
                currentState = ClientState.POSTLOGIN;
                clientAuth = success.authToken();
                return String.format("You are now registered as %s and are logged in.", success.username());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState != ClientState.PRELOGIN) {
            throw new Exception("You cannot register a new user while logged in. Please logout and try again.");
        }
        throw new Exception("Expected: register <username> <password> <email>");
    }

    public String login(String[] params) throws Exception {
        if (params.length == 2 && currentState == ClientState.PRELOGIN) {
            LoginRequest myRequest = new LoginRequest(params[0], params[1]);
            try {
                LoginResult success = facade.login(myRequest);
                currentState = ClientState.POSTLOGIN;
                clientAuth = success.authToken();
                return String.format("You are now logged in as %s.", success.username());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState != ClientState.PRELOGIN) {
            throw new Exception("You are already logged in and do not need to do so again until you log out.");
        }
        throw new Exception("Expected: login <username> <password>");
    }

    public String logout() throws Exception {
        if (currentState == ClientState.POSTLOGIN) {
            LogoutRequest myRequest = new LogoutRequest(clientAuth);
            try {
                LogoutResult success = facade.logout(myRequest);
                currentState = ClientState.PRELOGIN;
                clientAuth = "";
                return String.format("You are now logged out. We hope to see you again soon :)");
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in in order to log out.");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You must first exit game play in order to logout. Use the quitGame command.");
        }
        throw new Exception("Expected: logout");
    }

    public String create(String[] params) throws Exception {
        if (params.length == 1 && currentState == ClientState.POSTLOGIN) {
            CreateRequest myRequest = new CreateRequest(clientAuth, params[0]);
            try {
                CreateResult success = facade.create(myRequest);
                return String.format("You have now created a game named %s (game id: %d)", params[0], success.gameID());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to create a game.");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You cannot create a game from within gameplay. Please quitGame if you wish to make a game.");
        }
        throw new Exception("Expected: create <game name>");
    }

    public String listGames() throws Exception {
        if (currentState == ClientState.POSTLOGIN) {
            ListRequest myRequest = new ListRequest(clientAuth);
            try {
                ListResult success = facade.listGames(myRequest);
                String myStr = "Here are all the current games:\n";
                for (int i = 0; i < success.games().size(); i++) {
                    GameListData game = success.games().get(i);
                    String temp = (i+1) + ". game name: " + game.gameName() + " white player: " + game.whiteUsername() + " black player: " + game.blackUsername() + "\n";
                    myStr += temp;
                }
                return myStr;
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must be logged in to see the games.");
        } else if (currentState == ClientState.GAMEPLAY) {
            throw new Exception("You cannot list the possible games from within gameplay. Please quitGame if you wish to see the list.");
        }
        throw new Exception("Expected: list");
    }

    public String quitGame() throws Exception {
        if (currentState == ClientState.GAMEPLAY) {
            currentState = ClientState.POSTLOGIN;
            return "Thank you for visiting game play. See you again soon :)";
        } else if (currentState == ClientState.POSTLOGIN) {
            throw new Exception("You must be in game play in order to exit game play.");
        } else if (currentState == ClientState.PRELOGIN) {
            throw new Exception("You must first be logged in and then in game play in order to exit game play.");
        }
        throw new Exception("Expected: quitGame");
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
                    - to join a current game: join <game id> <WHITE or BLACK>
                    - to observe a current game: observe <game id>
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
        return "I'm not sure how you got here. Please contact the dev";
    }
}
