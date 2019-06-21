package com.lc.demo.nio.socket.block.one_way;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 1 使用NIO完成网络通信的三个核心
 * 1 channel 负责连接
 * socketchannel
 * serversocketchannel
 * datagramchannel
 * <p>
 * 2 buffer  负责数据的存取
 * <p>
 * 3 selector
 */
public class NioClientDemo {


    public static void main(String[] args) throws Exception {
        client();
    }

    private static void client() throws Exception {
        SocketAddress address = new InetSocketAddress("127.0.0.1", 9898);

        SocketChannel socketChannel = SocketChannel.open(address);

        FileChannel inChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml"), StandardOpenOption.READ);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (inChannel.read(buf) != -1) {
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
        }

//        inChannel.close();
//        socketChannel.close();
    }


}
