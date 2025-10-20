package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(String username, AuthData auth) throws DataAccessException;
    void clear() throws DataAccessException;
    void updateAuth(String username, String authToken) throws NotAuthException;
    void checkAuth(String authToken) throws NotAuthException;
    void deleteAuth(String authToken) throws DataAccessException;
}
