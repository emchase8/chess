package model.requests;

public record LoginRequest(String username, String password) implements BasicRequest {
    @Override
    public String authToken() {return "";}
}
