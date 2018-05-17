package com.wmt.tktsvc;


public class IdGenerator {
    public static int generate(String customerEmail) {
        int uniqueId = (int) (System.nanoTime() & 0xfffffff);
        return uniqueId;
    }
}
