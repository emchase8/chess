package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTests {
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
    public void positiveLogout() {
        RegisterRequest newUser = new RegisterRequest("Ahsoka", "Tano", "snips@jedi.org");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        MostBasicResult loginResult = service.login(new LoginRequest("Ahsoka", "Tano"));
        MostBasicResult logoutResult = service.logout(new LogoutRequest(loginResult.authToken()));
        assertEquals("", logoutResult.message());
    }

    @Test
    public void negativeLogout() {
        RegisterRequest newUser = new RegisterRequest("Anakin", "Skywalker", "skyguy@jedi.org");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        MostBasicResult loginResult = service.login(new LoginRequest("Anakin", "Skywalker"));
        MostBasicResult logoutResult = service.logout(new LogoutRequest("don't like sand"));
        assertEquals("Error: unauthorized", logoutResult.message());
    }
}
