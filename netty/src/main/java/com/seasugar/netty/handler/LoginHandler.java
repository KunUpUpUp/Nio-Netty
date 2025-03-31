package com.seasugar.netty.handler;

import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.entity.User;
import com.seasugar.netty.message.LoginMessage;
import com.seasugar.netty.message.ResponseMessage;
import com.seasugar.netty.service.LoginService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Handler
public class LoginHandler extends SimpleChannelInboundHandler<LoginMessage> {
    @Autowired
    private LoginService loginService;

    final static Map<Channel, String> userMap = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginMessage msg) {
//        ctx.writeAndFlush(new ResponseMessage((byte) 0x00, "111", "这条消息是由专门的接收器接收的", (byte) 0x01));
        try {
            User user = loginService.login(msg.getUsername(), msg.getPassword());
            if (user != null) {
                userMap.put(ctx.channel(), user.getUsername());
                ctx.writeAndFlush(new ResponseMessage((byte) 0x00, ctx.channel().remoteAddress().toString(), msg.getUsername() + " 登录成功", (byte) 0x01));
            }
        } catch (Exception e) {
            // 直接throw会造成断联
            ctx.writeAndFlush(new ResponseMessage((byte) 0x00, ctx.channel().remoteAddress().toString(), msg.getUsername() + " 登录失败", (byte) 0x00));
        }
    }
}