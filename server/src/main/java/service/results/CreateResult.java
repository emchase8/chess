package service.results;

public record CreateResult(int gameID) implements MostBasicResult{
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

    @Override
    public int gameID() {
        return gameID;
    }
}
