package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.*;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        javalin.delete("/db", context -> clearHandler(context));
        javalin.post("/user", context -> registerHandler(context));
        javalin.post("/session", context -> loginHandler(context));
        javalin.delete("/session", context -> logoutHandler(context));
        return javalin.port();
    }

    //how to use json stuff
//var serializer = new Gson();
//var game = new ChessGame();
// serialize to JSON
//var json = serializer.toJson(game);
// deserialize back to ChessGame
//game = serializer.fromJson(json, ChessGame.class);

    private void clearHandler(Context context) {
        var serializer = new Gson();
        UserService userInst = new UserService();
        AuthService authInst = new AuthService();
        GameService gameInst = new GameService();
        Result uResult = userInst.clear();
        if (uResult.message().equals("Error: unable to clear users")) {
            var json = serializer.toJson(uResult);
            context.status(500);
            context.json(json);
        }
        Result aResult = authInst.clear();
        if (aResult.message().equals("Error: unable to clear authTokens")) {
            var json = serializer.toJson(aResult);
            context.status(500);
            context.json(json);
        }
        Result gResult = gameInst.clear();
        if (gResult.message().equals("Error: unable to clear games")) {
            var json = serializer.toJson(gResult);
            context.status(500);
            context.json(json);
        }
        var json = serializer.toJson(aResult);
        context.status(200);
        context.json(json);
    }

    private void registerHandler(Context context) {
        var serializer = new Gson();
        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);
        UserService inst = new UserService();
        MostBasicResult result = inst.register(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
            //don't need this if register doesn't login
            context.sessionAttribute("auth", result.authToken());
        } else if (result.message().equals("Error: already taken")){
            context.status(403);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void loginHandler(Context context) {
        var serializer = new Gson();
        LoginRequest request = serializer.fromJson(context.body(), LoginRequest.class);
        UserService inst = new UserService();
        MostBasicResult result = inst.login(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
            context.sessionAttribute("auth", result.authToken());
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void logoutHandler(Context context) {
        var serializer = new Gson();
        //doesn't work in normal server test?????
        String currentAuth = context.sessionAttribute("auth");
        LogoutRequest request = new LogoutRequest(currentAuth);
        UserService inst = new UserService();
        MostBasicResult result = inst.logout(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
            context.sessionAttribute("auth", "");
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    public void stop() {
        javalin.stop();
    }
}
