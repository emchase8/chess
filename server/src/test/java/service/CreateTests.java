package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateRequest;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;
import static org.junit.jupiter.api.Assertions.*;

public class CreateTests {
    @BeforeEach
    public void clearEverything() {
        UserService uService = new UserService();
        GameService gService = new GameService();
        AuthService aService = new AuthService();
        uService.clear();
        gService.clear();
        aService.clear();
    }

    @Test
    public void positiveCreate() {
        RegisterRequest newUser = new RegisterRequest("Kaladin", "Stormblessed", "syl@knight.rad");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        CreateRequest createRequest = new CreateRequest(registerResult.authToken(), "Bridge4");
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(createRequest);
        assertEquals("", createResult.message());
    }

    @Test
    public void negativeCreate() {
        RegisterRequest newUser = new RegisterRequest("Kaladin", "Stormblessed", "syl@knight.rad");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        CreateRequest createRequest = new CreateRequest(registerResult.authToken(), "Bridge4");
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(createRequest);
        RegisterRequest newUser2 = new RegisterRequest("Shallan", "Davar", "pattern@knight.rad");
        MostBasicResult registerResult2 = service.register(newUser2);
        CreateRequest createRequest2 = new CreateRequest(registerResult2.authToken(), "Bridge4");
        MostBasicResult createResult2 = gameService.createGame(createRequest2);
        assertEquals("Error: bad request", createResult2.message());
    }
}
