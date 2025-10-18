package service;

import chess.ChessGame;

public record JoinRequest(String authToken, ChessGame.TeamColor team, String gameID) {}
