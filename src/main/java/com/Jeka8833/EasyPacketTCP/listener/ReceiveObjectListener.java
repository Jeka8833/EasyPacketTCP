package com.Jeka8833.EasyPacketTCP.listener;

import java.io.Serializable;

public interface ReceiveObjectListener {

    void receiveObject(final Serializable object);

}
