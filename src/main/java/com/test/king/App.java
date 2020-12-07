package com.test.king;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateNewToken());
            System.out.println(generateNewToken2());
        }

        System.out.println();
        System.out.println();
        System.out.println();
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE+1;
        System.out.println("X = " + x);
        System.out.println("Y = " + y);
        System.out.println("(int) X = " + Integer.toUnsignedString(x));
        System.out.println("(int) Y = " + Integer.toUnsignedString(y));
        System.out.println("(long) X = " + Integer.toUnsignedLong(x));
        System.out.println("(long) Y = " + Integer.toUnsignedLong(y));

        System.out.println("MAX = " + Integer.parseUnsignedInt( "2147483648"));


        System.out.printf("%d %d%n", x, y);
        System.out.printf("x compared to y: %d%n", Integer.compare(x, y));
        System.out.printf("x compared to y: %d%n", Integer.compareUnsigned(x, y));
        System.out.printf("y divided by x: %d%n", y/x);
        System.out.printf("y divided by x: %d%n", Integer.divideUnsigned(y, x));
        System.out.printf("x+y: %s%n", Integer.toString(x+y));
        System.out.printf("x+y: %s%n", Integer.toUnsignedString(x+y));
        System.out.printf("parse(\"2147483647\"): %d%n", Integer.parseUnsignedInt("2147483647"));
        System.out.printf("parse(\"2147483648\"): %d%n", Integer.parseUnsignedInt("2147483648"));
        System.out.printf("parse(\"-2147483648\"): %d%n", Integer.parseUnsignedInt("-2147483648"));

    }

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String generateNewToken2() {
        return UUID.randomUUID().toString().replace("-","");
    }

}
