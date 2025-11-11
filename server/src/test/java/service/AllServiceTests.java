package service;

import chess.ChessGame;
import model.GameListData;
import model.requests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.results.MostBasicResult;
import model.results.Result;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AllServiceTests {
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
        assertEquals(newUser.username(), registerResult.username());
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

    @Test
    public void positiveJoin() {
        RegisterRequest newUser = new RegisterRequest("Percy", "Jackson", "seaweedbrain@chb.org");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        CreateRequest createRequest = new CreateRequest(registerResult.authToken(), "ArgoIII");
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(createRequest);
        JoinRequest joinRequest = new JoinRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, createResult.gameID());
        MostBasicResult joinResult = gameService.joinGame(joinRequest);
        assertEquals("", joinResult.message());
    }

    @Test
    public void negativeJoin() {
        RegisterRequest newUser = new RegisterRequest("Annabeth", "Chase", "wisegirl@chb.org");
        UserService service = new UserService();
        MostBasicResult registerResult = service.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("Leo", "Valdez", "hotstuff@chb.org");
        MostBasicResult registerResult2 = service.register(newUser2);
        CreateRequest createRequest = new CreateRequest(registerResult.authToken(), "ArgoVI");
        GameService gameService = new GameService();
        MostBasicResult createResult = gameService.createGame(createRequest);
        JoinRequest joinRequest = new JoinRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, createResult.gameID());
        MostBasicResult joinResult = gameService.joinGame(joinRequest);
        JoinRequest joinRequest2 = new JoinRequest(registerResult2.authToken(), ChessGame.TeamColor.WHITE, createResult.gameID());
        MostBasicResult joinResult2 = gameService.joinGame(joinRequest2);
        assertEquals("Error: already taken", joinResult2.message());
    }

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
        CreateRequest createRequest2 = new CreateRequest(registerResult2.authToken(), null);
        MostBasicResult createResult2 = gameService.createGame(createRequest2);
        assertEquals("Error: bad request", createResult2.message());
    }
}
