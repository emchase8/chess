package model.requests;

public record RedrawRequest(String authToken, int gameID) implements BasicRequest {
}
