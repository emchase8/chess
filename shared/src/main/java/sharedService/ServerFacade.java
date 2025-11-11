package sharedService;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

//how to import these, and do I even have to?
//also ask how to set up tests? i'm guessing we also have to initalize the serverFacade?
import service.results.*;
import service.requests.*;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        //might have to change to basic result version, in theory you are fine
        return handleRequest(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) throws Exception {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleRequest(response, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws Exception {
        var request = buildRequest("DELETE", "/session", logoutRequest);
        var response = sendRequest(request);
        return handleRequest(response, LogoutResult.class);
    }

    public ListResult listGames(ListRequest listRequest) throws Exception {
        var request = buildRequest("GET", "/game", listRequest);
        var response = sendRequest(request);
        return handleRequest(response, ListResult.class);
    }

    public CreateResult create(CreateRequest createRequest) throws Exception {
        var request = buildRequest("POST", "/game", createRequest);
        var response = sendRequest(request);
        return handleRequest(response, CreateResult.class);
    }

    public JoinResult join(JoinRequest joinRequest) throws Exception {
        var request = buildRequest("PUT", "/game", joinRequest);
        var response = sendRequest(request);
        return handleRequest(response, JoinRequest.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path)).method(method, makeRequestBody(body));
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
