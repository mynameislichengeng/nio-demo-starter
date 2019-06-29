package com.lc.demo.bio;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class a {

    public static void main(String[] args) {
        Set<String> strings = new HashSet<>();
        strings.add("10");
        strings.add("12");

        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}
