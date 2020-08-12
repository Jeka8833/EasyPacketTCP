package com.Jeka8833.EasyPacketTCP;

public class PacketSettings {

    public static final BiMap<Short, Class<? extends Packet>> packets = new BiMap<>();
    public static byte[] stopBytes = {(byte) 0xAF, (byte) 0x00, (byte) 0xAF};

    public static void addPacket(final short signature, final Class<? extends Packet> aClass) {
        if (packets.containsKey(signature))
            throw new RuntimeException("Signature already created");
        packets.put(signature, aClass);
    }
}
