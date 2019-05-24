package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.systems.eim.practicaltest02.ServerThread;

public class CommunicationThread extends Thread {
    ServerThread serverThread;
    Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            String command = br.readLine();

            HashMap<String, String> data = serverThread.getData();

            String response = null;

            String[] elements = command.split(",");
            Log.v("cmd", elements[0]);

            if ("set".equals(elements[0])) {
                Log.v("set", "set");
                response = "Operation executed successfully, cmd=" + elements[0];
                response = response + "," + elements[1] + "," + elements[2] + "\n";
                String alarm = elements[1]+","+elements[2];
                String address = serverThread.serverSocket.getInetAddress().toString();
                if (data.containsKey(address)) {
                    Log.v("set", "overwrite old alarm");
                }
                data.put(address, alarm);
            } else if ("reset".equals(elements[0])) {
                response = "Operation executed successfully, cmd=" + elements[0] + "\n";
                Log.v("reset", "reset");
                String address = serverThread.serverSocket.getInetAddress().toString();
                if (data.containsKey(address)) {
                    data.remove(address);
                    Log.v("remove", "removed old alarm");
                }
            } else if ("poll".equals(elements[0])) {
                response = "Operation executed successfully, cmd=" + elements[0] + "\n";
                Log.v("poll", "poll");
            }

            pw.println(response);
            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("err", "[Comm THREAD] Error no server socket");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}