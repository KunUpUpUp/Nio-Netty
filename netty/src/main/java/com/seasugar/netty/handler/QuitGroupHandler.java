package com.seasugar.netty.handler;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.dao.GroupMapper;
import com.seasugar.netty.entity.tGroup;
import com.seasugar.netty.message.QuitGroupMessage;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.seasugar.netty.handler.GroupHandler.GROUP_MAP;

@Slf4j
@Handler
@Scope("prototype")
public class QuitGroupHandler extends SimpleChannelInboundHandler<QuitGroupMessage> {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupMessage msg) throws Exception {
        if (GROUP_MAP.containsKey(msg.getGroupId())) {
            GROUP_MAP.get(msg.getGroupId()).remove(ctx.channel());
            new Thread(
                    () -> {
                        tGroup tGroup = groupMapper.selectById(msg.getGroupId());
                        String userIds = tGroup.getUserIds();
                        String[] userList = userIds.split(",");
                        //**** Arrays.asList()返回的是一个不可变的List，不能使用add和remove方法 ****
//                        List<String> list = Arrays.asList(userList);
                        List<String> list = new ArrayList<>(Arrays.asList(userList));
                        list.remove(msg.getUserId().toString());
                        String newIds = String.join(",", list);
                        tGroup.setUserIds(newIds);
                        groupMapper.updateById(tGroup);
                    }
            ).start();
        } else {
            ctx.writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, 0L, "您要退出的群不存在", (byte) 0x01));
        }
    }
}
