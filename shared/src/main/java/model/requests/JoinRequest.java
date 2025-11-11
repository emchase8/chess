package model.requests;

import chess.ChessGame;

public record JoinRequest(String authToken, ChessGame.TeamColor team, int gameID) {}
