package service.results;

public record LoginResult(String username, String authToken) implements MostBasicResult {
    @Override
    public String message() {
        return "";
    }
    @Override
    public String username() {
        return username;
    }
    @Override
    public String authToken() {
        return authToken;
    }

    @Override
    public int gameID() {
        return 0;
    }
}
