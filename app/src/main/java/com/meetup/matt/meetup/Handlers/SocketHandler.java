package com.meetup.matt.meetup.Handlers;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.meetup.matt.meetup.config.Config;

import java.net.URISyntaxException;

public class SocketHandler {
    private static Socket socket;

    public class Event {
        public class Server {
            public static final String ON_USER_JOIN = "server_userjoin";
            public static final String DELETE_SESSION = "server_deletesession";
            public static final String DELETE_USER_FROM_SESSION = "server_deleteuser";
            public static final String ON_HOST_START = "server_startmaps";
            public static final String ON_DESTINATION_UPDATE = "server_updatedest";
            public static final String ON_USER_LOCATION_CHANGE = "server_updatelocation";
        }

        public class Client {
            public static final String ON_USER_JOIN = "client_userjoin";
            public static final String ON_USER_DISCONNECT = "client_userdisconnect";
            public static final String ON_HOST_START = "client_startmaps";
            public static final String ON_DESTINATION_UPDATE = "client_updatedest";
            public static final String ON_USER_LOCATION_CHANGE = "client_updatelocation";
        }
    }


    static {
        try {
            socket = IO.socket(Config.ENDPOINT_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket createSocketConnection() {
        socket.connect();
        return socket;
    }

    public static void destroySocketConnection() {
        socket.disconnect();
    }

    public static Socket getSocket() {
        return socket;
    }

}
