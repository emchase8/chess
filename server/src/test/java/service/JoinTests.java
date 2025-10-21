package service;

import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateRequest;
import service.requests.JoinRequest;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;

import static org.junit.jupiter.api.Assertions.*;

public class JoinTests {
    @BeforeEach
    public void clearEverythingJoin() {
        UserService uService = new UserService();
        GameService gService = new GameService();
        AuthService aService = new AuthService();
        uService.clear();
        gService.clear();
        aService.clear();
    }

    @Test
    public void positiveJoin() {
        RegisterRequest newUser = new RegisterRequest("Percy", "Jackson", "seaweedbrain@chb.org");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        CreateRequest createRequest = new CreateRequest(registerResult.authToken(), "ArgoIII");
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(createRequest);
        JoinRequest joinRequest = new JoinRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, createResult.gameID());
        MostBasicResult joinResult = gameService.joinGame(joinRequest);
        assertEquals("", joinResult.message());
    }

    @Test
    public void negativeJoin() {
        RegisterRequest newUser = new RegisterRequest("Annabeth", "Chase", "wisegirl@chb.org");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("Leo", "Valdez", "hotstuff@chb.org");
        MostBasicResult registerResult2 = service.register(newUser2);
        CreateRequest createRequest = new CreateRequest(registerResult.authToken(), "ArgoVI");
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(createRequest);
        JoinRequest joinRequest = new JoinRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, createResult.gameID());
        MostBasicResult joinResult = gameService.joinGame(joinRequest);
        JoinRequest joinRequest2 = new JoinRequest(registerResult2.authToken(), ChessGame.TeamColor.WHITE, createResult.gameID());
        MostBasicResult joinResult2 = gameService.joinGame(joinRequest2);
        assertEquals("Error: already taken", joinResult2.message());
    }
}
