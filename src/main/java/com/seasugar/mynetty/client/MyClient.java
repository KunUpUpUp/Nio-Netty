package com.seasugar.mynetty.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;


public class MyClient {
    final SocketChannel clientChannel;

    final String name;

    final Selector selector;

    public MyClient(String name) {
        try {
            // 创建SocketChannel并连接服务器
            this.name = name;
            this.clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false); // 非阻塞模式
            clientChannel.connect(new InetSocketAddress("localhost", 8080));
            selector = Selector.open();
            // 等待连接完成（非阻塞模式下需循环检查）
            while (!clientChannel.finishConnect()) {
                System.out.println("等待连接...");
            }
            getMsg();
            sendMsg();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMsg() {
        // 发送消息
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String message = sc.next();
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            try {
                clientChannel.write(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getMsg() {
        new Thread(() -> {
            try {
                clientChannel.register(selector, SelectionKey.OP_READ);
                while (selector.select() > 0) {
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        // next()：游标移到下个位置
                        keys.next();
                        // 移除next上一位的元素(必须和next一起使用)
                        keys.remove();
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        int len = clientChannel.read(buffer);
                        if (len > 0) {
                            buffer.flip(); // 切换为读模式
                            String response = new String(buffer.array(), 0, len);
                            System.out.println(response);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}