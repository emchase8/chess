package model.requests;

public record LogoutRequest(String authToken) implements BasicRequest {}
