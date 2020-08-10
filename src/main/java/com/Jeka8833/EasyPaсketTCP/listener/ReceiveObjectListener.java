package com.Jeka8833.EasyPaсketTCP.listener;

import java.io.Serializable;

public interface ReceiveObjectListener {

    void receiveObject(final Serializable object);

}
