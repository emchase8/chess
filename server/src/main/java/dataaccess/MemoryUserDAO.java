package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    Map<String, UserData> users = new HashMap<>();

    @Override
    public void getUser(String username) throws AlreadyTakenException {
        if (users.containsKey(username)) {
            throw new AlreadyTakenException("Error: username already taken");
        }
    };

    @Override
    public void createUser(String username, UserData user) throws DataAccessException {
        users.put(username, user);
    };
}
