package com.roc.websockettest;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Roc on 2018/10/9.
 */
public class ServerManager {
    
    private String TAG = "ServerManager";
    private ServerSocket serverSocket=null;
    private Thread broadcastThread;
    private static boolean isRunning = true;
    private Map<WebSocket, String> userMap=new HashMap<WebSocket, String>();
    public  long startTime = System.currentTimeMillis();
    private int shouldStop = 0;
        List<Long> times1 = new ArrayList<Long>();
    List<Long> times2 = new ArrayList<Long>();
    List<Long> times3 = new ArrayList<Long>();


    public ServerManager(){

    }

    public void UserLogin(String userName, WebSocket socket){
        Log.d(TAG, "UserLogin: userName is "+userName +"socket is " +socket);
        if (userName!=null||socket!=null) {
            userMap.put(socket, userName);
            Log.i("TAG","LOGIN:"+userName);
            //SendMessageToAll(userName+"...Login...");
        }
    }


    public void UserLeave(WebSocket socket){
        Log.d(TAG, "UserLeave: "+socket);
        if (userMap.containsKey(socket)) {
            String userName=userMap.get(socket);
            Log.i("TAG","Leave:"+userName);
            userMap.remove(socket);
            //SendMessageToAll(userName+"...Leave...");
        }
    }

    public void SendMessageToUser(WebSocket socket, String message){
        Log.d(TAG, "SendMessageToUser: ");
        if (socket!=null) {
            socket.send(message);
        }
    }

    public void SendMessageToUser(String userName, String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                if (name.equals(userName)) {
                    socket.send(message);
                    break;
                }
            }
        }
    }

    public void SendMessageToAll(final String message){
        Log.d(TAG, "SendMessageToAll: "+message);
        Set<WebSocket> ketSet=userMap.keySet();
        for(final WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                socket.send(message);

//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//
//                        startTime = System.currentTimeMillis();
//                        socket.send(message);
//                        shouldStop++;
//                    }
//                };
//                final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//                final ScheduledFuture<?> handle = service.scheduleAtFixedRate(runnable,1,2, TimeUnit.SECONDS);
//                service.schedule(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        handle.cancel(true);
//                        service.shutdownNow();
//
//                    }
//                },2000,TimeUnit.SECONDS);
            }
        }
    }

    public boolean Start(int port){
        Log.d(TAG, "Start: "+port);

        if (port<0) {
            Log.i("TAG","Port error...");
            return false;
        }

        Log.i("TAG","Start ServerSocket...");

        WebSocketImpl.DEBUG=false;
        try {
            serverSocket=new ServerSocket(this,port);
            serverSocket.start();
            Log.i("TAG","Start ServerSocket Success...");
            return true;
        } catch (Exception e) {
            Log.i("TAG","Start Failed...");
            e.printStackTrace();
            return false;
        }
    }

    public boolean Stop(){
        Log.d(TAG, "Stop: ");
        try {
            serverSocket.stop();
            Log.i("TAG","Stop ServerSocket Success...");
            return true;
        } catch (Exception e) {
            Log.i("TAG","Stop ServerSocket Failed...");
            e.printStackTrace();
            return false;
        }
    }

    public void handle1(long time){
        System.out.println(time);
        times1.add(time);
        System.out.println(times1);
        long sum = 0;
        for (long num : times1) {
            sum += num;
        }
        double average = (double) sum / times1.size();
        System.out.println("client1当前平均值" +average+"ms");
    }
    public void handle2(long time){
        System.out.println(time);
        times2.add(time);
        System.out.println(times2);
        long sum = 0;
        for (long num : times2) {
            sum += num;
        }
        double average = (double) sum / times2.size();
        System.out.println("client2当前平均值" +average+"ms");
    }
    public void handle3(long time){
        System.out.println(time);
        times3.add(time);
        System.out.println(times3);
        long sum = 0;
        for (long num : times3) {
            sum += num;
        }
        double average = (double) sum / times3.size();
        System.out.println("client3当前平均值" +average+"ms");
    }


}
