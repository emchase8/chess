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
            Assertions.assertEquals("minerva", minervaResult.username());
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }

    @Test
    public void negativeRegister() {
        RegisterRequest severusRegister = new RegisterRequest("severus", "snape", "halfbloodprince@hogwarts.edu");
        try {
            RegisterResult serverusResult1 = facade.register(severusRegister);
            Assertions.assertEquals("severus", serverusResult1.username());
            RegisterResult serverusResult2 = facade.register(severusRegister);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("already taken"));
        }
    }

    @Test
    public void loginPositive() {
        RegisterRequest sybillRequest = new RegisterRequest("sybill", "trelawney", "tealeaves@hogwarts.edu");
        try {
            RegisterResult sybillResult = facade.register(sybillRequest);
            LoginRequest sybillLogin = new LoginRequest("sybill", "trelawney");
            LoginResult sybillLoggedIn = facade.login(sybillLogin);
            Assertions.assertEquals("sybill", sybillLoggedIn.username());
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }

    @Test
    public void loginNegative() {
        LoginRequest filiusRequest = new LoginRequest("Filius", "Flitwick");
        try {
            LoginResult failedResult = facade.login(filiusRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("unauthorized"));
        }
    }

    @Test
    public void logoutPositive() {
        RegisterRequest pomonaRequest = new RegisterRequest("pomona", "sprout", "plantqueen@hogwarts.edu");
        try {
            RegisterResult pomonaResult = facade.register(pomonaRequest);
            LogoutRequest pomonaLogout = new LogoutRequest(pomonaResult.authToken());
            LogoutResult pomonaOuted = facade.logout(pomonaLogout);
            Assertions.assertTrue(pomonaOuted.message().isEmpty());
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }

    @Test
    public void logoutNegative() {
        LogoutRequest bogusRequest = new LogoutRequest("beazor");
        try {
            LogoutResult bogusLogout = facade.logout(bogusRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("unauthorized"));
        }
    }
}
