package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.List;

public class FancyPrinting {
    public static String printSquare(ChessPiece piece, String color) {
        if (piece == null) {
            return color + "   ";
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return color + EscapeSequences.SET_TEXT_COLOR_RED + " " + piece.toString().toUpperCase() + " ";
        } else {
            return color+ EscapeSequences.SET_TEXT_COLOR_BLUE + " " + piece.toString().toUpperCase() + " ";
        }
    }

    public static boolean isStart(ChessPosition start, int row, int col) {
        if (start.getRow() == row && start.getColumn() == col) {
            return true;
        } return false;
    }

    public static boolean isPossible(List<ChessPosition> possible, int row, int col) {
        for (ChessPosition item : possible) {
            if (item.getColumn() == col && item.getRow() == row) {
                return true;
            }
        }
        return false;
    }

    public static String printBlackHighlightVersion(ChessPiece piece, int row, int col, ChessPosition start, List<ChessPosition> possible) {
        if (isStart(start, row, col)) {
            return printSquare(piece, EscapeSequences.SET_BG_COLOR_YELLOW);
        } else if (isPossible(possible, row, col)) {
            return printSquare(piece, EscapeSequences.SET_BG_COLOR_DARK_GREEN);
        } else {
            return printSquare(piece, EscapeSequences.SET_BG_COLOR_BLACK);
        }
    }

    public static String printWhiteHighlightVersion(ChessPiece piece, int row, int col, ChessPosition start, List<ChessPosition> possible) {
        if (isStart(start, row, col)) {
            return printSquare(piece, EscapeSequences.SET_BG_COLOR_YELLOW);
        } else if (isPossible(possible, row, col)) {
            return printSquare(piece, EscapeSequences.SET_BG_COLOR_GREEN);
        } else {
            return printSquare(piece, EscapeSequences.SET_BG_COLOR_WHITE);
        }
    }

    public static String printBlackHighlighted(ChessBoard board, ChessPosition start, List<ChessPosition> possible) {
        String blackBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"H", "G", "F", "E", "D", "C", "B", "A"};
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 8; j >= 1; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (j%2 == 0 && i%2 == 0) {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                } else if (j%2 == 0 && i%2 != 0) {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                } else if (j%2 != 0 && i%2 == 0) {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                } else {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                }
            }
            blackBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        blackBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return blackBoardStr;
    }

    public static String printBlackBoard(ChessBoard board) {
        String blackBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"H", "G", "F", "E", "D", "C", "B", "A"};
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 8; j >= 1; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (j%2 == 0 && i%2 == 0) {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_BLACK);
                } else if (j%2 == 0 && i%2 != 0) {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_WHITE);
                } else if (j%2 != 0 && i%2 == 0) {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_WHITE);
                } else {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_BLACK);
                }
            }
            blackBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        blackBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            blackBoardStr += " " + letter + " ";
        }
        blackBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return blackBoardStr;
    }

    public static String printWhiteHighlighted(ChessBoard board, ChessPosition start, List<ChessPosition> possible) {
        String whiteBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (i%2 == 0 && j%2 != 0) {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                } else if (i%2 == 0 && j%2 == 0) {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                } else if (i%2 != 0 && j%2 != 0) {
                    temp += printBlackHighlightVersion(piece, i, j, start, possible);
                } else {
                    temp += printWhiteHighlightVersion(piece, i, j, start, possible);
                }
            }
            whiteBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        whiteBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return whiteBoardStr;
    }

    public static String printWhiteBoard(ChessBoard board) {
        String whiteBoardStr = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            String temp = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i + " ";
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (i%2 == 0 && j%2 != 0) {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_WHITE);
                } else if (i%2 == 0 && j%2 == 0) {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_BLACK);
                } else if (i%2 != 0 && j%2 != 0) {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_BLACK);
                } else {
                    temp += printSquare(piece, EscapeSequences.SET_BG_COLOR_WHITE);
                }
            }
            whiteBoardStr += temp + EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + i
                    + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        whiteBoardStr += EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ";
        for (String letter : letters) {
            whiteBoardStr += " " + letter + " ";
        }
        whiteBoardStr += "   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
        return whiteBoardStr;
    }
}
