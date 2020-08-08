package com.Jeka8833.EasyPaketTCP.server;

import com.Jeka8833.EasyPaketTCP.Packet;
import com.Jeka8833.EasyPaketTCP.PacketInputStream;
import com.Jeka8833.EasyPaketTCP.PacketOutputStream;
import com.Jeka8833.EasyPaketTCP.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class ServerUser extends Thread implements User {

    private static final Logger log = LogManager.getLogger(ServerUser.class);

    private final Server server;
    private final Socket socket;
    private final PacketInputStream inputStream;
    private final PacketOutputStream outputStream;

    public ServerUser(Server server, final Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        inputStream = new PacketInputStream(socket.getInputStream());
        outputStream = new PacketOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {

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
            log.warn("Fail close connection", ex);
        }
        server.users.remove(this);
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

    /**
     * Sending client any class
     *
     * @param object The class to send
     * @throws NullPointerException If the object is null
     */
    @Override
    public void sendObject(Serializable object) {
        if (socket.isClosed()) return;
        if (object == null) throw new NullPointerException("Sending can not be Null");
        try {
            outputStream.writeShort(0);
            outputStream.writeObject(object);
        } catch (IOException ex) {
            log.warn("Fail send Object", ex);
        }
    }

    /**
     * Sending client any packet
     *
     * @param packet The packet to send
     * @throws NullPointerException If packet is null
     */
    @Override
    public void sendPacket(Packet packet) {
        if (socket.isClosed()) return;
        if (packet == null) throw new NullPointerException("Sending can not be Null");
        try {
            outputStream.writeShort(Packet.packetMap.getKey(packet.getClass()));
            packet.write(outputStream);
        } catch (IOException ex) {
            log.warn("Fail send Packet", ex);
        }
    }
}
