package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(String username, AuthData auth) throws DataAccessException;
}
