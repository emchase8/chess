package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;
import sharedService.ServerFacade;
import model.results.*;
import model.requests.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() {
        UserService uService = new UserService();
        GameService gService = new GameService();
        AuthService aService = new AuthService();
        uService.clear();
        gService.clear();
        aService.clear();
    }

    @Test
    public void positiveRegister() {
        RegisterRequest minervaRegister = new RegisterRequest("minerva", "mcgonagall", "catlady@hogwarts.edu");
        try {
            RegisterResult minervaResult = facade.register(minervaRegister);
            Assertions.assertEquals(minervaResult.username(), "minerva");
        } catch (Exception e) {
            Assertions.assertTrue(!e.getMessage().isEmpty());
        }
    }

    @Test
    public void negativeRegister() {
        RegisterRequest severusRegister = new RegisterRequest("severus", "snape", "halfbloodprince@hogwarts.edu");
        try {
            RegisterResult serverusResult1 = facade.register(severusRegister);
            Assertions.assertEquals(serverusResult1.username(), "severus");
            RegisterResult serverusResult2 = facade.register(severusRegister);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("already taken"));
        }
    }

}
