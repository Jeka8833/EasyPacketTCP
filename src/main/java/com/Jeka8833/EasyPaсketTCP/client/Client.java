package com.Jeka8833.EasyPaсketTCP.client;

import com.Jeka8833.EasyPaсketTCP.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread implements User {

    private static final Logger log = LogManager.getLogger(Client.class);

    private final Socket socket;
    private final PacketInputStream inputStream;
    private final PacketOutputStream outputStream;

    public final List<ReceiveObjectListener> objectListeners = new ArrayList<>();
    public final List<ReceivePacketListener> packetListeners = new ArrayList<>();

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
                        packet.processByClient(this);
                    }
                } catch (IllegalAccessException | InstantiationException | IOException ex) {
                    log.debug("Fail read packet", ex);
                    inputStream.searchEnd();
                }
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
