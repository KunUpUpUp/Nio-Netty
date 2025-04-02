package com.seasugar.netty;

import com.seasugar.netty.entity.Group;
import com.seasugar.netty.entity.Msg;
import com.seasugar.netty.entity.User;
import com.seasugar.netty.message.ChatMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;

import static com.seasugar.netty.handler.LoginHandler.ID_USER;
import static com.seasugar.netty.handler.LoginHandler.USER_MAP;

public class NettyUtils {
    public static User getUserByChannel(ChannelHandlerContext ctx) {
        for (Map.Entry<Long, Channel> entry : USER_MAP.entrySet()) {
            if (entry.getValue() == ctx.channel()) {
                Long userId = entry.getKey();
                User tUser = ID_USER.get(userId);
                return tUser;
            }
        }
        return null;
    }

    // 离线信息写入
    public static List<Msg> getMsg(ChatMessage msg, Map<Long, Group> map) {
        try {
            if (msg.getMessageType() == 0x00) {
                // 私聊
                Msg offlineMsg = new Msg();
                offlineMsg.setSenderId(msg.getFrom());
                offlineMsg.setReceiverId(msg.getTo());
                offlineMsg.setContent(msg.getMsg());
                return Collections.singletonList(offlineMsg);
            } else if (msg.getMessageType() == 0x01) {
                // 群聊
                List<Msg> offlineMsgList = new ArrayList<>();
                Arrays.stream(msg.getReceiverIds().split(",")).forEach(receiver -> {
                    Msg offlineMsg = new Msg();
                    offlineMsg.setSenderId(msg.getFrom());
                    offlineMsg.setReceiverId(Long.valueOf(receiver));
                    offlineMsg.setGroupName(map.get(msg.getTo()).getGroupName());
                    offlineMsg.setContent(msg.getMsg());
                    offlineMsgList.add(offlineMsg);
                });
                return offlineMsgList;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}