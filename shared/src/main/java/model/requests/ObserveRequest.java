package model.requests;

public record ObserveRequest(String authToken, int gameID) implements BasicRequest {
}
