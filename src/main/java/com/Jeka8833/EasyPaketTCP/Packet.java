package com.Jeka8833.EasyPaketTCP;

import com.Jeka8833.EasyPaketTCP.client.Client;
import com.Jeka8833.EasyPaketTCP.server.ServerUser;

public interface Packet {
    void write(final PacketOutputStream outputStream);

    void read(final PacketInputStream inputStream);

    void processByServer(final ServerUser sender);

    void processByClient(final Client sender);

    BiMap<Short, Class<? extends Packet>> packetMap = new BiMap<>();

    static void addPacket(final short signature, final Class<? extends Packet> aClass) {
        if (packetMap.containsKey(signature))
            throw new RuntimeException("Signature already created");
        packetMap.put(signature, aClass);
    }
}
