package com.lc.demo.nio.socket.block.two_way;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioServerDemo {

    public static void main(String[] args) throws Exception {
        server();
    }

    private static void server() throws Exception {

        FileChannel outChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom_server_socket.xml"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        SocketAddress address = new InetSocketAddress(9898);
        //1 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2 绑定连接
        serverSocketChannel.bind(address);
        //3 获取客户连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();

        System.out.println("--accept--");
        //4 接受客户端的数据，并保存到本地
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            outChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        System.out.println("--server accpet client over--");
        //5 发送数据给客户端
        ByteBuffer sendByteBuffer = ByteBuffer.allocate(1024);
        sendByteBuffer.put("服务端接受数据成功".getBytes());
        sendByteBuffer.flip();
        socketChannel.write(sendByteBuffer);


        //5 关闭通道
//        outChannel.close();
//        serverSocketChannel.close();
//        socketChannel.close();
    }
}
