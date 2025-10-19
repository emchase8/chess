package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;
    void getUser(String username) throws AlreadyTakenException;
    void createUser(String username, UserData user) throws DataAccessException;
    void checkPassword(String username, String password) throws NotAuthException;
}
