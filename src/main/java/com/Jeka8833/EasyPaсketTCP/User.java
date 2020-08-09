package com.Jeka8833.EasyPaсketTCP;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface User {
    void close() throws IOException;

    Socket getSocket();

    PacketInputStream getInputStream();

    PacketOutputStream getOutputStream();

    void sendObject(final Serializable object);

    void sendPacket(final Packet packet);
}