package com.lc.demo.bio.socket;

import java.io.*;
import java.net.Socket;

public class SocketClient {


    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient();
        socketClient.initClient(Constants.host, Constants.port);
    }


    public void initClient(String host, int port) {
        InputStream is = null;
        BufferedWriter bw = null;
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            is = System.in;
            System.out.println("client send start");
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = is.read(b)) > 0) {
                String content = new String(b, 0, length);
                System.out.println("client send: " + content);
                bw.write(content);
                bw.flush();
            }

            System.out.println("client over");
        } catch (Exception e) {

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
