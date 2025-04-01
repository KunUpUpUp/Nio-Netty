package com.seasugar.netty.handler;

import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Slf4j
@Handler
@Scope("prototype")
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, 0L, "服务端已接受您的消息————" + msg, (byte) 0x01));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, 0L, "您已连接断开", (byte) 0x01));
        ctx.channel().close();
    }
}
