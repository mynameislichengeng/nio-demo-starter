package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Responder extends Thread {

    private ServerApplication serverApplication;

    private Selector writeSelector;

    public Responder() throws IOException {
        this.writeSelector = Selector.open();
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {
            try {
                registerWriters();
                int n = this.writeSelector.select(1000);
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

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    public void doResponse(Call call) {
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

    public void   registerWriters() {
        Iterator<Call> iterator = this.serverApplication.responseCalls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next();
            iterator.remove();
            SelectionKey key = call.conn.channel.keyFor(this.writeSelector);
            try {
                if (key == null) {
                    try {
                        call.conn.channel.register(this.writeSelector, SelectionKey.OP_WRITE, call);
                    } catch (Exception e) {

                    }
                } else {
                    key.interestOps(SelectionKey.OP_WRITE);
                }
            } catch (CancelledKeyException e) {

            }
        }
    }

    public void registerForWrite(Call call) {
        this.serverApplication.responseCalls.add(call);
        this.writeSelector.wakeup();
    }

    private void doAsyncWrite(SelectionKey key) throws IOException {
        Call call = (Call) key.attachment();
        if (call.conn.channel != key.channel()) {
            throw new IOException("bad channel");
        }
        int numBytes = ServerApplication.channelWrite(call.conn.channel, call.response);
        if (numBytes < 0 || call.response.remaining() == 0) {
            try {
                key.interestOps(0);
            } catch (CancelledKeyException e) {

            }
        }
    }
}
