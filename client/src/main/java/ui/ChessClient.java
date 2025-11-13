package ui;

import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;
import sharedservice.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade facade;
    private ClientState currentState = ClientState.PRELOGIN;

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
                case "quit" -> "quit";
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
                return String.format("You are now logged in as %s.", success.username());
            } catch (Exception e) {
                return e.getMessage();
            }
        } else if (currentState != ClientState.PRELOGIN) {
            throw new Exception("You are already logged in and do not need to do so again until you log out.");
        }
        throw new Exception("Expected: login <username> <password>");
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
