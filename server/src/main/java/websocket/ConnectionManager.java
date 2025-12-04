package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
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
                session.getRemote().sendString(msg);
            }
        }
    }

    public void broadcastOnlyCurrent(int gameID, Session userSession, ServerMessage notify) throws IOException {
        String msg = notify.getMessage();
        ArrayList<Session> current = connection.get(gameID);
        for (Session option : current) {
            if (option.equals(userSession)) {
                option.getRemote().sendString(msg);
            }
        }
    }

    //make a broadcast method to send the chess board to one person and to the whole session
    public void broadcastGameOne(int gameID, Session session, ServerMessage notify) throws IOException {
        String jsonGame = notify.getGame();
        ArrayList<Session> current = connection.get(gameID);
        for (Session option : current) {
            if (option.equals(session)) {
                option.getRemote().sendString(jsonGame);
            }
        }
    }

    public void broadcastGameExculdeCurrent(int gameID, Session excludedSession, ServerMessage notify) throws IOException {
        String jsonGame = notify.getGame();
        ArrayList<Session> current = connection.get(gameID);
        for (Session session : current) {
            if (!session.equals(excludedSession)) {
                session.getRemote().sendString(jsonGame);
            }
        }
    }

    public void broadcastGameAll(int gameID, ServerMessage notify) throws IOException {
        String jsonGame = notify.getGame();
        ArrayList<Session> current = connection.get(gameID);
        for (Session option : current) {
            option.getRemote().sendString(jsonGame);
        }
    }

    public void broadcastIncludingCurrentUser(int gameID, ServerMessage notify) throws IOException {
        String msg = notify.getMessage();
        ArrayList<Session> current = connection.get(gameID);
        for (Session session : current) {
            session.getRemote().sendString(msg);
        }
    }
}
