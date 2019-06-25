package com.lc.demo.sample.simnio.better;

import java.nio.ByteBuffer;

public class Handler extends Thread {

    private ServerApplication serverApplication;

    public Handler(int p) {

        setName("Handle-" + p);
        System.out.println("starting Handler-" + p + "...");
    }

    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    @Override
    public void run() {
        while (this.serverApplication.running) {

            try {
                Call call = this.serverApplication.queue.take();
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


        int dataLength = 2 * 1024 * 1024;
        ByteBuffer buffer = ByteBuffer.allocate(4 + dataLength);
        buffer.putInt(dataLength);
        this.serverApplication.writeDataForTest(buffer);
        buffer.flip();

        call.response = buffer;
        this.serverApplication.responder.doResponse(call);

    }

}
