package com.lc.demo.sample.simnio.better;

import javax.net.SocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientApplication {

    public static void main(String[] args) throws IOException {
        int n = 1;
        for (int i = 0; i < n; i++) {
            new Thread(new Runnable() {
                ClientApplication client = new ClientApplication();
                @Override
                public void run() {
                    try {
                        client.send(Thread.currentThread().getName() + "simviso");
//                        DataInputStream inputStream = new DataInputStream(client.in);
//                        int dataLength = inputStream.readInt();
//                        byte[] data = new byte[dataLength];
//                        inputStream.readFully(data);
//                        client.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }).start();

        }
    }

    private InputStream in;
    private OutputStream out;
    private Socket socket;

    public ClientApplication() throws IOException {
        this.socket = SocketFactory.getDefault().createSocket();
        this.socket.setTcpNoDelay(true);
        this.socket.setKeepAlive(true);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 11000);
        this.socket.connect(inetSocketAddress, 3000);
        this.out = this.socket.getOutputStream();
        this.in = this.socket.getInputStream();

    }

    public void send(String message) throws IOException {
        byte[] data = message.getBytes();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        dos.write(data);
        this.out.flush();
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
