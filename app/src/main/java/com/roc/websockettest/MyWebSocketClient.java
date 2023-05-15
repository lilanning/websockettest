package com.roc.websockettest;

import static java.sql.DriverManager.println;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {
    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        println("WebSocket opened: " + getURI());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("WebSocket message received: "+message);
        send("客户端接收到数据了");

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
