package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPacketTCP.Packet;
import com.Jeka8833.EasyPacketTCP.PacketInputStream;
import com.Jeka8833.EasyPacketTCP.PacketOutputStream;
import com.Jeka8833.EasyPacketTCP.client.Client;
import com.Jeka8833.EasyPacketTCP.server.ServerUser;

import java.io.IOException;

public class PINGServerPacket implements Packet {

    private long time;

    public PINGServerPacket() {
        this(System.currentTimeMillis());
    }

    public PINGServerPacket(final long time) {
        this.time = time;
    }

    @Override
    public void write(PacketOutputStream outputStream) throws IOException {
        outputStream.writeLong(time);
    }

    @Override
    public void read(PacketInputStream inputStream) throws IOException {
        time = inputStream.readLong();
    }

    @Override
    public void processByServer(ServerUser sender) {
        System.out.println("Ping to client: " + (System.currentTimeMillis() - time));
    }

    @Override
    public void processByClient(Client sender) {
        try {
            sender.sendPacket(new PINGServerPacket(time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
