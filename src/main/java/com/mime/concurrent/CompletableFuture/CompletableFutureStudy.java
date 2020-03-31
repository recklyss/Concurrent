package com.mime.concurrent.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureStudy {

    private static CompletableFuture<String> getUserName(CompletableFuture<String> future) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return "Jia Heng";
        });
    }

    private static CompletableFuture<String> sayHello(CompletableFuture<String> userName) {
        return userName.thenApplyAsync(user -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "I am " + user + " and I will say hello to Mark";
        });
    }

    public static void main(String[] args) {
    }

}
