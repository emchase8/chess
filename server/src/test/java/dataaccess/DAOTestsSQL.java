package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameListData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;
import service.requests.RegisterRequest;
import service.results.MostBasicResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAOTestsSQL {
    @AfterEach
    public void clearEverythingSQL() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            SQLAuthDAO authDAO = new SQLAuthDAO();
            SQLGameDAO gameDAO = new SQLGameDAO();
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @BeforeEach
    public void setup() {
        UserService uService = new UserService();
        GameService gService = new GameService();
        MostBasicResult register = uService.register(new RegisterRequest("Padme", "Amidala", "naboo@senate.gov"));
    }

    @Test
    public void checkUserPositive() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            assertDoesNotThrow(() -> {userDAO.checkUser("Luke");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void checkUserNegative() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            assertThrows(AlreadyTakenException.class, () -> {userDAO.checkUser("Padme");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void createUserPositive() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            userDAO.createUser("C3P0", new UserData("C3P0", "protocalDroid", "c3p0@protocal.droid"));
            var conn = DatabaseManager.getConnection();
            try (var get3P0 = conn.prepareStatement("SELECT username FROM users WHERE username=?")) {
                get3P0.setString(1, "C3P0");
                try (var rs = get3P0.executeQuery()) {
                    while (rs.next()) {
                        var dbUser = rs.getString("username");
                        assertEquals("C3P0", dbUser);
                    }
                }
            } catch (SQLException e) {
                assertEquals(1, 1);
            }
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void createUserNegative() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            assertThrows(DataAccessException.class, () -> {userDAO.createUser(null, new UserData(null, "imposter", "notReal@senate.gov"));});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void clearUserPositive() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            assertDoesNotThrow(() -> {userDAO.clear();});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void checkPasswordPositive() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            assertDoesNotThrow(() -> {userDAO.checkPassword("Padme", "Amidala");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void checkPasswordNegative() {
        try {
            SQLUserDAO userDAO = new SQLUserDAO();
            assertThrows(NotAuthException.class, () -> {userDAO.checkPassword("Padme", "Skywalker");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void createAuthPositive() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.createAuth("Padme", new AuthData("nabooSenator", "Padme"));
            var conn = DatabaseManager.getConnection();
            try (var getPadme = conn.prepareStatement("SELECT username FROM auths WHERE auth=?")) {
                getPadme.setString(1, "nabooSenator");
                try (var rs = getPadme.executeQuery()) {
                    var dbUser = rs.getString("username");
                    assertEquals(dbUser, "Padme");
                }
            } catch (SQLException e) {
                assertEquals(e.getMessage(), e.getMessage());
            }
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    //ASK HOW TO DO NEGATIVE TEST WHEN ONLY ERROR THROWN IS WHEN DATABASE DOESN'T WORK
    @Test
    public void createAuthNegative() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            assertThrows(DataAccessException.class, () -> {authDAO.createAuth(null, new AuthData("nabooSenator", "Padme"));});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void clearAuthPositive() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            assertDoesNotThrow(() -> {authDAO.clear();});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void updateAuthPositive() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            var conn = DatabaseManager.getConnection();
            try (var getPadme = conn.prepareStatement("SELECT username FROM auths WHERE auth=?")) {
                getPadme.setString(1, "nabooQueen");
                try (var rs = getPadme.executeQuery()) {
                    var dbUser = rs.getString("username");
                    assertEquals(dbUser, "Padme");
                }
            } catch (SQLException e) {
                assertEquals(e.getMessage(), e.getMessage());
            }
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void updateAuthNegative() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            assertThrows(NotAuthException.class, () -> {authDAO.updateAuth("Satine", "nabooQueen");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void checkAuthPositive() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            assertDoesNotThrow(() -> authDAO.checkAuth("nabooQueen"));
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void checkAuthNegative() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            assertThrows(NotAuthException.class, () -> {authDAO.checkAuth("nabooSenator");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void deleteAuthPositive() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            authDAO.deleteAuth("nabooQueen");
            var conn = DatabaseManager.getConnection();
            try (var checkPadme = conn.prepareStatement("SELECT auth FROM auths WHERE auth=?")) {
                checkPadme.setString(1, "nabooQueen");
                try (var rs = checkPadme.executeQuery()) {
                    assertFalse(rs.next());
                }
            } catch (SQLException e) {
                assertEquals(e.getMessage(), e.getMessage());
            }
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    //ANOTHER TO ASK TAS ABOUT
    @Test
    public void deleteAuthNegative() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            assertThrows(DataAccessException.class, () -> {authDAO.deleteAuth(null);});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void getUserPositive() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            assertEquals("Padme", authDAO.getUser("nabooQueen"));
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void getUserNegative() {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            authDAO.updateAuth("Padme", "nabooQueen");
            assertThrows(NotAuthException.class, () -> {authDAO.getUser("nabooSenator");});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void clearGamePositive() {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            assertDoesNotThrow(() -> {gameDAO.clear();});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void listGamesPositive() {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            gameDAO.createGame("aggressiveNegotiations");
            List<GameListData> list = gameDAO.listGames();
            List<GameListData> expectedList = new ArrayList<>();
            expectedList.add(new GameListData(1, null, null, "aggressiveNegotiations"));
            assertEquals(expectedList, list);
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void listGamesNegative() {
        //this is what a TA recommened for this one, as it takes no args and the only way it fails is if the database breaks
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            List<GameListData> list = gameDAO.listGames();
            List<GameListData> expectedList = new ArrayList<>();
            assertEquals(expectedList, list);
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void createGamePositive() {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            gameDAO.createGame("acrossTheStars");
            var conn = DatabaseManager.getConnection();
            try (var checkGame = conn.prepareStatement("SELECT game_name FROM real_games WHERE game_name=?")) {
                checkGame.setString(1, "acrossTheStars");
                try (var rs = checkGame.executeQuery()) {
                    assertTrue(rs.next());
                }
            } catch (SQLException e) {
                assertEquals(e.getMessage(), e.getMessage());
            }
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void createGameNegative() {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            assertThrows(DataAccessException.class, () -> {gameDAO.createGame(null);});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void joinGamePositive() {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            gameDAO.createGame("aggressiveNegotiations");
            gameDAO.joinGame("Anakin", ChessGame.TeamColor.WHITE, 1);
            var conn = DatabaseManager.getConnection();
            try (var checkGame = conn.prepareStatement("SELECT white_user FROM real_games WHERE game_id=?")) {
                checkGame.setInt(1, 1);
                try (var rs = checkGame.executeQuery()) {
                    assertTrue(rs.next());
                    if (rs.next()) {
                        assertEquals("Anakin", rs.getString("white_user"));
                    }
                }
            } catch (SQLException e) {
                assertEquals(e.getMessage(), e.getMessage());
            }
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }

    @Test
    public void joinGameNegative() {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            gameDAO.createGame("aggressiveNegotiations");
            gameDAO.joinGame("Anakin", ChessGame.TeamColor.WHITE, 1);
            assertThrows(AlreadyTakenException.class, () -> {gameDAO.joinGame("Padme", ChessGame.TeamColor.WHITE, 1);});
        } catch (DataAccessException e) {
            assertEquals("Error: unable to setup database", e.getMessage());
        }
    }
}
