package com.Jeka8833.EasyPacketTCP;

import com.Jeka8833.EasyPacketTCP.client.Client;
import com.Jeka8833.EasyPacketTCP.server.ServerUser;

import java.io.IOException;

public interface Packet {
    void write(final PacketOutputStream outputStream) throws IOException;

    void read(final PacketInputStream inputStream) throws IOException;

    void processByServer(final ServerUser sender);

    void processByClient(final Client sender);

    BiMap<Short, Class<? extends Packet>> packets = new BiMap<>();

    static void addPacket(final short signature, final Class<? extends Packet> aClass) {
        if (packets.containsKey(signature))
            throw new RuntimeException("Signature already created");
        packets.put(signature, aClass);
    }
}