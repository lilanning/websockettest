package com.roc.websockettest;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Roc on 2018/10/9.
 */
public class ServerSocket extends WebSocketServer {

    private int userNum = 0;
    private ServerManager _serverManager;

    List<Long> times1 = new ArrayList<Long>();
    List<Long> times2 = new ArrayList<Long>();
    List<Long> times3 = new ArrayList<Long>();

    List<Long> times = new ArrayList<Long>();
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    public ServerSocket(ServerManager serverManager,int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        _serverManager=serverManager;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.i("TAG","Some one Connected...");
        _serverManager.UserLogin("user"+userNum,conn);
        userNum++;
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        _serverManager.UserLeave(conn);
        userNum =0;
    }

    public void excute(String message){
        long current = System.currentTimeMillis();
        long perTime = current-_serverManager.startTime;
        Log.i("TAG","OnMessage: "+message.toString()+"perTime:"+perTime+"ms");
        if ("client1".equals(message)){
            handle1(perTime);
        }else if ("client2".equals(message)){
            handle2(perTime);
        }else if ("client3".equals(message)){
            handle3(perTime);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        executorService.submit(()->{
            excute(message);
        });


        if ("1".equals(message)) {
            _serverManager.SendMessageToAll("what？");
        }


        String[] result=message.split(":");
        if (result.length==2) {
            if (result[0].equals("user")) {
                _serverManager.UserLogin(result[1], conn);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.i("TAG","Socket Exception:"+ex.toString());
    }

    @Override
    public void onStart() {

    }
    public void handle1(long time){
        System.out.println(time);
        times1.add(time);
        System.out.println("client1数据  "+times1);
        long sum = 0;
        for (long num : times1) {
            sum += num;
        }
        double average = (double) sum / times1.size();
        System.out.println("client1当前平均值   " +average+"ms");
    }
    public void handle2(long time){
        System.out.println(time);
        times2.add(time);
        System.out.println("client2数据  "+times2);
        long sum = 0;
        for (long num : times2) {
            sum += num;
        }
        double average = (double) sum / times2.size();
        System.out.println("client2当前平均值    " +average+"ms");
    }
    public void handle3(long time){
        times3.add(time);
        System.out.println("client3数据  "+times3);
        long sum = 0;
        for (long num : times3) {
            sum += num;
        }
        double average = (double) sum / times3.size();
        System.out.println("client3当前平均值    " +average+"ms");
    }
}
