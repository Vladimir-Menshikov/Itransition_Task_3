package com.company;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException{
        int len = args.length;
        int compMove;
        int plrMove;
        String key;
        byte[] hmac;

        if (len < 3 || len % 2 == 0) {
            System.out.println("Incorrect input. Number of arguments must be odd (>=3) (e.g., rock paper scissors)");
            return;
        }
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (args[i].equals(args[j])) {
                    System.out.println("Incorrect input. Arguments must be unique (e.g., rock paper scissors)");
                    return;
                }
            }
        }

        key = String.format("%032x", new BigInteger(1,new SecureRandom().generateSeed(16)));
        compMove = new Random().nextInt(len);
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(keySpec);
        hmac = mac.doFinal(args[compMove].getBytes());

        System.out.println("HMAC:");
        System.out.println(String.format("%064x", new BigInteger(1,hmac)));

        while (true) {
            System.out.println("Available moves:");
            for (int i = 0; i < len; i++) {
                System.out.println(i + 1 + " - " + args[i]);
            }
            System.out.println("0 - exit");

            try {
                plrMove = new Scanner(System.in).nextInt();
            }
            catch (Exception e) {
                System.out.println("Incorrect input. Enter number (e.g., 1)");
                continue;
            }

            if (plrMove > len || plrMove < 0) {
                System.out.println("Incorrect input. Enter number from 0 to " + len + " (e.g., 1)");
            }
            else if (plrMove == 0) {
                return;
            }
            else {
                plrMove--;
                break;
            }

        }

        System.out.println("Your move: " + args[plrMove]);
        System.out.println("Computer move: " + args[compMove]);

        int half = len / 2;

        if ((plrMove > compMove && plrMove - compMove <= half) || (compMove > plrMove && compMove - plrMove > half)) {
            System.out.println("You win!");
        }
        else if (plrMove == compMove) {
            System.out.println("Draw!");
        }
        else {
            System.out.println("You lose!");
        }

        System.out.println("HMAC Key: " + key);
    }
}