//package com.seasugar.nio.client;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//
//public class ReadDemo {
//
//    public static void getMsg(SocketChannel clientChannel) {
//        new Thread(() -> {
//            Selector selector;
//            try {
//                selector = Selector.open();
//                clientChannel.register(selector, SelectionKey.OP_READ);
//                while (selector.select() > 0) {
//                    ByteBuffer buffer = ByteBuffer.allocate(4096);
//                    int len = clientChannel.read(buffer);
//                    if (len > 0) {
//                        buffer.flip(); // 切换为读模式
//                        String response = new String(buffer.array(), 0, len);
//                        System.out.println(response);
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//    }
//}
