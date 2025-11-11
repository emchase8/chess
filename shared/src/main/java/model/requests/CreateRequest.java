package model.requests;

public record CreateRequest(String authToken, String gameName) implements BasicRequest {}
