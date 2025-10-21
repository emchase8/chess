package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.LoginRequest;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    @BeforeEach
    public void clearEverythingLogin() {
        UserService uService = new UserService();
        GameService gService = new GameService();
        AuthService aService = new AuthService();
        uService.clear();
        gService.clear();
        aService.clear();
    }

    @Test
    public void positiveLogin() {
        RegisterRequest newUser = new RegisterRequest("Orym", "Ashari", "littlemoon@wind.zep");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        LoginRequest loginOrym = new LoginRequest("Orym", "Ashari");
        MostBasicResult loginResult = service.login(loginOrym);
        assertEquals("Orym", loginResult.username());
    }

    @Test
    public void negativeLogin() {
        RegisterRequest newUser = new RegisterRequest("Orym", "Ashari", "littlemoon@wind.zep");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        LoginRequest loginOrym = new LoginRequest("Orym", "Ashari");
        LoginRequest loginImogen = new LoginRequest("Imogen", "Temult");
        MostBasicResult orymLogin = service.login(loginOrym);
        MostBasicResult imogenLogin = service.login(loginImogen);
        assertNotEquals(orymLogin.message(), imogenLogin.message());
        assertEquals("Error: unauthorized", imogenLogin.message());
    }
}
