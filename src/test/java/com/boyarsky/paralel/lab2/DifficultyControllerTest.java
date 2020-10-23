package com.boyarsky.paralel.lab2;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyControllerTest {
    @Test
    void testNonce() throws NoSuchAlgorithmException {
        byte[] nonce = DifficultyController.parseHexString("00a00f245c");
        byte[] data = ("HelloWorld").getBytes();
        byte[] target = DifficultyController.parseHexString("000000f000000000000000000000000000000000000000000000000000000000");
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(data);
        byte[] result = instance.digest(nonce);
        BigInteger bigInteger = new BigInteger(1, result);
        assertTrue(bigInteger.compareTo(new BigInteger(1, target)) < 0);
    }
}