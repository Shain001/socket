package com.shain.socket.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientNio {
    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // connect
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080));

            // prepare message
            byte[] data = "Hello server".getBytes();
            // 有数据的情况下直接将 data 复制到buffer
            ByteBuffer buffer = ByteBuffer.wrap(data);

            socketChannel.write(buffer);

            buffer.clear();

            // 接受响应
            int readLen = socketChannel.read(buffer);
            buffer.flip();

            if (readLen > 0) {
                byte[] received = new byte[readLen];
                buffer.get(received);
                String response = new String(received);
                System.out.println(response);
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
