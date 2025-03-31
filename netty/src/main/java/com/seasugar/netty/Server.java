package com.seasugar.netty;

import com.seasugar.netty.handler.ChatHandler;
import com.seasugar.netty.handler.LoginHandler;
import com.seasugar.netty.handler.ServerHandler;
import com.seasugar.netty.protocol.MessageDuplxCodec;
import com.seasugar.netty.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Server {

    @Autowired
    private LoginHandler loginHandler;
    @Autowired
    private ChatHandler chatHandler;
    @Autowired
    private ServerHandler serverHandler;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Server.class, args);
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        public void initChannel(SocketChannel ch) {
//                            ch.pipeline()
//                                    .addLast(new LoggingHandler(LogLevel.INFO))
//                                    .addLast(new ProcotolFrameDecoder())
//                                    .addLast(new MessageDuplxCodec())
//                                    .addLast(msgInHandler) // 使用注入的Bean
//                                    .addLast(serverHandler);
//                        }
//                    });
//
//            ChannelFuture f = b.bind(8080).sync();
//            f.channel().closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
//        }
    }

    @Bean // 使用@Bean初始化Netty服务
    public CommandLineRunner run() {
        return args -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                ch.pipeline()
                                        .addLast(new LoggingHandler(LogLevel.INFO))
                                        .addLast(new ProcotolFrameDecoder())
                                        .addLast(new MessageDuplxCodec())
                                        // 如果不传递，就会被第一个Handler处理
//                                        .addLast(serverHandler)
//                                        .addLast(loginHandler)
//                                        .addLast(chatHandler)
//                                        .addLast(serverHandler);
                                        .addLast(applicationContext.getBean(LoginHandler.class))
                                        .addLast(applicationContext.getBean(ChatHandler.class))
                                        .addLast(applicationContext.getBean(ServerHandler.class));
                            }
                        });

                ChannelFuture f = b.bind(8080).sync();
                f.channel().closeFuture().sync();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        };
    }
}