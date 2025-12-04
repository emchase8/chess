package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Session>> connection = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        if (connection.containsKey(gameID)) {
            connection.get(gameID).add(session);
        } else {
            connection.put(gameID, new ArrayList<>());
            connection.get(gameID).add(session);
        }
    }

    public void remove(int gameID, Session session) {
        connection.get(gameID).remove(session);
    }

    public void broadcastExcludeCurrent(int gameID, Session excludedSession, ServerMessage notify) throws IOException {
        String msg = notify.getMessage();
        ArrayList<Session> current = connection.get(gameID);
        for (Session session : current) {
            if (!session.equals(excludedSession)) {
                session.getRemote().sendString(new Gson().toJson(notify));
            }
        }
    }

    public void broadcastOnlyCurrent(int gameID, Session userSession, ServerMessage notify) throws IOException {
        ArrayList<Session> current = connection.get(gameID);
        for (Session option : current) {
            if (option.equals(userSession)) {
                option.getRemote().sendString(new Gson().toJson(notify));
            }
        }
    }

    //make a broadcast method to send the chess board to one person and to the whole session
    public void broadcastGameOne(int gameID, Session session, ServerMessage notify) throws IOException {
        ArrayList<Session> current = connection.get(gameID);
        for (Session option : current) {
            if (option.equals(session)) {
                option.getRemote().sendString(new Gson().toJson(notify));
            }
        }
    }

    public void broadcastGameExculdeCurrent(int gameID, Session excludedSession, ServerMessage notify) throws IOException {
        ArrayList<Session> current = connection.get(gameID);
        for (Session session : current) {
            if (!session.equals(excludedSession)) {
                session.getRemote().sendString(new Gson().toJson(notify));
            }
        }
    }

    public void broadcastGameAll(int gameID, ServerMessage notify) throws IOException {
        ArrayList<Session> current = connection.get(gameID);
        for (Session option : current) {
            option.getRemote().sendString(new Gson().toJson(notify));
        }
    }

    public void broadcastIncludingCurrentUser(int gameID, ServerMessage notify) throws IOException {
        String msg = notify.getMessage();
        ArrayList<Session> current = connection.get(gameID);
        for (Session session : current) {
            session.getRemote().sendString(new Gson().toJson(notify));
        }
    }
}
