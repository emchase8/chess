package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public void getUser(String username) throws AlreadyTakenException {};
    @Override
    public void createUser(String username, UserData user) throws DataAccessException {};
    @Override
    public void clear() throws DataAccessException {};
    @Override
    public void checkPassword(String username, String password) throws NotAuthException {}
}
