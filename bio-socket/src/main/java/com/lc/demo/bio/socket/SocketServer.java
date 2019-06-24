package com.lc.demo.bio.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.initSocketServer();

    }

    public void initSocketServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        ClientSocketThread clientSocketThread = null;
        try {
            serverSocket = new ServerSocket(Constants.port);
//            serverSocket.setSoTimeout(5000);
            while (true) {
                System.out.println(stringNowTime() + " server start");
                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    System.out.println("SocketTimeoutException: socket id:" + socket.hashCode());
                    continue;
                }

                System.out.println(stringNowTime() + " server accept-- socket Id(): " + socket.hashCode());
                clientSocketThread = new ClientSocketThread(socket);
                executorService.execute(clientSocketThread);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String stringNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String str = simpleDateFormat.format(new Date());
        return str;
    }

    static class ClientSocketThread implements Runnable {
        private Socket socket;

        public ClientSocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader br = null;
            try {
                InputStream is = socket.getInputStream();
//                socket.setSoTimeout(5000);
                br = new BufferedReader(new InputStreamReader(is));
                String content = null;
                while ((content = br.readLine()) != null) {
                    System.out.println(stringNowTime() + "socket id:" + socket.hashCode() + " 收到的内容: " + content);
                }
            } catch (Exception e) {

            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {


                    }
                }
            }
        }
    }
}
