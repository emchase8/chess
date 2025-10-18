package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.*;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
//        javalin.post("/user", context -> registerHandler(context));
        return javalin.port();
    }

    //how to use json stuff
//var serializer = new Gson();
//
//var game = new ChessGame();
//
// serialize to JSON
//var json = serializer.toJson(game);
//
// deserialize back to ChessGame
//game = serializer.fromJson(json, ChessGame.class);


    private registerHandler(Context context) {
        var serializer = new Gson();
        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);
        UserService inst = new UserService();
        RegisterResult result = inst.register(request);
    }

    public void stop() {
        javalin.stop();
    }
}
