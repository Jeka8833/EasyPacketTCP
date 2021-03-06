package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPacketTCP.Packet;
import com.Jeka8833.EasyPacketTCP.PacketInputStream;
import com.Jeka8833.EasyPacketTCP.PacketOutputStream;
import com.Jeka8833.EasyPacketTCP.client.Client;
import com.Jeka8833.EasyPacketTCP.server.ServerUser;

import java.io.IOException;

public class PINGClientPacket implements Packet {

    private long time;

    public PINGClientPacket() {
        this(System.currentTimeMillis());
    }

    public PINGClientPacket(final long time) {
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
        try {
            sender.sendPacket(new PINGClientPacket(time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processByClient(Client sender) {
        System.out.println("Ping to server: " + (System.currentTimeMillis() - time));
    }
}
