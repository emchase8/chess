package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;
import sharedservice.ServerFacade;
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
        UserService user = new UserService();
        GameService game = new GameService();
        AuthService auth = new AuthService();
        user.clear();
        game.clear();
        auth.clear();
    }

    @Test
    public void positiveRegister() {
        try {
            RegisterRequest minervaRegister = new RegisterRequest("minerva", "mcgonagall", "catlady@hogwarts.edu");
            RegisterResult minervaResult = facade.register(minervaRegister);
            Assertions.assertEquals("minerva", minervaResult.username());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().isEmpty());
        }
    }

    @Test
    public void negativeRegister() {
        try {
            RegisterRequest severusRegister = new RegisterRequest("severus", "snape", "halfbloodprince@hogwarts.edu");
            RegisterResult serverusResult1 = facade.register(severusRegister);
            Assertions.assertEquals("severus", serverusResult1.username());
            RegisterResult serverusResult2 = facade.register(severusRegister);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("already taken"));
        }
    }

    @Test
    public void loginPositive() {
        try {
            RegisterRequest sybillRequest = new RegisterRequest("sybill", "trelawney", "tealeaves@hogwarts.edu");
            RegisterResult sybillResult = facade.register(sybillRequest);
            LoginRequest sybillLogin = new LoginRequest("sybill", "trelawney");
            LoginResult sybillLoggedIn = facade.login(sybillLogin);
            Assertions.assertEquals("sybill", sybillLoggedIn.username());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().isEmpty());
        }
    }

    @Test
    public void loginNegative() {
        try {
            LoginRequest filiusRequest = new LoginRequest("Filius", "Flitwick");
            LoginResult failedResult = facade.login(filiusRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("unauthorized"));
        }
    }

    @Test
    public void logoutPositive() {
        try {
            RegisterRequest pomonaRequest = new RegisterRequest("pomona", "sprout", "plantqueen@hogwarts.edu");
            RegisterResult pomonaResult = facade.register(pomonaRequest);
            LogoutRequest pomonaLogout = new LogoutRequest(pomonaResult.authToken());
            LogoutResult pomonaOuted = facade.logout(pomonaLogout);
            Assertions.assertTrue(pomonaOuted.message().isEmpty());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().isEmpty());
        }
    }

    @Test
    public void logoutNegative() {
        try {
            LogoutRequest bogusRequest = new LogoutRequest("beazor");
            LogoutResult bogusLogout = facade.logout(bogusRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("unauthorized"));
        }
    }

    @Test
    public void createPositive() {
        try {
            RegisterRequest percyRequest = new RegisterRequest("percy", "jackson", "seaweedbrain@chb.oly");
            RegisterResult percyResult = facade.register(percyRequest);
            CreateRequest makeGame = new CreateRequest(percyResult.authToken(), "i didn't wanna be a halfblood");
            CreateResult gameMade = facade.create(makeGame);
            Assertions.assertTrue(gameMade.gameID() > 0);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().isEmpty());
        }
    }

    @Test
    public void createNegative() {
        try {
            CreateRequest gameWOName = new CreateRequest("wisegirl", "my grand plan");
            CreateResult badGame = facade.create(gameWOName);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("unauthorized"));
        }
    }

    @Test
    public void listPositive() {
        try {
            RegisterRequest leoRequest = new RegisterRequest("leo", "valdez", "fireboy@chb.org");
            RegisterResult leoResult = facade.register(leoRequest);
            CreateRequest makeGame = new CreateRequest(leoResult.authToken(), "festus II");
            CreateResult gameMade = facade.create(makeGame);
            ListRequest iWantGames = new ListRequest(leoResult.authToken());
            ListResult myGames = facade.listGames(iWantGames);
            Assertions.assertTrue(myGames.games().size() == 1);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().isEmpty());
        }
    }

    @Test
    public void listNegative() {
        try {
            ListRequest gimmeGames = new ListRequest("stoll bros are the best");
            ListResult gamesGotNot = facade.listGames(gimmeGames);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("unauthorized"));
        }
    }

    @Test
    public void joinPositive() {
        try {
            RegisterRequest magnusRequest = new RegisterRequest("magnus", "chase", "talkingsword@valhalla.nor");
            RegisterResult magnusResult = facade.register(magnusRequest);
            CreateRequest makeGame = new CreateRequest(magnusResult.authToken(), "i don't wanna fight");
            CreateResult gameMade = facade.create(makeGame);
            JoinRequest joinGame = new JoinRequest(magnusResult.authToken(), ChessGame.TeamColor.WHITE, gameMade.gameID());
            JoinResult gameJoined = facade.join(joinGame);
            Assertions.assertTrue(gameJoined.message().isEmpty());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().isEmpty());
        }
    }

    @Test
    public void joinNegative() {
        try {
            RegisterRequest sadieRequest = new RegisterRequest("sadie", "kane", "british@brooklyn.nome");
            RegisterResult sadieResult = facade.register(sadieRequest);
            RegisterRequest carterRequest = new RegisterRequest("carter", "kane", "pharaoh@brooklyn.nome");
            RegisterResult carterResult = facade.register(carterRequest);
            CreateRequest makeGame = new CreateRequest(carterResult.authToken(), "horus for the win");
            CreateResult cartersGame = facade.create(makeGame);
            JoinRequest joinGameGood = new JoinRequest(carterResult.authToken(), ChessGame.TeamColor.WHITE, 1);
            JoinResult goodJoined = facade.join(joinGameGood);
            JoinRequest joinGameBad = new JoinRequest(sadieResult.authToken(), ChessGame.TeamColor.WHITE, 1);
            JoinResult badJoined = facade.join(joinGameBad);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("already taken"));
        }
    }
}
