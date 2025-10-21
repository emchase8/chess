package model;

import chess.ChessGame;

public record JoinData(ChessGame.TeamColor playerColor, int gameID) {}
