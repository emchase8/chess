package service;

import dataaccess.MemoryAuthDAO;
import service.results.Result;

public class AuthService {
    public Result clear() {
        MemoryAuthDAO authMem = new MemoryAuthDAO();
        try {
            authMem.clear();
            return new Result("");
        } catch (dataaccess.DataAccessException e) {
            //figure out how exceptions work!!!
            return new Result("Error: unable to clear authTokens");
        }
    }
}
