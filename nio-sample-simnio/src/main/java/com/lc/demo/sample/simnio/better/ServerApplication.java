package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class ServerApplication {

    public static void main(String[] args) throws IOException {
        ServerApplication serverApplication = new ServerApplication();
        serverApplication.start();
    }

    public Responder responder;
    private int handler;
    public boolean running;
    private final static int NIO_BUFFER_LIMIT = 100;
    public BlockingQueue<Call> queue;
    public Queue<Call> responseCalls;

    public void start() throws IOException {
        Listener listener = new Listener(10000);
        listener.setServerApplication(this);
        listener.start();
        this.responder = new Responder();
        this.responder.setServerApplication(this);
        this.responder.start();
        startHandler();
    }

    public void startHandler() {
        for (int i = 0; i < this.handler; i++) {
            Handler handler = new Handler(i);
            handler.setServerApplication(this);
            handler.start();
        }
    }

    public void writeDataForTest(ByteBuffer buffer) {
        int n = buffer.limit() - 4;
        for (int i = 0; i < n; i++) {
            buffer.put((byte) 0);
        }
    }

    public static int channleRead(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.read(buffer) : channleIO(channel, buffer);
    }

    public static int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.write(buffer) : channleIO(channel, buffer);
    }

    public static int channleIO(Channel channel, ByteBuffer buffer) {
        return 1;
    }
}
