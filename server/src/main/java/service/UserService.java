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

    public Result clear() {
        MemoryUserDAO userMem = new MemoryUserDAO();
        try {
            userMem.clear();
            return new Result("");
        } catch (dataaccess.DataAccessException e) {
            //figure out how exceptions work!!!
            return new Result("Error: unable to clear users");
        }
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
            //figure out how exceptions work!!!
            return new RegisterResult("", "", "Error: already taken");
        }
        try {
            userMem.createUser(registerRequest.username(), newUser);
        } catch (dataaccess.DataAccessException e) {
            //figure out how exceptions work!!!
            return new RegisterResult("", "", "Error: unable to create user");
        }
        try {
            authMem.createAuth(registerRequest.username(), newAuth);
        } catch (dataaccess.DataAccessException e) {
            //figure out how exceptions work!!!
            return new RegisterResult("", "", "Error: unable to create authToken");
        }
        return new RegisterResult(registerRequest.username(), newAuthToken, "");
    }
}
