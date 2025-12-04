package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.*;
import model.*;
import model.requests.*;
import service.*;
import model.results.MostBasicResult;
import model.results.Result;
import websocket.WebSocketHandler;

public class Server {

    private final Javalin javalin;

    private final WebSocketHandler webSocketHandler;

    public Server() {

        webSocketHandler = new WebSocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        javalin.delete("/db", context -> clearHandler(context));
        javalin.post("/user", context -> registerHandler(context));
        javalin.post("/session", context -> loginHandler(context));
        javalin.delete("/session", context -> logoutHandler(context));
        javalin.get("/game", context -> listHandler(context));
        javalin.post("/game", context-> createHandler(context));
        javalin.put("/game", context -> joinHandler(context));
        javalin.put("/leavegame", context -> leaveHandler(context));
        javalin.put("/resigngame", context -> resignHandler(context));
        javalin.get("/observegame", context -> observeHandler(context));
        javalin.put("/makemove", context -> moveHandler(context));
        javalin.get("/redraw", context -> redrawHandler(context));
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
        Result aResult = authInst.clear();
        Result gResult = gameInst.clear();
        if (uResult.message().equals("Error: unable to clear users")) {
            var json = serializer.toJson(uResult);
            context.status(500);
            context.json(json);
        }
        else if (aResult.message().equals("Error: unable to clear authTokens")) {
            var json = serializer.toJson(aResult);
            context.status(500);
            context.json(json);
        }
        else if (gResult.message().equals("Error: unable to clear games")) {
            var json = serializer.toJson(gResult);
            context.status(500);
            context.json(json);
        } else {
            var json = serializer.toJson(aResult);
            context.status(200);
            context.json(json);
        }
    }

    private void registerHandler(Context context) {
        var serializer = new Gson();
        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);
        UserService inst = new UserService();
        MostBasicResult result = inst.register(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
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
        String currentAuth = context.header("authorization");
        LogoutRequest request = new LogoutRequest(currentAuth);
        UserService inst = new UserService();
        MostBasicResult result = inst.logout(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void listHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        ListRequest request = new ListRequest(currentAuth);
        GameService inst = new GameService();
        MostBasicResult result = inst.listGames(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void createHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        GameName gameName = serializer.fromJson(context.body(), GameName.class);
        CreateRequest request = new CreateRequest(currentAuth, gameName.gameName());
        GameService inst = new GameService();
        MostBasicResult result = inst.createGame(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void joinHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        JoinData joinData = serializer.fromJson(context.body(), JoinData.class);
        JoinRequest request = new JoinRequest(currentAuth, joinData.playerColor(), joinData.gameID());
        GameService inst = new GameService();
        MostBasicResult result = inst.joinGame(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else if (result.message().equals("Error: already taken")) {
            context.status(403);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void leaveHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        LeaveData leaveData = serializer.fromJson(context.body(), LeaveData.class);
        LeaveRequest request = new LeaveRequest(currentAuth, leaveData.gameID());
        GameService inst = new GameService();
        MostBasicResult result = inst.leaveGameService(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void resignHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        ResignData resignData = serializer.fromJson(context.body(), ResignData.class);
        ResignRequest request = new ResignRequest(currentAuth, resignData.gameID());
        GameService inst = new GameService();
        MostBasicResult result = inst.resignGameService(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void observeHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        ObserveData observeData = serializer.fromJson(context.body(), ObserveData.class);
        ObserveRequest request = new ObserveRequest(currentAuth, observeData.gameID());
        GameService inst = new GameService();
        MostBasicResult result = inst.observeGame(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void moveHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        MoveData moveData = serializer.fromJson(context.body(), MoveData.class);
        MoveRequest request = new MoveRequest(currentAuth, moveData.gameID(), moveData.move());
        GameService inst = new GameService();
        MostBasicResult result = inst.move(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: bad request") || result.message().equals("Error: Sorry, you can not make that move. Try again :)")) {
            context.status(400);
        } else if (result.message().equals("Error: unauthorized")) {
            context.status(401);
        } else {
            context.status(500);
        }
        context.json(json);
    }

    private void redrawHandler(Context context) {
        var serializer = new Gson();
        String currentAuth = context.header("authorization");
        RedrawData redrawData = serializer.fromJson(context.body(), RedrawData.class);
        RedrawRequest request = new RedrawRequest(currentAuth, redrawData.gameID());
        GameService inst = new GameService();
        MostBasicResult result = inst.redraw(request);
        var json = serializer.toJson(result);
        if (result.message().isEmpty()) {
            context.status(200);
        } else if (result.message().equals("Error: bad request")) {
            context.status(400);
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
