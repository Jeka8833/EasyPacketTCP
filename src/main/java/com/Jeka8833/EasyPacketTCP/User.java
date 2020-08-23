package com.Jeka8833.EasyPacketTCP;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface User extends Closeable {

    Socket getSocket();

    PacketInputStream getInputStream();

    PacketOutputStream getOutputStream();

    void sendObject(final Serializable object) throws IOException;

    void sendPacket(final Packet packet) throws IOException;
}