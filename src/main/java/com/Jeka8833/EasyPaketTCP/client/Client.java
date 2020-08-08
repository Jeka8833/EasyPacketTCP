package com.Jeka8833.EasyPaketTCP.client;

import com.Jeka8833.EasyPaketTCP.Packet;
import com.Jeka8833.EasyPaketTCP.PacketInputStream;
import com.Jeka8833.EasyPaketTCP.PacketOutputStream;
import com.Jeka8833.EasyPaketTCP.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client extends Thread implements User {

    private static final Logger log = LogManager.getLogger(Client.class);

    private final Socket socket;
    private final PacketInputStream inputStream;
    private final PacketOutputStream outputStream;

    public Client(final int port) throws IOException {
        this("localhost", port);
    }

    public Client(final String ip, final int port) throws IOException {
        socket = new Socket(ip, port);
        inputStream = new PacketInputStream(socket.getInputStream());
        outputStream = new PacketOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {

            }
        } catch (Exception ignored) {

        } finally {
            close();
        }
    }

    @Override
    public void close() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException ex) {
            log.warn("Fail disconnect client", ex);
        }
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public PacketInputStream getInputStream() {
        return inputStream;
    }

    @Override
    public PacketOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void sendObject(Serializable object) {

    }

    @Override
    public void sendPacket(Packet packet) {

    }
}
