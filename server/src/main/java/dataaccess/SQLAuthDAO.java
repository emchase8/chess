package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void createAuth(String username, AuthData auth) throws DataAccessException {};

    @Override
    public void clear() throws DataAccessException {};

    @Override
    public void updateAuth(String username, String authToken) throws NotAuthException {};
}
