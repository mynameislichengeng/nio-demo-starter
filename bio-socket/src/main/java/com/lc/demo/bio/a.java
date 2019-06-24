package com.lc.demo.bio;

import java.io.FileInputStream;
import java.io.InputStream;

public class a {

    public static void main(String[] args) {
        InputStream fis = System.in;
        byte[] by = new byte[1024];
        try {
            int len = 0;
            while ((len = fis.read(by)) > 0) {
                System.out.println("读的长度:" + len);
                String str1  = new String(by);
                System.out.println(str1);


                String str = new String(by,0,len);
                System.out.println(str);
            }
        } catch (Exception e) {

        }

    }
}
