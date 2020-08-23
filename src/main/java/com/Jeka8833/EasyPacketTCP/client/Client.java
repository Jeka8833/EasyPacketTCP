package com.Jeka8833.EasyPacketTCP.client;

import com.Jeka8833.EasyPacketTCP.*;
import com.Jeka8833.EasyPacketTCP.listener.ClientDisconnectListener;
import com.Jeka8833.EasyPacketTCP.listener.ReceiveObjectListener;
import com.Jeka8833.EasyPacketTCP.listener.ReceivePacketListener;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread implements User {

    private final Socket socket;
    private final PacketInputStream inputStream;
    private final PacketOutputStream outputStream;

    public final List<ReceiveObjectListener> objectListeners = new ArrayList<>();
    public final List<ReceivePacketListener> packetListeners = new ArrayList<>();
    public final List<ClientDisconnectListener> disconnectListeners = new ArrayList<>();

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
                if (signature == 0) {
                    final Serializable object = (Serializable) inputStream.readObject();
                    for (ReceiveObjectListener listener : objectListeners)
                        listener.receiveObject(object);
                } else if (Packet.packets.containsKey(signature)) {
                    final Packet packet = Packet.packets.get(signature).newInstance();
                    packet.read(inputStream);
                    for (ReceivePacketListener listener : packetListeners)
                        listener.receivePacket(packet);
                    packet.processByClient(this);
                }
            }
        } catch (Exception ignored) {

        } finally {
            for (ClientDisconnectListener listener : disconnectListeners)
                listener.clientDisconnect(this);
            try {
                close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        inputStream.close();
        outputStream.close();
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
    public void sendObject(Serializable object) throws IOException {
        if (socket.isClosed()) return;
        if (object == null) throw new NullPointerException("Sending can not be Null");
        outputStream.sendObject(object);
    }

    @Override
    public void sendPacket(Packet packet) throws IOException {
        if (socket.isClosed()) return;
        if (packet == null) throw new NullPointerException("Sending can not be Null");
        outputStream.sendPacket(packet);
    }
}
