package com.lc.demo.sample.simnio.better;

import java.nio.ByteBuffer;

public class Call {

    Connection conn;
    byte[] request;
    Responder responder;

    //
    ByteBuffer response;
    boolean done;

    public Call(Connection conn, byte[] request, Responder responder) {
        this.conn = conn;
        this.request = request;
        this.responder = responder;
    }
}
