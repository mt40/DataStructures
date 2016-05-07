package com.company;

import java.io.PrintStream;

public class Main {

    static PrintStream out = System.out;

    public static void main(String[] args) {
        MTBigInt a = MTBigInt.valueOf(520);
        MTBigInt b = MTBigInt.valueOf(42);
        MTBigInt c = a.add(b);
        MTBigInt d = a.multiply(b);
        MTBigInt e = a.subtract(b);
        MTBigInt f = a.divide(b);

        out.println(a);
        out.println(b);
        out.println(c);
        out.println(d);
        out.println(e);
        out.println(f);
    }
}
