package com.seasugar.netty.handler;

import com.seasugar.netty.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
public class ClientHandler extends SimpleChannelInboundHandler<ResponseMessage> {
//    private final Semaphore semaphore = new Semaphore(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                log.info("请输入操作(login/send/group/quit/exit)");
                String op = sc.nextLine();
                switch (op) {
                    case "login":
                        LoginMessage loginMessage = new LoginMessage();
                        log.info("请输入用户名:");
                        loginMessage.setUsername(sc.nextLine());
                        log.info("请输入密码:");
                        loginMessage.setPassword(sc.nextLine());
                        ctx.writeAndFlush(loginMessage);
                        break;
                    case "send":
                        ChatMessage chatMessage = new ChatMessage();
                        log.info("请输入聊天类型(0-私聊 1-群聊):");
                        byte messageType = sc.nextByte();
                        chatMessage.setMessageType(messageType);
                        // nextByte()不消耗换行符
                        sc.nextLine();
                        if (messageType == 0x00) {
                            log.info("请输入内容:");
                            chatMessage.setMsg(sc.nextLine());
                            log.info("请输入发送者id:");
                            chatMessage.setFrom(sc.nextLong());
                            log.info("请输入接收者id:");
                            chatMessage.setTo(sc.nextLong());
                        } else if (messageType == 0x01) {
                            log.info("请输入内容:");
                            chatMessage.setMsg(sc.nextLine());
                            log.info("请输入id:");
                            chatMessage.setFrom(sc.nextLong());
                            log.info("请输入群聊id:");
                            chatMessage.setTo(sc.nextLong());
                        }
                        ctx.writeAndFlush(chatMessage);
                        break;
                    case "group":
                        GroupMessage groupMessage = new GroupMessage();
                        log.info("请输入群名:");
                        groupMessage.setGroupName(sc.nextLine());
                        log.info("请输入群主id:");
                        groupMessage.setFrom(sc.nextLong());
                        sc.nextLine();
                        log.info("请输入要拉取人的id:");
                        groupMessage.setUserIds(sc.nextLine());
                        ctx.writeAndFlush(groupMessage);
                        break;
                    case "quit" :
                        QuitGroupMessage quitGroupMessage = new QuitGroupMessage();
                        log.info("请输入群id:");
                        quitGroupMessage.setGroupId(sc.nextLong());
                        log.info("请输入您的id:");
                        quitGroupMessage.setUserId(sc.nextLong());
                        ctx.writeAndFlush(quitGroupMessage);
                        break;
                    case "exit":
                        ctx.close();
                        return;
                    default:
                        break;
                }
//                try {
//                    semaphore.acquire();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }).start();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        log.info("{}：{}", msg.getSender(), msg.getMsg());
//        semaphore.release();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
