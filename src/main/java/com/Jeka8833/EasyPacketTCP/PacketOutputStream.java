package com.Jeka8833.EasyPacketTCP;

import java.io.*;
import java.util.List;

public class PacketOutputStream extends DataOutputStream {

    public PacketOutputStream(final OutputStream out) {
        super(out);
    }

    public void writeByteArray(final byte[] array) throws IOException {
        writeInt(array.length);
        write(array);
    }

    public void writeShortArray(final short[] array) throws IOException {
        writeInt(array.length);
        for (short value : array) writeShort(value);
    }

    public void writeCharArray(final char[] array) throws IOException {
        writeInt(array.length);
        for (char value : array) writeChar(value);
    }

    public void writeIntArray(final int[] array) throws IOException {
        writeInt(array.length);
        for (int value : array) writeInt(value);
    }

    public void writeFloatArray(final float[] array) throws IOException {
        writeInt(array.length);
        for (float value : array) writeFloat(value);
    }

    public void writeDoubleArray(final double[] array) throws IOException {
        writeInt(array.length);
        for (double value : array) writeDouble(value);
    }

    public void writeLongArray(final long[] array) throws IOException {
        writeInt(array.length);
        for (long value : array) writeLong(value);
    }

    public void writeBooleanArray(final boolean[] array) throws IOException {
        writeInt(array.length);
        for (boolean value : array) writeBoolean(value);
    }

    public void writeStringArray(final String[] array) throws IOException {
        writeInt(array.length);
        for (String value : array) writeUTF(value);
    }

    public void writeStringList(final List<String> array) throws IOException {
        writeInt(array.size());
        for (String value : array) writeUTF(value);
    }

    public void writeObject(final Serializable object) throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            writeByteArray(outputStream.toByteArray());
        }
    }

    public void sendObject(Serializable object) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PacketOutputStream temp = new PacketOutputStream(byteArrayOutputStream)) {

            temp.writeShort(0);
            temp.writeObject(object);

            write(byteArrayOutputStream.toByteArray());
        }
    }

    public void sendPacket(Packet packet) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PacketOutputStream temp = new PacketOutputStream(byteArrayOutputStream)) {

            temp.writeShort(Packet.packets.getKey(packet.getClass()));
            packet.write(temp);

            write(byteArrayOutputStream.toByteArray());
        }
    }
}
