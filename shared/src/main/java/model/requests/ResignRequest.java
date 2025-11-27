package model.requests;

public record ResignRequest(String authToken, int gameID) implements BasicRequest {
}
