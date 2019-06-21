package com.lc.demo.nio.channel;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * 通道之间的数据传输
 */
public class ChannelDemo2 {

    public static void main(String args[]) throws Exception {
        transferFrom();
    }

    private static void transferTo() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom_transfer_to_1.xml"),StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        //
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inChannel.close();
        outChannel.close();

    }

    private static void transferFrom() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom.xml"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/licheng/ws/example_mv/spring/nio/sgg/pom_transfer_from_2.xml"),StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        outChannel.transferFrom(inChannel, 0, inChannel.size());
        inChannel.close();
        outChannel.close();

    }

}
