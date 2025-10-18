package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    Map<String, List<AuthData>> auths = new HashMap<>();

    public void createAuth(String username, AuthData auth) throws DataAccessException {
        auths.get(username).add(auth);
    }
}
