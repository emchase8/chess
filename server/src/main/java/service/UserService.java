package service;

import dataaccess.AlreadyTakenException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.NotAuthException;
import model.AuthData;
import model.UserData;
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
        MemoryUserDAO userMem = new MemoryUserDAO();
        try {
            userMem.clear();
            return new Result("");
        } catch (dataaccess.DataAccessException e) {
            return new Result("Error: unable to clear users");
        }
    }

    public MostBasicResult register(RegisterRequest registerRequest) {
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        String newAuthToken = generateToken();
        AuthData newAuth = new AuthData(newAuthToken, registerRequest.username());
        MemoryUserDAO userMem = new MemoryUserDAO();
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            userMem.getUser(registerRequest.username());
        } catch (AlreadyTakenException e) {
            return new ErrorResult("Error: already taken");
        }
        try {
            userMem.createUser(registerRequest.username(), newUser);
        } catch (dataaccess.DataAccessException e) {
            return new ErrorResult("Error: bad request");
        }
        try {
            authMem.createAuth(registerRequest.username(), newAuth);
        } catch (dataaccess.DataAccessException e) {
            return new ErrorResult("Error: bad request");
        }
        return new RegisterResult(registerRequest.username(), newAuthToken);
    }

    public MostBasicResult login(LoginRequest loginRequest) {
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
            return new LogoutResult();
        } catch (NotAuthException n) {
            return new ErrorResult("Error: unauthorized");
        }
    }
}
