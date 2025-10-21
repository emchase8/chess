package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {
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
    public void positiveRegister() {
        RegisterRequest newUser = new RegisterRequest("Caleb", "Widogast", "frumpkin@sol.edu");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        //how to deal with the basicness????
//        assertEquals(newUser.username(), registerResult.username());
    }

    @Test
    public void negativeRegister() {
        RegisterRequest newUser = new RegisterRequest("Vax", "Rogue", "champ@ravenqueen.rel");
        RegisterRequest newUser2 = new RegisterRequest("Vax", "Rogue", "champ@ravenqueen.rel");
        UserService service = new UserService();
        MostBasicResult result1 = service.register(newUser);
        MostBasicResult result2 = service.register(newUser2);
        assertNotEquals(result1.message(), result2.message());
        assertEquals("Error: already taken", result2.message());
    }
}
