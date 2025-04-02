package com.seasugar.netty.handler;

import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.message.ChatMessage;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.Map;

import static com.seasugar.netty.handler.LoginHandler.ID_USER;
import static com.seasugar.netty.handler.LoginHandler.USER_MAP;

@Slf4j
@Handler
@Scope("prototype")
public class ChatHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Autowired
    private Map<Long, List<String>> GROUP_MAP;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) {
        if (!USER_MAP.containsKey(msg.getFrom())) {
            ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, msg.getFrom(), "用户未登录，请登录后发送信息", (byte) 0x00));
            return;
        }
        if (msg.getMessageType() == 0x00) {
            // 私聊
            if (USER_MAP.containsKey(msg.getTo())) {
                ctx.writeAndFlush(new ResponseMessage(ID_USER.get(msg.getFrom()).getNickname(), (byte) 0x00, msg.getFrom(), msg.getMsg(), (byte) 0x01));
                USER_MAP.get(msg.getTo()).writeAndFlush(new ResponseMessage(ID_USER.get(msg.getFrom()).getNickname(), (byte) 0x00, msg.getFrom(), msg.getMsg(), (byte) 0x01));
            } else {
                ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, msg.getFrom(), "对方不在线", (byte) 0x00));
            }
        } else if (msg.getMessageType() == 0x01) {
            // 群聊
            if (GROUP_MAP.containsKey(msg.getTo())) {
                for (String user : GROUP_MAP.get(msg.getTo())) {
                    for (Long userId : USER_MAP.keySet()) {
                        if (userId.toString().equals(user)) {
                            Channel channel = USER_MAP.get(userId);
                            channel.writeAndFlush(new ResponseMessage(ID_USER.get(msg.getFrom()).getNickname(), (byte) 0x00, msg.getFrom(), msg.getMsg(), (byte) 0x01));
                        }
                    }
                }
            }
        }
    }
}
