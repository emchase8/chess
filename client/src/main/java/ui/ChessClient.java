package ui;

import sharedservice.ServerFacade;

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
            //do magic loop
        }
        System.out.println();
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
