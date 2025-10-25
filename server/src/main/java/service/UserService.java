package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.*;

import java.util.UUID;

public class UserService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public Result clear() {
        try {
            SQLUserDAO userSQL = new SQLUserDAO();
            userSQL.clear();
            return new Result("");
        } catch (DataAccessException e) {
            return new Result("Error: unable to clear users");
        }
    }

    public MostBasicResult register(RegisterRequest registerRequest) {
        //UPDATE TO DEAL WITH HASH PASSWORDS
        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        UserData newUser = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());
        String newAuthToken = generateToken();
        AuthData newAuth = new AuthData(newAuthToken, registerRequest.username());
        try {
            SQLUserDAO userSQL = new SQLUserDAO();
            SQLAuthDAO authSQL = new SQLAuthDAO();
            try {
                userSQL.getUser(registerRequest.username());
            } catch (AlreadyTakenException e) {
                return new ErrorResult("Error: already taken");
            } catch (DataAccessException e) {
                return new ErrorResult("Error: database error");
            }
            try {
                userSQL.createUser(registerRequest.username(), newUser);
            } catch (dataaccess.DataAccessException e) {
                return new ErrorResult("Error: bad request");
            }
            try {
                authSQL.createAuth(registerRequest.username(), newAuth);
            } catch (dataaccess.DataAccessException e) {
                return new ErrorResult("Error: bad request");
            }
            return new RegisterResult(registerRequest.username(), newAuthToken);
        } catch (DataAccessException e) {
            return new ErrorResult("Error: database error");
        }
    }

    public MostBasicResult login(LoginRequest loginRequest) {
        //UPDATE TO DEAL WITH HASH PASSWORDS
        MemoryUserDAO userMem = new MemoryUserDAO();
        try {
            if (loginRequest.username() != null && loginRequest.password() != null) {
                userMem.getUser(loginRequest.username());
            } else {
                return new ErrorResult("Error: bad request");
            }
        } catch (AlreadyTakenException e) {
            try {
                userMem.checkPassword(loginRequest.username(), loginRequest.password());
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            }
            MemoryAuthDAO authMem = new MemoryAuthDAO();
            String newAuthToken = generateToken();
            try {
                authMem.updateAuth(loginRequest.username(), newAuthToken);
                return new LoginResult(loginRequest.username(), newAuthToken);
            } catch (NotAuthException n) {
                return new ErrorResult("Error: unauthorized");
            }
        }
        return new ErrorResult("Error: unauthorized");
    }

    public MostBasicResult logout(LogoutRequest logoutRequest) {
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            authMem.checkAuth(logoutRequest.authToken());
            try {
                authMem.deleteAuth(logoutRequest.authToken());
                return new LogoutResult();
            } catch (DataAccessException e) {
                return new ErrorResult("Error:");
            }
        } catch (NotAuthException n) {
            return new ErrorResult("Error: unauthorized");
        }
    }
}
