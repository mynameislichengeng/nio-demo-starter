package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Connection {

    public SocketChannel channel;
    private ByteBuffer dataBufferLength;
    private ByteBuffer dataBuffer;
    private boolean skipHeader;
    private ServerApplication serverApplication;

    public Connection(SocketChannel socketChannel) {
        this.channel = socketChannel;
        log("Connection() this.socketchannel=" + socketChannel.toString());
        this.dataBufferLength = ByteBuffer.allocate(4);
    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    public int readAndProcess() throws IOException {
        log("readAndProcess()");
        int count;
        if (!this.skipHeader) {
            count = ServerApplication.channleRead(this.channel, this.dataBufferLength);
            if (count < 0 || this.dataBufferLength.remaining() > 0) {
                return count;
            }
        }
        this.skipHeader = true;
        if (this.dataBuffer == null) {
            dataBufferLength.flip();
            int dataLength = dataBufferLength.getInt();
            this.dataBuffer = ByteBuffer.allocate(dataLength);
        }
        count = ServerApplication.channleRead(this.channel, this.dataBuffer);
        if (count >= 0 && this.dataBuffer.remaining() == 0) {
            process();
        }
        return count;
    }

    public void process() {
        this.dataBuffer.flip();
        byte[] data = this.dataBuffer.array();
        Call call = new Call(this, data, this.serverApplication.responder);
        try {
            this.serverApplication.queue.put(call);
        } catch (Exception e) {

        }
    }

    public void close() {
        if (this.channel != null) {
            try {
                this.channel.close();
            } catch (Exception e) {

            }
        }
    }

    private void log(String str) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, Thread.currentThread() + " " + str);
    }


}
