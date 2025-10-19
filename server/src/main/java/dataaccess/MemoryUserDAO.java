package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private static Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }

    @Override
    public void getUser(String username) throws AlreadyTakenException {
        if (users.containsKey(username)) {
            throw new AlreadyTakenException("Error: username already taken");
        }
    };

    @Override
    public void createUser(String username, UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        users.put(username, user);
    };
}
