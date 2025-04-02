package com.seasugar.netty;

import com.seasugar.netty.entity.tUser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

import static com.seasugar.netty.handler.LoginHandler.ID_USER;
import static com.seasugar.netty.handler.LoginHandler.USER_MAP;

public class NettyUtils {
    public static tUser getUserByChannel(ChannelHandlerContext ctx) {
        for (Map.Entry<Long, Channel> entry : USER_MAP.entrySet()) {
            if (entry.getValue() == ctx.channel()) {
                Long userId = entry.getKey();
                tUser tUser = ID_USER.get(userId);
                return tUser;
            }
        }
        return null;
    }
}