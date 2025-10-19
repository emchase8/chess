package service.results;

public record LogoutResult() implements MostBasicResult {
    @Override
    public String authToken() {
        return "";
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public String message() {
        return "";
    }
}
