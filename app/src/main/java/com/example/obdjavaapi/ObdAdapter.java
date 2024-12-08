package com.example.obdjavaapi;

import java.net.Socket;

public class ObdAdapter {
    public Socket connectToObdAdapter() {
        try {
            String ip = "192.168.2.5"; // Replace with your OBD-II adapter's IP
            int port = 35000;          // Replace with your adapter's port
            Socket socket = new Socket(ip, port);
            System.out.println("Connected to OBD-II adapter");
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
