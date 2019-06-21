package com.lc.demo.nio.socket.unblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerDemo {

    public static void main(String args[]) throws Exception {
        server();
    }

    private static void server() throws Exception {

        //1 建立服务socketchannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9898));
        serverSocketChannel.configureBlocking(false);

        //2 创建选择器
        Selector selector = Selector.open();

        //3 服务器通道 注册 选择器
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        //
        while (selector.select() > 0) {

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            System.out.println("selectionkey size: " + selector.selectedKeys().size());
            while (it.hasNext()) {
                System.out.println("hasnext");
                SelectionKey sk = it.next();
                if (sk.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) sk.channel();

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    int len = 0;

                    while ((len = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                }
                it.remove();
            }

        }
    }
}
