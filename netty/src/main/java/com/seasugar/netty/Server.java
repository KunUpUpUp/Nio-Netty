package com.seasugar.netty;

import com.seasugar.netty.dao.UserMapper;
import com.seasugar.netty.entity.tUser;
import com.seasugar.netty.handler.*;
import com.seasugar.netty.protocol.MessageDuplxCodec;
import com.seasugar.netty.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

import static com.seasugar.netty.handler.LoginHandler.ID_USER;
import static com.seasugar.netty.handler.LoginHandler.USER_MAP;


@Slf4j
@SpringBootApplication
public class Server {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private UserMapper userMapper;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Server.class, args);
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
                                        .addLast(new IdleStateHandler(300, 0, 0))
                                        .addLast(new ChannelDuplexHandler() {
                                            @Override
                                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                                if (evt instanceof IdleStateEvent) {
                                                    IdleStateEvent event = (IdleStateEvent) evt;
                                                    if (event.state() == IdleState.READER_IDLE) {
                                                        new Thread(() -> {
                                                            tUser tUser = NettyUtils.getUserByChannel(ctx);
                                                            if (tUser != null) {
                                                                tUser.setOnline(false);
                                                                userMapper.updateById(tUser);
                                                            }
                                                        }).start();
                                                        ctx.channel().close();
                                                    }
                                                }
                                            }
                                        })
                                        .addLast(applicationContext.getBean(LoginHandler.class))
                                        .addLast(applicationContext.getBean(ChatHandler.class))
                                        .addLast(applicationContext.getBean(GroupHandler.class))
                                        .addLast(applicationContext.getBean(QuitGroupHandler.class))
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