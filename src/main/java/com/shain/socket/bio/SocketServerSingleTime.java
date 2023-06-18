package com.shain.socket.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 只接受一次消息
 */
public class SocketServerSingleTime {
    public static void main(String[] args) throws IOException {

        // Create a listener on port 9999
        var server = new ServerSocket(8080);
        System.out.println("Listening on port 8080, waiting for connection");

        // Create the socket object to receive data
        // Note that the thread will be blocked here when there is no connection.
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
            server.close();
        }

    }
}
