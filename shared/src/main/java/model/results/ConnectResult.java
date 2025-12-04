package model.results;

import chess.ChessGame;
import model.GameListData;

import java.util.List;

public record ConnectResult(String message, String jsonGame, String username, String connectionType, ChessGame.TeamColor joinedAs) {
    public String jsonGame() {return jsonGame;}

    public String connectionType() {return connectionType;}

    public ChessGame.TeamColor joinedAs() {return joinedAs;}

    public String username() {
        return username;
    }

    public String message() {
        return message;
    }
}
