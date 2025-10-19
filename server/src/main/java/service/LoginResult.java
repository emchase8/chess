package service;

public record LoginResult(String username, String authToken) implements MostBasicResult {
    @Override
    public String message() {
        return "";
    }
}
