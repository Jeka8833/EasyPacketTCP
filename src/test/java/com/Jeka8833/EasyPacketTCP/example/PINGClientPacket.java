package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPaсketTCP.Packet;
import com.Jeka8833.EasyPaсketTCP.PacketInputStream;
import com.Jeka8833.EasyPaсketTCP.PacketOutputStream;
import com.Jeka8833.EasyPaсketTCP.client.Client;
import com.Jeka8833.EasyPaсketTCP.server.ServerUser;

import java.io.IOException;

public class PINGClientPacket implements Packet {

    private long time;

    public PINGClientPacket() {
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
        sender.sendPacket(new PINGClientPacket(time));
    }

    @Override
    public void processByClient(Client sender) {
        System.out.println("Ping to server: " + (System.currentTimeMillis() - time));
    }
}
