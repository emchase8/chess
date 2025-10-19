package service;

public record RegisterResult(String username, String authToken) implements MostBasicResult {
    @Override
    public String message() {
        return "";
    }
}
