package com.lc.demo.sample.simnio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientDemo {

    public static void main(String[] args) {
        ClientDemo clientDemo = new ClientDemo();
        clientDemo.init();
    }

    public void init() {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            socketChannel.configureBlocking(false);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                    int length = 0;
                    try {
                        while ((length = socketChannel.read(byteBuffer)) > 0) {
                            byte[] by = new byte[1024];
                            byteBuffer.flip();
                            byteBuffer.get(by, 0, length);
                            String str = new String(by);
                            System.out.println("client 接受到的数据: " + str);
                        }
                    } catch (Exception e) {

                    }

                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = System.in;
                        byte[] by = new byte[1024];
                        int length = 0;
                        while ((length = is.read(by)) > 0) {
                            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                            byteBuffer.put(by, 0, length);
                            byteBuffer.flip();
                            socketChannel.write(byteBuffer);
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
