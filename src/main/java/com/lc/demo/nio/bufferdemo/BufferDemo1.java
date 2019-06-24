package com.lc.demo.nio.bufferdemo;

import java.nio.ByteBuffer;

public class BufferDemo1 {

    public static void main(String[] args) {


        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte a = 1;
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
        byteBuffer.put(a);
        byte b = byteBuffer.get(0);
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
        System.out.println(a);
        System.out.println(b);

        byte zz1 = 3;
        byteBuffer.put(zz1);
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());

        byte oo1 = byteBuffer.get(0);
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
        byte oo2 = byteBuffer.get(1);
        System.out.println(oo1 + "," + oo2);
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());


        byteBuffer.flip();
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
        byte oo3 = byteBuffer.get(0);
        System.out.println(oo3);
        byte oo4 = byteBuffer.get(1);
        System.out.println(oo4);

        byte zz2 = 5;
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
        //这里将index=0位置的1，覆盖了
        byteBuffer.put(zz2);
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
        byte oo5 = byteBuffer.get(0);
        System.out.println(oo5);
        byte oo6 = byteBuffer.get(1);
        System.out.println(oo6);

        byteBuffer.rewind();
        System.out.println(byteBuffer.position() + "," + byteBuffer.limit() + "," + byteBuffer.capacity());
    }
}
