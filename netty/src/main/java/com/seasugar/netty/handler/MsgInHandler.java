package com.seasugar.netty.handler;

import com.seasugar.netty.message.SendMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MsgInHandler extends SimpleChannelInboundHandler<SendMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendMsg msg) throws Exception {
        log.info("我只接收SendMsg类型的消息{}", msg.toString());
    }
}
