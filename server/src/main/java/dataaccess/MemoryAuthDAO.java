package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private static Map<String, List<AuthData>> auths = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }

    @Override
    public void createAuth(String username, AuthData auth) throws DataAccessException {
        if (auths.containsKey(username)) {
            auths.get(username).add(auth);
        } else {
            auths.put(username, new ArrayList<>());
            auths.get(username).add(auth);
        }
    }

    @Override
    public void updateAuth(String username, String authToken) throws NotAuthException {
        if (!auths.containsKey(username)) {
            throw new NotAuthException("Error: unauthorized");
        } else {
            auths.get(username).add(new AuthData(username, authToken));
        }
    }
}
