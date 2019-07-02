package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader extends Thread {
    private Selector readSelector;
    boolean adding;

    private ServerApplication serverApplication;

    public Reader(int i) throws IOException {

        setName("Reader--" + i);
        this.readSelector = Selector.open();
        log("this.readSelector = " + this.readSelector.toString());

    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {
            try {
                log("selector.select() before");
                this.readSelector.select();
                log("selector.select() after");
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
                            log("isReadable()");
                            doRead(key);
                        } else if (key.isAcceptable()) {
                            log("isAcceptable()");
                        } else if (key.isWritable()) {
                            log("isWritable()");
                        } else if (key.isConnectable()) {
                            log("isConnectable()");
                        } else {
                            log("isUnKonwn()");
                        }
                    } else {
                        log("not isValid()");
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    public void doRead(SelectionKey key) {
        log("doRead() selectionKey=" + key);
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


    public SelectionKey registerChannel(SocketChannel socketChannel) throws ClosedChannelException {

        SelectionKey keys = socketChannel.register(this.readSelector, SelectionKey.OP_READ);

        return keys;
    }

    public void startAdd() {
        this.adding = true;
        log("startadd() -- selector.wakeup()  before");
        this.readSelector.wakeup();
        log("startadd() -- selector.wakeup()  after");
    }

    public synchronized void finishAdd() {
        log("finishAdd()");
        this.adding = false;
        this.notify();
    }

    private void log(String str) {
        Logger.getLogger(this.getName()).log(Level.INFO, "thread [" + Thread.currentThread().getName() + "] " + str);
    }
}
