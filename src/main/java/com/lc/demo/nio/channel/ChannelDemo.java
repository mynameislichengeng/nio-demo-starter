package com.lc.demo.nio.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ChannelDemo {

    public static void main(String args[]) throws Exception {

        allocate();
    }

    /**
     * 间接缓冲区
     * @throws Exception
     */
    public static void allocate() throws Exception {

        long start = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml");
        FileOutputStream fos = new FileOutputStream("/Users/licheng/ws/example_mv/spring/nio/sgg/pom_1.xml");

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while ((fisChannel.read(buf) != -1)) {
            buf.flip();
            fosChannel.write(buf);
            buf.clear();
        }
        if (fisChannel != null) {
            fisChannel.close();
        }
        if (fosChannel != null) {
            fosChannel.close();
        }
        if (fis != null) {
            fis.close();
        }
        if (fos != null) {
            fos.close();
        }

        System.out.println(System.currentTimeMillis()-start);
    }

    /**
     * 直接缓冲区
     * @throws Exception
     */
    public static void allocateDir() throws Exception {

        long start = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom_2.xml"),StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);


        //内存映射文件
        MappedByteBuffer iMapBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer oMapBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对缓冲区进行数据的读写操作
        byte[] dst = new byte[iMapBuffer.limit()];
        iMapBuffer.get(dst);
        oMapBuffer.put(dst);
        System.out.println(System.currentTimeMillis()-start);
        inChannel.close();
        outChannel.close();
    }
}
