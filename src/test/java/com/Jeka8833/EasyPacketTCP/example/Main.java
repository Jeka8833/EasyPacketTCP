package com.Jeka8833.EasyPacketTCP.example;

import com.Jeka8833.EasyPacketTCP.Packet;
import com.Jeka8833.EasyPacketTCP.client.Client;
import com.Jeka8833.EasyPacketTCP.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        log.info("Please choose what you want to run:");
        log.info("1. Server");
        log.info("2. Client");

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
                    log.info("Server successfully started");
                } catch (Exception ex) {
                    log.fatal("Fail run server", ex);
                }
            } else {
                try {
                    startClient(scanner);
                    log.info("Client connect");
                } catch (Exception ex) {
                    log.fatal("Fail connect to the server", ex);
                }
            }
        } catch (Exception ex) {
            log.fatal("Invalid input");
        }
    }

    private static void startServer(final Scanner scanner) throws IOException {
        final Server server = new Server(8833);
        server.start();

        server.joinListeners.add((user) -> {
            log.info("New user connect > " + user.getSocket().getInetAddress());

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    user.sendPacket(new PINGServerPacket());
                }
            }, 0, 10000);

            new Thread(() -> {
                while (true) {
                    final String text = scanner.next();
                    if (text.equalsIgnoreCase("exit"))
                        System.exit(0);
                    user.sendPacket(new SendMessagePacket(text));
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
                client.sendPacket(new PINGClientPacket());
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
