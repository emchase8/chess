package service;

import dataaccess.AlreadyTakenException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class UserService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        String newAuthToken = generateToken();
        AuthData newAuth = new AuthData(newAuthToken, registerRequest.username());
        MemoryUserDAO userMem = new MemoryUserDAO();
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            userMem.getUser(registerRequest.username());
        } catch (AlreadyTakenException e) {
            RegisterResult result = new RegisterResult("", "", e.getMessage());
            return result;
        }
        try {
            userMem.createUser(registerRequest.username(), newUser);
        } catch (dataaccess.DataAccessException e) {
            //figure out how exceptions work!!!
            throw new RuntimeException(e);
        }
        try {
            authMem.createAuth(registerRequest.username(), newAuth);
        } catch (dataaccess.DataAccessException e) {
            //figure out how exceptions work!!!
            throw new RuntimeException(e);
        }
        return new RegisterResult(registerRequest.username(), newAuthToken, "");
    }
}
