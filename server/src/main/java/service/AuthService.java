package service;

import dataaccess.DataAccessException;
import dataaccess.NotAuthException;
import dataaccess.SQLAuthDAO;
import model.results.Result;

public class AuthService {
    public Result clear() {
        try {
            SQLAuthDAO authSQL = new SQLAuthDAO();
            authSQL.clear();
            return new Result("");
        } catch (dataaccess.DataAccessException e) {
            return new Result("Error: unable to clear authTokens");
        }
    }

    public String getUser(String authToken) throws NotAuthException {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            return authDAO.getUser(authToken);
        } catch (Exception e) {
            //replace with something else later
            throw new NotAuthException(e.getMessage());
        }
    }
}
