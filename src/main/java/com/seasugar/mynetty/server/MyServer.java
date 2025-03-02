package com.seasugar.mynetty.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class MyServer {
    final Selector selector;
    final ServerSocketChannel serverChannel;

    public MyServer() throws IOException {
        // 创建Selector和ServerSocketChannel
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));
        // 非阻塞模式 Block——阻塞
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("NIO服务端启动，监听端口8080...");
        handleMsg();
    }

    private void handleMsg() throws IOException {
        while (selector.select() > 0) {
            // 阻塞等待事件就绪 客户端只要有操作这里就会触发
            // select()会遍历已经准备好的事件，如果没有就继续阻塞，如果有就拿出来
            // selector.selectedKeys()就是已经准备好的事件，下面需要判断类型来对不同的事件进行处理
            // 每个channel都会注册到selector中并说明自己关注的是什么类型的事件，只有channel关注的事件完成了才会被selector捕获到
            // 举个例子，serverChannel.register(selector, SelectionKey.OP_ACCEPT);注册的是accept事件，那么这个channel中的其他事件它是不关心的，也就不会解除selector的阻塞
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();

                 /* 如果不移除会怎么样
                 selectedKeys会一直add，但是不会remove
                 在上一个accept的时候，server.accept()已经把客户端的相关信息取走（native方法）
                 下次客户端发信息的时候，由于selectedKeys没有remove server的accept，再次server.accept()时返回null
                 之后clientChannel.configureBlocking(false);会NLP */
                keys.remove(); // 移除即将处理的事件

                // 有时候学习也不用太较真，比如int i = 5;底层是什么，这个对初学某种知识意义不大
                if (key.isAcceptable()) { // 处理新连接
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    // 注册READ事件，即服务端的selector在客户端该chennel数据可读时触发
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接：" + clientChannel.getRemoteAddress());

                    ByteBuffer welcomeMsg = StandardCharsets.UTF_8.encode("您已进入聊天室，请遵纪守法，文明聊天");
                    clientChannel.write(welcomeMsg);
                } else if (key.isReadable()) { // 处理数据读取
                    try {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        int len = clientChannel.read(buffer);
                        if (len > 0) {
                            buffer.flip(); // 切换为读模式
                            clientChannel.write(buffer);
                        }
                    } catch (IOException e) {
                        System.out.println(((SocketChannel) key.channel()).getRemoteAddress() + "下线");
                        key.cancel();
                        key.channel().close();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyServer server = new MyServer();
    }

}