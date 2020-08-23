package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPacketTCP.Packet;
import com.Jeka8833.EasyPacketTCP.client.Client;
import com.Jeka8833.EasyPacketTCP.server.Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose what you want to run:");
        System.out.println("1. Server");
        System.out.println("2. Client");

        Packet.addPacket((short) 1, PINGServerPacket.class);
        Packet.addPacket((short) 2, PINGClientPacket.class);
        Packet.addPacket((short) 3, SendMessagePacket.class);

        try {
            final int select = scanner.nextInt();
            if (!(select == 1 || select == 2))
                throw new NullPointerException();

            if (select == 1) {
                try {
                    startServer(scanner);
                    System.out.println("Server successfully started");
                } catch (Exception ex) {
                    System.err.println("Fail run server");
                    ex.printStackTrace();
                }
            } else {
                try {
                    startClient(scanner);
                    System.out.println("Client connect");
                } catch (Exception ex) {
                    System.err.println("Fail connect to the server");
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.err.println("Invalid input");
        }
    }

    private static void startServer(final Scanner scanner) throws IOException {
        final Server server = new Server(8833);
        server.start();

        server.joinListeners.add((user) -> {
            System.out.println("New user connect > " + user.getSocket().getInetAddress());

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        user.sendPacket(new PINGServerPacket());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 10000);

            new Thread(() -> {
                while (true) {
                    final String text = scanner.next();
                    if (text.equalsIgnoreCase("exit"))
                        System.exit(0);
                    try {
                        user.sendPacket(new SendMessagePacket(text));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }

    public static void startClient(final Scanner scanner) throws IOException {
        final Client client = new Client(8833);
        client.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.sendPacket(new PINGClientPacket());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000);

        while (true) {
            final String text = scanner.next();
            if (text.equalsIgnoreCase("exit"))
                System.exit(0);
            client.sendPacket(new SendMessagePacket(text));
        }
    }
}
