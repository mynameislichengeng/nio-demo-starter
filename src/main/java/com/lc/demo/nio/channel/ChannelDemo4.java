package com.lc.demo.nio.channel;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.Set;

/**
 * 字符集
 */
public class ChannelDemo4 {

    public static void main(String[] args) throws Exception {
        encoder();
    }

    /**
     * 打印总共有多少的字符集
     */
    private static void charset() {
        Map<String, Charset> map = Charset.availableCharsets();

        Set<Map.Entry<String, Charset>> set = map.entrySet();

        for (Map.Entry<String, Charset> entry : set) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        System.out.println(set.size());
    }

    private static void encoder() throws Exception {
        Charset cs1 = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce = cs1.newEncoder();
        //获取解码器
        CharsetDecoder cd = cs1.newDecoder();

        CharBuffer cBuf = CharBuffer.allocate(1024);
        cBuf.put("尚硅谷");
        cBuf.flip();

        //编码
        ByteBuffer bBuf = ce.encode(cBuf);
        System.out.println(bBuf.limit());
        for (int i = 0; i < bBuf.limit(); i++) {
            System.out.println(bBuf.get());
        }

        //解码
        bBuf.rewind();
        CharBuffer deCharBuf = cd.decode(bBuf);
        System.out.println(deCharBuf.toString());
    }
}
