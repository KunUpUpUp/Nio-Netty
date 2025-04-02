package com.seasugar.netty.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.dao.MsgMapper;
import com.seasugar.netty.entity.Msg;
import com.seasugar.netty.entity.User;
import com.seasugar.netty.message.GroupResponseMessage;
import com.seasugar.netty.message.LoginMessage;
import com.seasugar.netty.message.ResponseMessage;
import com.seasugar.netty.service.LoginService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Handler
// 要么@ChannelHandler.Sharable单实例，适用于无状态handler，要么@Scope("prototype")多实例
@Scope("prototype")
public class LoginHandler extends SimpleChannelInboundHandler<LoginMessage> {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MsgMapper msgMapper;

    // 防止并发导致登录用户错乱
    // 不过这里是prototype，并不会共享，所以是线程安全的  —— 不对，静态变量是共享的，并不会因为是prototype就安全了
    public final static ConcurrentHashMap<Long, Channel> USER_MAP = new ConcurrentHashMap<>();
    public final static ConcurrentHashMap<Long, User> ID_USER = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginMessage msg) {
//        ctx.writeAndFlush(new ResponseMessage((byte) 0x00, "111", "这条消息是由专门的接收器接收的", (byte) 0x01));
        User user;
        try {
            user = loginService.login(msg.getUsername(), msg.getPassword());
            if (user != null) {
                USER_MAP.put(user.getId(), ctx.channel());
                ID_USER.put(user.getId(), user);
                ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, 0L, user.getNickname() + " 登录成功", (byte) 0x01));
                List<Msg> tMsgs = msgMapper.selectList(new LambdaQueryWrapper<Msg>().eq(Msg::getReceiverId, user.getId()));
                for (Msg tMsg : tMsgs) {
                    if (tMsg.getGroupName() != null) {
                        // 群聊
                        ctx.writeAndFlush(new GroupResponseMessage(ID_USER.get(tMsg.getSenderId()).getNickname(), (byte) 0x00, tMsg.getSenderId(), tMsg.getContent(), (byte) 0x01, tMsg.getGroupName()));
                    } else {
                        // 私聊
                        ctx.writeAndFlush(new ResponseMessage(ID_USER.get(tMsg.getSenderId()).getNickname(), (byte) 0x00, tMsg.getSenderId(), tMsg.getContent(), (byte) 0x01));
                    }
                }
                // 异步删除
                new Thread(() -> {
                    try {
                        msgMapper.delete(new LambdaQueryWrapper<Msg>().eq(Msg::getReceiverId, user.getId()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (Exception e) {
            // 直接throw会造成断联
            ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, 0L, "登录失败——" + e.getCause().getLocalizedMessage(), (byte) 0x00));
        }
    }
}