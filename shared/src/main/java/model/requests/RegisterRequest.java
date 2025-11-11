package model.requests;

public record RegisterRequest(String username, String password, String email) implements BasicRequest {
    @Override
    public String authToken() {return "";}
}
