package com.Jeka8833.EasyPaсketTCP;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketInputStream extends DataInputStream {

    public PacketInputStream(final InputStream in) {
        super(in);
    }

    public byte[] readByteArray() throws IOException {
        final byte[] array = new byte[readInt()];
        readFully(array);
        return array;
    }

    public short[] readShortArray() throws IOException {
        final int size = readInt();
        final short[] array = new short[size];
        for (int i = 0; i < size; i++)
            array[i] = readShort();
        return array;
    }

    public char[] readCharArray() throws IOException {
        final int size = readInt();
        final char[] array = new char[size];
        for (int i = 0; i < size; i++)
            array[i] = readChar();
        return array;
    }

    public int[] readIntArray() throws IOException {
        final int size = readInt();
        final int[] array = new int[size];
        for (int i = 0; i < size; i++)
            array[i] = readInt();
        return array;
    }

    public float[] readFloatArray() throws IOException {
        final int size = readInt();
        final float[] array = new float[size];
        for (int i = 0; i < size; i++)
            array[i] = readFloat();
        return array;
    }

    public double[] readDoubleArray() throws IOException {
        final int size = readInt();
        final double[] array = new double[size];
        for (int i = 0; i < size; i++)
            array[i] = readDouble();
        return array;
    }

    public long[] readLongArray() throws IOException {
        final int size = readInt();
        final long[] array = new long[size];
        for (int i = 0; i < size; i++)
            array[i] = readLong();
        return array;
    }

    public boolean[] readBooleanArray() throws IOException {
        final int size = readInt();
        final boolean[] array = new boolean[size];
        for (int i = 0; i < size; i++)
            array[i] = readBoolean();
        return array;
    }

    public String[] readStringArray() throws IOException {
        final int size = readInt();
        final String[] array = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = readUTF();
        return array;
    }

    public List<String> readStringList() throws IOException {
        final int size = readInt();
        final List<String> array = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            array.add(readUTF());
        return array;
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        try (final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(readByteArray()))) {
            return inputStream.readObject();
        }
    }

    public void searchEnd() throws IOException {
        if (PacketSettings.stopBytes.length == 0) throw new NullPointerException();
        if (PacketSettings.stopBytes.length == 1)
            while (readByte() != PacketSettings.stopBytes[0]) {
            }

        final byte[] tempBytes = new byte[PacketSettings.stopBytes.length];
        do {
            System.arraycopy(tempBytes, 1, tempBytes, 0, tempBytes.length - 1);
            tempBytes[PacketSettings.stopBytes.length - 1] = readByte();
        } while (!Arrays.equals(PacketSettings.stopBytes, tempBytes));
    }
}
