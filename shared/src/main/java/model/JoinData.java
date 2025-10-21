package model;

import chess.ChessGame;

public record JoinData(ChessGame.TeamColor teamColor, int gameID) {}
