package com.lc.demo.sample.simnio.better;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApplication {

    public static void main(String[] args) throws IOException {
        ServerApplication serverApplication = new ServerApplication();
        serverApplication.start();
    }

    public Responder responder;
    private int handler = 1;
    public int readNum = 1;
    public boolean running = true;
    private final static int NIO_BUFFER_LIMIT = 100;
    public BlockingQueue<Call> queue = new ArrayBlockingQueue<>(10);
    public Queue<Call> responseCalls = new LinkedList<>();

    public void start() throws IOException {
        Listener listener = new Listener(11000, this);
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
        byte[] response = "i am result".getBytes();
        for (int i = 0; i < n; i++) {
            buffer.put(response[i]);
        }
    }

    public static int channleRead(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        int g = buffer.remaining();
        if (g <= NIO_BUFFER_LIMIT) {
            return channel.read(buffer);
        } else {
            return channleIO(channel, buffer);
        }


    }

    public static int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        int g = buffer.remaining();
        if (g <= NIO_BUFFER_LIMIT) {
            return channel.write(buffer);
        } else {
            return channleIO(channel, buffer);
        }
    }

    public static int channleIO(Channel channel, ByteBuffer buffer) {
        log("channleIO()");
        return 1;
    }

    private static void log(String str) {
        Logger.getLogger(ServerApplication.class.getName()).log(Level.INFO, "thread [" + Thread.currentThread().getName() + "] " + str);
    }
}
