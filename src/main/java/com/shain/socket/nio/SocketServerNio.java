package com.shain.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketServerNio {
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 1. 绑定端口， 设置非阻塞。 ServerSocketChannel 只负责监听， 不负责读写
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);

            while (true) {
                // 2. 通过accept方法获得socketChannel 对象， which is really used to read and write
                SocketChannel socketChannel = serverSocketChannel.accept();

                // 没有链接时socketChannel为null， here you can see the difference with serverSocket, serverScoket will block the
                // program, but here it will not.
                if (socketChannel == null) {
                    System.out.println("waiting for connection");
                    continue;
                }

                try {
                    // 3. 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readLen;

                    // 此处， 是先将socketChannel收到的数据写到buffer中， 然后将buffer切换成读模式， 进而得到数据。
                    while ((readLen = socketChannel.read(buffer)) != -1) {
                        // switch to read mode
                        buffer.flip();
                        // 读取buffer中的数据到数组
                        byte[] data = new byte[readLen];
                        buffer.get(data);

                        // get message
                        String message = new String(data);
                        System.out.println("received message from: " + socketChannel.getRemoteAddress() + "message: " + message);

                        // 4. return response to client
                        String response = "Hi Client " + socketChannel.getRemoteAddress();
                        byte[] responseData = response.getBytes();

                        // clear buffer
                        buffer.clear();
                        buffer.put(responseData);
                        // 切换为写模式
                        buffer.flip();
                        // 发送
                        socketChannel.write(buffer);
                        // 清除
                        buffer.clear();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
