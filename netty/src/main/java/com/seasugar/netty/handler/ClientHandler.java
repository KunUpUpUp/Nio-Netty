package com.seasugar.netty.handler;

import com.alibaba.fastjson2.JSON;
import com.seasugar.netty.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                log.info("请输入操作(send/exit)");
                String op = sc.nextLine();
                switch (op) {
                    case "login":
                        LoginMessage loginMessage = new LoginMessage();
                        loginMessage.setUsername(sc.nextLine());
                        loginMessage.setPassword(sc.nextLine());
                        ctx.writeAndFlush(loginMessage);
                        break;
                    case "send":
                        String message = sc.nextLine();
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setMsg(message);
                        ctx.writeAndFlush(chatMessage);
                        break;
                    case "exit":
                        ctx.close();
                        return;
                }
            }
        }).start();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage) throws Exception {
        log.info("收到消息:{}", responseMessage.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
