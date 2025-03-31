package com.seasugar.netty.handler;

import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.message.ChatMessage;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import static com.seasugar.netty.handler.LoginHandler.userMap;

@Slf4j
@Handler
@Scope("prototype")
public class ChatHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) {
        String userName = userMap.get(ctx.channel());
        for (Channel channel : userMap.keySet()) {
            
        }
        ctx.writeAndFlush(new ResponseMessage((byte) 0x00, userName, msg.getMsg(), (byte) 0x01));
    }
}
