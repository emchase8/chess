package service.results;

public record JoinResult() implements MostBasicResult{
    @Override
    public String message() {
        return "";
    }

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
}
