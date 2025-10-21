package service;

import model.GameListData;
import org.junit.jupiter.api.Test;
import service.requests.CreateRequest;
import service.requests.ListRequest;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListTests {
    @Test
    public void positiveList() {
        RegisterRequest newUser = new RegisterRequest("Gimli", "sonOfGloin", "hammer@fellow.ship");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(new CreateRequest(registerResult.authToken(), "Sprinter"));
        MostBasicResult createResult2 = gameService.createGame(new CreateRequest(registerResult.authToken(), "beardBros"));
        MostBasicResult listResult = gameService.listGames(new ListRequest(registerResult.authToken()));
        List<GameListData> games = listResult.games();
        assertEquals("Sprinter", games.get(0).gameName());
        assertEquals("beardBros", games.get(1).gameName());
    }

    @Test
    public void negativeList() {
        RegisterRequest newUser = new RegisterRequest("Leagolas", "Greenleaf", "bow@fellow.ship");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(new CreateRequest(registerResult.authToken(), "Lembas"));
        MostBasicResult createResult2 = gameService.createGame(new CreateRequest(registerResult.authToken(), "shieldSurf"));
        MostBasicResult listResult = gameService.listGames(new ListRequest("and my bow!"));
        assertEquals("Error: unauthorized", listResult.message());
    }
}
