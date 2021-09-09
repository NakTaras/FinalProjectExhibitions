package com.my.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryption {
    public static String encrypt(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return null;
        }

        byte[] digest;
        messageDigest.update(password.getBytes());
        digest = messageDigest.digest();

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder output = new StringBuilder(bigInt.toString(16));

        while( output.length() < 32 ){
            output.reverse().append("0").reverse();
        }
        return output.toString().toUpperCase();
    }
}
