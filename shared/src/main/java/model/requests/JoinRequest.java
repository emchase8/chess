package model.requests;

import chess.ChessGame;

public record JoinRequest(String authToken, ChessGame.TeamColor playerColor, int gameID) implements BasicRequest {}
