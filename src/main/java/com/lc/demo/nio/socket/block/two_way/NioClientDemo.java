package com.lc.demo.nio.socket.block.two_way;


import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * shutdownoutput()
 **/
public class NioClientDemo {


    public static void main(String[] args) throws Exception {
        client();
    }

    private static void client() throws Exception {
        SocketAddress address = new InetSocketAddress("127.0.0.1", 9898);

        SocketChannel socketChannel = SocketChannel.open(address);

        FileChannel inChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml"), StandardOpenOption.READ);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //发送数据给服务器
        while (inChannel.read(buf) != -1) {
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
        }
        socketChannel.shutdownOutput();

        System.out.println("--client send server over--");
        //接受服务器的数据
        ByteBuffer receiveBuf = ByteBuffer.allocate(1024);
        while (socketChannel.read(receiveBuf) != -1) {
            receiveBuf.flip();

            //
            byte[] byteStr = new byte[receiveBuf.limit()];
            receiveBuf.get(byteStr);
            System.out.println(new String(byteStr, 0, byteStr.length));

            //
            receiveBuf.clear();
        }


        inChannel.close();
        socketChannel.close();
    }


}
