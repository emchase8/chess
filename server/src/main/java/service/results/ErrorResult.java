package service.results;

public record ErrorResult(String message) implements MostBasicResult {
    @Override
    public String authToken() {
        return "";
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public int gameID() {
        return 0;
    }

    @Override
    public String message() {
        return message;
    }
}
