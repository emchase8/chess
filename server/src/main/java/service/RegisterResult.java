package service;

public record RegisterResult(String username, String authToken) implements MostBasicResult {
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
}
