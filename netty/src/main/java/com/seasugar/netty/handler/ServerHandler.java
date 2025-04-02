package com.seasugar.netty.handler;

import com.seasugar.netty.NettyUtils;
import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.dao.UserMapper;
import com.seasugar.netty.entity.User;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import static com.seasugar.netty.handler.LoginHandler.USER_MAP;

@Slf4j
@Handler
@Scope("prototype")
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private UserMapper userMapper;

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
        new Thread(() -> {
            User tUser = NettyUtils.getUserByChannel(ctx);
            if (tUser != null) {
                USER_MAP.remove(tUser.getId());
                tUser.setOnline(false);
                userMapper.updateById(tUser);
            }
        }).start();
        ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, 0L, "您已连接断开", (byte) 0x01));
        ctx.channel().close();
    }
}
