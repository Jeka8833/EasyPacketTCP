package com.Jeka8833.EasyPacketTCP.listener;

import com.Jeka8833.EasyPacketTCP.Packet;

public interface ReceivePacketListener {

    void receivePacket(final Packet object);

}
