package com.lc.demo.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelDemo3 {

    public static void main(String args[]) throws Exception {
        allocate();
    }

    /**
     * 分散写出和聚集写入
     * @throws Exception
     */
    public static void allocate() throws Exception {

        long start = System.currentTimeMillis();
        RandomAccessFile raf1 = new RandomAccessFile("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml", "rw");



        FileChannel fisChannel = raf1.getChannel();

        System.out.println(fisChannel.size());

        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        ByteBuffer[] bfs = {buf1,buf2};
        fisChannel.read(bfs);

        for(ByteBuffer byteBuffer:bfs){
            byteBuffer.flip();
        }

        System.out.println(new String(buf1.array(), 0, buf1.limit()));
        System.out.println("--------");
        System.out.println(new String(buf2.array(), 0, buf2.limit()));


        System.out.println(System.currentTimeMillis()-start);

        RandomAccessFile raf2 = new RandomAccessFile("/Users/licheng/ws/example_mv/spring/nio/sgg/pom_3.xml", "rw");
        FileChannel fisChannel2 = raf2.getChannel();
        fisChannel2.write(bfs);

        fisChannel.close();
        fisChannel2.close();


    }
}
