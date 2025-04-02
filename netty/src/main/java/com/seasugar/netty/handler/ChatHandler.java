package com.seasugar.netty.handler;

import com.seasugar.netty.NettyUtils;
import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.dao.MsgMapper;
import com.seasugar.netty.entity.Group;
import com.seasugar.netty.entity.Msg;
import com.seasugar.netty.message.ChatMessage;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.seasugar.netty.handler.LoginHandler.ID_USER;
import static com.seasugar.netty.handler.LoginHandler.USER_MAP;

@Slf4j
@Handler
@Scope("prototype")
public class ChatHandler extends SimpleChannelInboundHandler<ChatMessage> {

    // key——groupid ，value——用户ids
    @Autowired
    private Map<Long, List<String>> GROUP_MAP;
    @Autowired
    private Map<Long, Group> ID_GROUP;
    @Autowired
    private MsgMapper msgMapper;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) {
//        if (!USER_MAP.containsKey(msg.getFrom())) {
//            ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, msg.getFrom(), "用户未登录，请登录后发送信息", (byte) 0x00));
//            return;
//        }
        if (msg.getMessageType() == 0x00) {
            // 私聊
            if (USER_MAP.containsKey(msg.getTo())) {
                ctx.writeAndFlush(new ResponseMessage(ID_USER.get(msg.getFrom()).getNickname(), (byte) 0x00, msg.getFrom(), msg.getMsg(), (byte) 0x01));
                USER_MAP.get(msg.getTo()).writeAndFlush(new ResponseMessage(ID_USER.get(msg.getFrom()).getNickname(), (byte) 0x00, msg.getFrom(), msg.getMsg(), (byte) 0x01));
            } else {
                // 不在线就写入离线信息表
                new Thread(() -> {
                    Msg tMsg = NettyUtils.getMsg(msg, null).get(0);
                    msgMapper.insert(tMsg);
                }).start();
            }
        } else if (msg.getMessageType() == 0x01) {
            // 群聊
            // 群聊id存在
            Long groupId = msg.getTo();
            List<String> userList = new ArrayList<>(GROUP_MAP.get(groupId));
            if (!userList.isEmpty()) {
                // 群聊成员
                Iterator<String> userIteractor = userList.iterator();
                StringBuilder sb = new StringBuilder();
                // 安全删除
                while (userIteractor.hasNext()) {
                    String user = userIteractor.next();
                    // 在线成员
                    if (USER_MAP.containsKey(Long.parseLong(user))) {
                        Channel channel = USER_MAP.get(Long.parseLong(user));
                        channel.writeAndFlush(new ResponseMessage(ID_USER.get(msg.getFrom()).getNickname(), (byte) 0x00, msg.getFrom(), msg.getMsg(), (byte) 0x01));
                        userIteractor.remove();
                    } else {
                        // 不在线成员
                        sb.append(user).append(",");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                msg.setReceiverIds(sb.toString());
                // 不在线
                new Thread(() -> {
                    List<Msg> resList = NettyUtils.getMsg(msg, ID_GROUP);
                    // 应批量写入
                    resList.forEach(msgMapper::insert);
                }).start();
            }
        }
    }
}
