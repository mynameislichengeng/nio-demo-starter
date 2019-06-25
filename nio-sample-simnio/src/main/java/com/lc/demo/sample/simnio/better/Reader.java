package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Reader extends Thread {
    private Selector readSelector;
    boolean adding;

    private ServerApplication serverApplication;

    public Reader(int i) throws IOException {

            setName("Reader--" + i);
            this.readSelector = Selector.open();
            System.out.println("starting Reader-" + i + "...");

    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {
            try {
                this.readSelector.select();
                while (this.adding) {
                    synchronized (this) {
                        this.wait(1000);
                    }
                }
                Iterator<SelectionKey> iterator = this.readSelector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid()) {
                        if (key.isReadable()) {
                            doRead(key);
                        }
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    public void doRead(SelectionKey key) {
        Connection c = (Connection) key.attachment();
        if (c == null) {
            return;
        }
        int n;
        try {
            n = c.readAndProcess();
        } catch (Exception e) {
            n = -1;
        }
        if (n == -1) {
            c.close();
        }
    }


    public SelectionKey  registerChannel(SocketChannel socketChannel) throws ClosedChannelException {
        SelectionKey keys = null;
        keys = socketChannel.register(this.readSelector, SelectionKey.OP_READ);

        return keys;
    }

    public void startAdd() {
        this.adding = true;
        this.readSelector.wakeup();
    }

    public synchronized void finishAdd() {
        this.adding = false;
        this.notify();
    }
}
