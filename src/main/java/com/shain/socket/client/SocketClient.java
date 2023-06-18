package com.shain.socket.client;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SocketClient implements Runnable {
    // todo: refactor & add sending length first
    public static void main(String[] args) {
        //runSingle();
        runConcurrent(10);
    }

    private static void runConcurrent(int numberOfUsers) {
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfUsers);
        for (int i = 0; i < numberOfUsers; i++) {
            threadPool.execute(new SocketClient());
        }

        try {
            // 停止接受新任务
            threadPool.shutdown();

            // block直到所有当前任务完成， 或者超过timeout
            // 当所有任务都在时间内完成则true， 如果超时则false
            // 注意， 该方法必须在 shutdown() 方法之后运行， 否则总会返回true
            if (!threadPool.awaitTermination(1, TimeUnit.MINUTES)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runSingle() {
        Thread single = new Thread(new SocketClient());
        single.start();
    }

    @Override
    public void run() {
        try (
                // create the socket
                var socket = new Socket(InetAddress.getLocalHost(), 8080);
                // create output stream.
                var outputStream = socket.getOutputStream();
                var inputStream = socket.getInputStream()
        ) {

            outputStream.write("hi".getBytes(StandardCharsets.UTF_8));
            socket.shutdownOutput();

            var readLen = 0;
            byte[] buf = new byte[1024];
            while ((readLen = inputStream.read(buf))  != -1) {
                System.out.println(new String(buf, 0,readLen, StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
