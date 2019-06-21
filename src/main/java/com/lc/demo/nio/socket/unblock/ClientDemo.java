package com.lc.demo.nio.socket.unblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientDemo {

    public static void main(String args[]) throws Exception {
        client();
    }

    private static void client() throws Exception {

        //1 建立socket通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        socketChannel.configureBlocking(false);

        //2 准备数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("你好".getBytes());
        byteBuffer.flip();

        //3 写入数据
        socketChannel.write(byteBuffer);
        byteBuffer.clear();

        socketChannel.close();
    }
}
