package dataaccess;

import model.UserData;

public interface UserDAO {
    void getUser(String username) throws AlreadyTakenException;
    void createUser(String username, UserData user) throws DataAccessException;
}
