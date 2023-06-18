package com.shain.socket.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 持续接受消息， 唯一区别在于添加了无限while， 以及移除了server.close()
 */
public class SocketServerMultipleTime {
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        var server = new ServerSocket(8080);
        System.out.println("Listening on port 8080, waiting for connection");

        while (true) {
            Socket socket = server.accept();
            System.out.println("Connection established on port: " + socket.getLocalPort());

            // read data
            try (var inputStream = socket.getInputStream();
                 var outputStream = socket.getOutputStream()) {
                byte[] buf = new byte[1024];
                var readLen = 0;
                while ((readLen = inputStream.read(buf)) != -1) {
                    System.out.println(new String(buf, 0, readLen));
                }
                outputStream.write("hi client".getBytes());
                socket.shutdownOutput();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                socket.close();
            }
        }

    }
}
