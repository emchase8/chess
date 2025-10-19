package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    @Test
    public void positiveLogin() {
        RegisterRequest newUser = new RegisterRequest("Orym", "Ashari", "littlemoon@wind.zep");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        LoginRequest loginOrym = new LoginRequest("Orym", "Ashari");
        MostBasicResult loginResult = service.login(loginOrym);
        //how to deal with the basicness???????
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
