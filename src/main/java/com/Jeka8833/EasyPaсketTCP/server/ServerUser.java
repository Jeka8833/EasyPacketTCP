package com.Jeka8833.EasyPaсketTCP.server;

import com.Jeka8833.EasyPaсketTCP.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerUser extends Thread implements User {

    private static final Logger log = LogManager.getLogger(ServerUser.class);

    private final Server server;
    private final Socket socket;
    private final PacketInputStream inputStream;
    private final PacketOutputStream outputStream;

    public final List<ReceiveObjectListener> objectListeners = new ArrayList<>();
    public final List<ReceivePacketListener> packetListeners = new ArrayList<>();

    public ServerUser(Server server, final Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        inputStream = new PacketInputStream(socket.getInputStream());
        outputStream = new PacketOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                final short signature = inputStream.readShort();
                try {
                    if (signature == 0) {
                        final Serializable object = (Serializable) inputStream.readObject();
                        for (ReceiveObjectListener listener : objectListeners)
                            listener.receiveObject(object);
                    } else if (PacketSettings.packets.containsKey(signature)) {
                        final Packet packet = PacketSettings.packets.get(signature).newInstance();
                        packet.read(inputStream);
                        for (ReceivePacketListener listener : packetListeners)
                            listener.receivePacket(packet);
                        packet.processByServer(this);
                    }
                } catch (IllegalAccessException | InstantiationException | IOException ex) {
                    log.debug("Fail read packet", ex);
                    inputStream.searchEnd();
                }
            }
        } catch (Exception e) {
            log.debug("Client crash", e);
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

    @Override
    public void sendObject(Serializable object) {
        if (socket.isClosed()) return;
        if (object == null) throw new NullPointerException("Sending can not be Null");
        outputStream.sendObject(object);
    }

    @Override
    public void sendPacket(Packet packet) {
        if (socket.isClosed()) return;
        if (packet == null) throw new NullPointerException("Sending can not be Null");
        outputStream.sendPacket(packet);
    }
}
