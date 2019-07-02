package com.lc.demo.sample.simnio.better;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends Thread {

    private ServerApplication serverApplication;

    public Handler(int p) {
        setName("Handle-" + p);
    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {

            try {
                log("blockqueue.take() before");
                Call call = this.serverApplication.queue.take();
                log("blockqueue.take() after");
                process(call);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void process(Call call) {
        byte[] request = call.request;
        String message = new String(request);
        System.out.println("received message: " + message);


        int dataLength = "i am result".getBytes().length;
        ByteBuffer buffer = ByteBuffer.allocate(4 + dataLength);
        buffer.putInt(dataLength);
        this.serverApplication.writeDataForTest(buffer);
        buffer.flip();

        call.response = buffer;
        this.serverApplication.responder.doResponse(call);

    }

    private static void log(String str) {
        Logger.getLogger(Handler.class.getName()).log(Level.INFO, "thread [" + Thread.currentThread() + "] " + str);
    }
}
