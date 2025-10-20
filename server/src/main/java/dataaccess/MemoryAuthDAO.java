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
            auths.get(username).add(new AuthData(authToken, username));
        }
    }

    @Override
    public void checkAuth(String authToken) throws NotAuthException {
        int found = 0;
        for (Map.Entry mapElement : auths.entrySet()) {
            String username = (String)mapElement.getKey();
            AuthData current = new AuthData(authToken, username);
            if (auths.get(username).contains(current)) {
                found = 1;
            }
        }
        if (found == 0) {
            throw new NotAuthException("Error: unauthorized");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (Map.Entry mapElement : auths.entrySet()) {
            String username = (String)mapElement.getKey();
            AuthData current = new AuthData(authToken, username);
            if (auths.get(username).contains(current)) {
                auths.get(username).remove(current);
            }
        }
    }
}

