package model.requests;

public record LeaveRequest(String authToken, int gameID) implements BasicRequest {
}
