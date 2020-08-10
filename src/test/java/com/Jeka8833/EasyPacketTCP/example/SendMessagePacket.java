package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPaсketTCP.Packet;
import com.Jeka8833.EasyPaсketTCP.PacketInputStream;
import com.Jeka8833.EasyPaсketTCP.PacketOutputStream;
import com.Jeka8833.EasyPaсketTCP.client.Client;
import com.Jeka8833.EasyPaсketTCP.server.ServerUser;

import java.io.IOException;

public class SendMessagePacket implements Packet {

    private String text;

    public SendMessagePacket() {

    }

    public SendMessagePacket(final String text) {
        this.text = text;
    }

    @Override
    public void write(PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(text);
    }

    @Override
    public void read(PacketInputStream inputStream) throws IOException {
        text = inputStream.readUTF();
    }

    @Override
    public void processByServer(ServerUser sender) {
        System.out.println("Client send: " + text);
    }

    @Override
    public void processByClient(Client sender) {
        System.out.println("Server send: " + text);
    }
}
