package ui;

import sharedService.ServerFacade;

public class ChessClient {
    private final ServerFacade facade;

    public ChessClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public void run() {
        System.out.println("Welcome to the magical chess server! Please login or register to begin!");

    }
}
