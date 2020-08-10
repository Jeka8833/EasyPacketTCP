package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPaсketTCP.client.Client;
import com.Jeka8833.EasyPaсketTCP.server.Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

    }

    private static void startServer(final Scanner scanner) throws IOException {
        final Server server = new Server(8833);
        server.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                server.users.get(0).sendPacket(new PINGServerPacket());
            }
        }, 0, 1000);

        while (true) {
            final String text = scanner.next();
            if (text.equalsIgnoreCase("exit"))
                System.exit(0);
            server.users.get(0).sendPacket(new SendMessagePacket(text));
        }
    }

    public static void startClient(final Scanner scanner) throws IOException {
        final Client client = new Client(8833);
        client.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                client.sendPacket(new PINGClientPacket());
            }
        }, 0, 1000);

        while (true) {
            final String text = scanner.next();
            if (text.equalsIgnoreCase("exit"))
                System.exit(0);
            client.sendPacket(new SendMessagePacket(text));
        }
    }
}
