package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;


public class ServerThread extends Thread {
    int port;
    ServerSocket serverSocket;
    HashMap<String, String> data;

    public ServerThread(int port) {
        this.port = port;
        this.data = new HashMap<String, String>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("err", "[SERVER THREAD] Error no server socket");
        }
    }

    public synchronized HashMap<String, String> getData() {
        return data;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("err", "[SERVER THREAD] Error no server comm thread created");
            }
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}