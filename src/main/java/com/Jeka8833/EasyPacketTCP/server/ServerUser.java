package com.Jeka8833.EasyPacketTCP.server;

import com.Jeka8833.EasyPacketTCP.Packet;
import com.Jeka8833.EasyPacketTCP.PacketInputStream;
import com.Jeka8833.EasyPacketTCP.PacketOutputStream;
import com.Jeka8833.EasyPacketTCP.User;
import com.Jeka8833.EasyPacketTCP.listener.ReceiveObjectListener;
import com.Jeka8833.EasyPacketTCP.listener.ReceivePacketListener;
import com.Jeka8833.EasyPacketTCP.listener.ServerUserDisconnectListener;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerUser extends Thread implements User {

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
                if (signature == 0) {
                    final Serializable object = (Serializable) inputStream.readObject();
                    for (ReceiveObjectListener listener : objectListeners)
                        listener.receiveObject(object);
                } else if (Packet.packets.containsKey(signature)) {
                    final Packet packet = Packet.packets.get(signature).newInstance();
                    packet.read(inputStream);
                    for (ReceivePacketListener listener : packetListeners)
                        listener.receivePacket(packet);
                    packet.processByServer(this);
                }

            }
        } catch (Exception ignored) {

        } finally {
            for (ServerUserDisconnectListener listener : server.disconnectListeners)
                listener.userDisconnect(this);
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
