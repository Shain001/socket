package com.shain.socket.fakeBio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerFakeBio {
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket socket = serverSocket.accept();

            threadPool.execute(() -> {
                try {
                    var inputStream = socket.getInputStream();
                    int readLen = 0;
                    byte[] buffer = new byte[1024];

                    while ((readLen = inputStream.read(buffer)) != -1) {
                        String message = new String(buffer, 0, readLen, StandardCharsets.UTF_8);
                        System.out.println("thread: " + Thread.currentThread().getName() + "processign message: " + message);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }
}
