package com.lc.demo.nio.socket.unblock.copy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerDemo {

    public static void main(String args[]) {

    }

    private static void server() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9898));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {

            Set<SelectionKey> selectionKeySet = selector.selectedKeys();

            Iterator<SelectionKey> it = selectionKeySet.iterator();
            while (it.hasNext()) {

                SelectionKey selectionKey = it.next();

                if (selectionKey.isAcceptable()) {

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    while (socketChannel.read(byteBuffer)!=-1){

                    }

                }

            }


        }
    }
}
