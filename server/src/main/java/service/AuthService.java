package service;

import dataaccess.SQLAuthDAO;
import service.results.Result;

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
}
