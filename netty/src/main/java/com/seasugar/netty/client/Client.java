package com.seasugar.netty.client;

import com.seasugar.netty.message.LoginMessage;
import com.seasugar.netty.message.OtherMessage;
import com.seasugar.netty.protocol.MessageDuplxCodec;
import com.seasugar.netty.message.Message;
import com.seasugar.netty.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast(new ProcotolFrameDecoder());
                            ch.pipeline().addLast(new MessageDuplxCodec());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

@Slf4j
class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                log.info("请输入操作(send/exit)");
                String op = sc.nextLine();
                switch (op) {
                    case "send":
                        String message = sc.nextLine();
                        OtherMessage sendMsg = new OtherMessage();
                        sendMsg.setMsg(message);
                        sendMsg.setFrom("111");
                        ctx.write(sendMsg);
                        LoginMessage loginMessage = new LoginMessage();
                        loginMessage.setMsg("第二阶段");
                        loginMessage.setFrom("111");
                        loginMessage.setUsername("zkp");
                        loginMessage.setPassword("123");
                        ctx.write(loginMessage);
                        ctx.flush();
                        break;
                    case "exit":
                        ctx.close();
                        return;
                }
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Message) {
            log.info(((Message) msg).getMsg());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
