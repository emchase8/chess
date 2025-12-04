package model.results;

import chess.ChessGame;

public record ExtraMoveResult(String message, String username, String game, boolean inCheck, boolean inCheckmate, boolean inStalemate, ChessGame.TeamColor joinedAs) {
    public String message() {
        return message;
    }

    public String username() {
        return username;
    }

    public boolean inCheck() {
        return inCheck;
    }

    public boolean inCheckmate() {
        return inCheckmate;
    }

    public boolean inStalemate() {
        return inStalemate;
    }

    public String game() {
        return game;
    }

    public ChessGame.TeamColor joinedAs() {
        return joinedAs;
    }
}
