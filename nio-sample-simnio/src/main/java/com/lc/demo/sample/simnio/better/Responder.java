package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Responder extends Thread {

    private ServerApplication serverApplication;

    private Selector writeSelector;

    public Responder() throws IOException {
        setName("Responder");
        this.writeSelector = Selector.open();
        log("Responder() writeSelector = " + writeSelector.toString());
    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {
            try {
                registerWriters();
//                log("this.writeSelector.select() before");
                int n = this.writeSelector.select(1000);
//                log("this.writeSelector.select() after");
                if (n == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = this.writeSelector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid() && key.isWritable()) {
                        doAsyncWrite(key);
                    }
                }
            } catch (Exception e) {

            }
        }
    }


    public void registerWriters() {
//        log("registerWriters()");
        Iterator<Call> iterator = this.serverApplication.responseCalls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next();
            iterator.remove();
            SelectionKey key = call.conn.channel.keyFor(this.writeSelector);

            try {
                if (key == null) {
                    try {
                        SelectionKey key1 = call.conn.channel.register(this.writeSelector, SelectionKey.OP_WRITE, call);
                        log("registerWriters key==null selectionKey = " + key1.toString());

                    } catch (Exception e) {

                    }
                } else {
                    log("registerWriters key!=null before selectionKey = " + key.toString());
                    key.interestOps(SelectionKey.OP_WRITE);
                    log("registerWriters key!=null after selectionKey = " + key.toString());
                }
            } catch (CancelledKeyException e) {

            }
        }
    }

    private void doAsyncWrite(SelectionKey key) throws IOException {
        log("doAsyncWrite() SelectionKey=" + key.toString());
        Call call = (Call) key.attachment();
        if (call.conn.channel != key.channel()) {
            throw new IOException("bad channel");
        }
        int numBytes = ServerApplication.channelWrite(call.conn.channel, call.response);
        if (numBytes < 0 || call.response.remaining() == 0) {
            try {
                key.interestOps(0);
                log("doAsyncWrite()  key.interestOps(0) SelectionKey=" + key.toString());
            } catch (CancelledKeyException e) {

            }
        }
    }


    public void doResponse(Call call) {
        log("doResponse()");
        if (!processResponse(call)) {
            registerForWrite(call);
        }
    }

    private boolean processResponse(Call call) {
        boolean error = true;
        try {
            int numBytes = ServerApplication.channelWrite(call.conn.channel, call.response);
            if (numBytes < 0) {
                throw new IOException("error socket write");
            }
            error = false;
        } catch (Exception e) {

        } finally {
            if (error) {
                call.conn.close();
            }
        }
        if (call.response.hasRemaining()) {
            call.done = true;
            return true;
        }
        return false;
    }


    public void registerForWrite(Call call) {
        log("registerForWrite()");
        this.serverApplication.responseCalls.add(call);
        this.writeSelector.wakeup();
    }


    private void log(String str) {
        Logger.getLogger(this.getName()).log(Level.INFO,
                "thread [" + Thread.currentThread().getName() + "] " + str);
    }
}
