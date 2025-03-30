package com.seasugar.netty.handler;

import com.seasugar.netty.message.LoginMessage;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MsgInHandler extends SimpleChannelInboundHandler<LoginMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginMessage msg) throws Exception {
        ctx.writeAndFlush(new ResponseMessage((byte) 0x00, "111", "这条消息是由专门的接收器接收的", (byte) 0x01));

    }
}