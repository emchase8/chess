import ui.ChessClient;

//make sure to make it able to run multiple clients
public class ClientMain {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length == 1) {
            serverURL = args[0];
        }
        try {
            new ChessClient(serverURL).run();
        } catch (Exception e) {
            System.out.printf("unable to start client server: " + e.getMessage());
        }
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
    }
}