package com.Jeka8833.EasyPacketTCP.listener;

import com.Jeka8833.EasyPacketTCP.server.ServerUser;

public interface ServerUserDisconnectListener {

    void userDisconnect(final ServerUser user);

}
