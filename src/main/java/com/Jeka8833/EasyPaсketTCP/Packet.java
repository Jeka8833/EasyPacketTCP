package com.Jeka8833.EasyPaсketTCP;

import com.Jeka8833.EasyPaсketTCP.client.Client;
import com.Jeka8833.EasyPaсketTCP.server.ServerUser;

public interface Packet {
    void write(final PacketOutputStream outputStream);

    void read(final PacketInputStream inputStream);

    void processByServer(final ServerUser sender);

    void processByClient(final Client sender);
}