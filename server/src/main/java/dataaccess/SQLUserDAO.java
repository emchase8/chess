package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
    public void getUser(String username) throws AlreadyTakenException {};
    public void createUser(String username, UserData user) throws DataAccessException {};
}
