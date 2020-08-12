package com.Jeka8833.EasyPacketTCP.server;

import com.Jeka8833.EasyPacketTCP.listener.ServerUserDisconnectListener;
import com.Jeka8833.EasyPacketTCP.listener.ServerUserJoinListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private static final Logger log = LogManager.getLogger(Server.class);

    public final List<ServerUser> users = new ArrayList<>();
    public final List<ServerUserJoinListener> joinListeners = new ArrayList<>();
    public final List<ServerUserDisconnectListener> disconnectListeners = new ArrayList<>();

    public final ServerSocket serverSocket;

    public Server(final int port) throws IOException {
        this("localhost", port, 16);
    }

    public Server(final String ip, final int port) throws IOException {
        this(ip, port, 16);
    }

    public Server(final String ip, final int port, final int maxUser) throws IOException {
        serverSocket = new ServerSocket(port, maxUser, InetAddress.getByName(ip));
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    final ServerUser user = new ServerUser(this, serverSocket.accept());
                    users.add(user);
                    user.start();
                    for (ServerUserJoinListener listener : joinListeners)
                        listener.userJoin(user);
                } catch (Exception ignored) {
                }
            }
        } finally {
            close();
        }
    }

    public void close() {
        try {
            for (ServerUser user : users) {
                try {
                    user.getSocket().close();
                    user.getInputStream().close();
                    user.getOutputStream().close();
                } catch (IOException ignored) {
                }
            }
            users.clear();
            serverSocket.close();
        } catch (IOException ex) {
            log.warn("Fail close this server", ex);
        }
    }
}
