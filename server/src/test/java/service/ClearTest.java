package service;
import org.junit.jupiter.api.Test;
import service.requests.CreateRequest;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;
import service.results.Result;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {
    @Test
    public void positiveClear() {
        RegisterRequest newUser = new RegisterRequest("Hiccup", "Haddock", "toothless@berk.nor");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(new CreateRequest(registerResult.authToken(), "fireSword"));
        MostBasicResult createResult2 = gameService.createGame(new CreateRequest(registerResult.authToken(), "astridWins"));
        Result clearUsers = service.clear();
        Result clearGames = gameService.clear();
        AuthService authService = new AuthService();
        Result clearAuth = authService.clear();
        assertEquals("", clearAuth.message());
        assertEquals("", clearGames.message());
        assertEquals("", clearUsers.message());
    }

    @Test
    public void allClearClear() {
        UserService service = new UserService();
        Result clearUsers = service.clear();
        GameService gameService = new GameService();
        Result clearGames = gameService.clear();
        AuthService authService = new AuthService();
        Result clearAuth = authService.clear();
        assertEquals("", clearAuth.message());
        assertEquals("", clearGames.message());
        assertEquals("", clearUsers.message());
    }
}
