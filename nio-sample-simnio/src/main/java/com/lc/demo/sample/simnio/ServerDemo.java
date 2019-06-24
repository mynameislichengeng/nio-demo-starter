package com.lc.demo.sample.simnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerDemo {

    public static void main(String[] args) {
        ServerDemo serverDemo = new ServerDemo();
        try {
            serverDemo.go();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Selector selector;

    public ServerDemo() {

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
//            ServerSocket serverSocket = serverSocketChannel.socket();
//            serverSocket.bind(new InetSocketAddress(8080));
            serverSocketChannel.bind(new InetSocketAddress(8080));
            this.selector = Selector.open();

            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go() throws IOException {

        while (selector.select() > 0) {
            System.out.println("--server go ---size:" + selector.selectedKeys().size());

            Iterator<SelectionKey> iterable = selector.selectedKeys().iterator();
            if (iterable.hasNext()) {
                SelectionKey selectionKey = iterable.next();
                iterable.remove();
                if (selectionKey.isAcceptable()) {
                    System.out.println("server isReadable()");
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();

                    SocketChannel socketChannel = channel.accept();
                    if (socketChannel == null) {
                        continue;
                    }
                    socketChannel.configureBlocking(false);
                    socketChannel.register(this.selector, SelectionKey.OP_READ );


                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                    byteBuffer.put("hi new channel".getBytes());
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);

                } else if (selectionKey.isConnectable()) {

                } else if (selectionKey.isReadable()) {
                    System.out.println("server isReadable()");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                    int length = 0;
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        byte[] bytes = new byte[1024];
                        byteBuffer.flip();
                        byteBuffer.get(bytes, 0, length);
                        String str = new String(bytes);
                        System.out.println("server 接受到的数据:" + str);
                    }


                } else if (selectionKey.isWritable()) {
                    System.out.println("server isWritable()");
//                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                }

            }

        }
    }
}
