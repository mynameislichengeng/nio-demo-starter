package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Listener extends Thread {


    private ServerApplication serverApplication;

    private Selector selector;
    private int readNum = 0;
    private Reader[] readers;
    private int robin;

    public Listener(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port), 150);

            this.selector = Selector.open();
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            this.readNum = 10;
            this.readers = new Reader[this.readNum];

            for (int i = 0; i < readNum; i++) {
                this.readers[i] = new Reader(i);
                this.readers[i].setServerApplication(this.serverApplication);
                this.readers[i].start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {
            try {
                this.selector.select();
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            doAccept(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel;
        while ((socketChannel = serverSocketChannel.accept()) != null) {
            try {
                socketChannel.configureBlocking(false);
                socketChannel.socket().setTcpNoDelay(true);
                socketChannel.socket().setKeepAlive(true);
            } catch (IOException e) {
                socketChannel.close();
                throw e;
            }
            Reader reader = getReader();
            try {
                reader.startAdd();
                SelectionKey readKey = reader.registerChannel(socketChannel);
                Connection c = new Connection(socketChannel);
                c.setServerApplication(this.serverApplication);
                readKey.attach(c);
            } catch (Exception e) {

            } finally {
                reader.finishAdd();
            }

        }

    }

    public Reader getReader() {
        if (robin == Integer.MAX_VALUE) {
            robin = 0;

        }
        int index = (robin++) % readNum;
        return readers[index];
    }
}
