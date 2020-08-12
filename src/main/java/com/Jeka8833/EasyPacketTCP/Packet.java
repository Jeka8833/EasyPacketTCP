package com.Jeka8833.EasyPacketTCP;

import com.Jeka8833.EasyPacketTCP.client.Client;
import com.Jeka8833.EasyPacketTCP.server.ServerUser;

import java.io.IOException;

public interface Packet {
    void write(final PacketOutputStream outputStream) throws IOException;

    void read(final PacketInputStream inputStream) throws IOException;

    void processByServer(final ServerUser sender);

    void processByClient(final Client sender);
}