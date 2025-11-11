package sharedService;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

//how to import these, and do I even have to?
//also ask how to set up tests? i'm guessing we also have to initalize the serverFacade?
import model.results.*;
import model.requests.*;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        boolean needAuth = false;
        var request = buildRequest("POST", "/user", registerRequest, needAuth);
        var response = sendRequest(request);
        return handleRequest(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) throws Exception {
        boolean needAuth = false;
        var request = buildRequest("POST", "/session", loginRequest, needAuth);
        var response = sendRequest(request);
        return handleRequest(response, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws Exception {
        boolean needAuth = true;
        var request = buildRequest("DELETE", "/session", logoutRequest, needAuth);
        var response = sendRequest(request);
        return handleRequest(response, LogoutResult.class);
    }

    public ListResult listGames(ListRequest listRequest) throws Exception {
        boolean needAuth = true;
        var request = buildRequest("GET", "/game", listRequest, needAuth);
        var response = sendRequest(request);
        return handleRequest(response, ListResult.class);
    }

    public CreateResult create(CreateRequest createRequest) throws Exception {
        boolean needAuth = true;
        var request = buildRequest("POST", "/game", createRequest, needAuth);
        var response = sendRequest(request);
        return handleRequest(response, CreateResult.class);
    }

    public JoinResult join(JoinRequest joinRequest) throws Exception {
        boolean needAuth = true;
        var request = buildRequest("PUT", "/game", joinRequest, needAuth);
        var response = sendRequest(request);
        return handleRequest(response, JoinResult.class);
    }

    private HttpRequest buildRequest(String method, String path, BasicRequest body, boolean needAuth) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (needAuth) {
            request.setHeader("authorization", body.authToken());
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private <T> T handleRequest(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (status/100 != 2) {
            var body = response.body();
            var map = new Gson().fromJson(body, HashMap.class);
            if (body != null) {
                throw new Exception(map.get("message").toString());
            }
            throw new Exception("other error:" + map.get("status").toString());
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }
}
