package com.seasugar.netty.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seasugar.netty.annotation.Handler;
import com.seasugar.netty.dao.GroupMapper;
import com.seasugar.netty.entity.Group;
import com.seasugar.netty.message.GroupMessage;
import com.seasugar.netty.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.seasugar.netty.handler.LoginHandler.USER_MAP;

@Slf4j
@Handler
@Scope("prototype")
public class GroupHandler extends SimpleChannelInboundHandler<GroupMessage> {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private Map<Long, List<String>> GROUP_MAP;

    // 保证线程安全
//    public final static ConcurrentHashMap<Long, List<Channel>> GROUP_MAP = new ConcurrentHashMap<>();
    // 使用静态属性，防止prototype出来的每个count都不同，由于在concurrentMap中计数，安全得以保证
//    private static short COUNT;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessage msg) throws Exception {
        QueryWrapper<Group> wrapper = new QueryWrapper<>();
        wrapper.select("max(id) as maxId");
        Map<String, Object> map = groupMapper.selectMaps(wrapper).get(0);
        Long maxId = map == null ? 1L : (Long) map.get("maxId") + 1;
        String userIds = msg.getUserIds();
        new Thread(() -> broadCastMessage(maxId, ctx, msg)).start();
        List<String> userList = Arrays.asList(userIds.split(","));
        if (userList.size() > 1) {
            for (Long userId : USER_MAP.keySet()) {
                if (userList.contains(userId.toString())) {
                    USER_MAP.get(userId).writeAndFlush("加入 " + msg.getGroupName() + " 成功");
                }
            }
            GROUP_MAP.put(maxId, Arrays.asList(userIds.split(",")));
        }
    }

    public void broadCastMessage(Long groupId, ChannelHandlerContext ctx, GroupMessage msg) {
        String userList = msg.getUserIds();
        String[] users = userList.split(",");
        if (users.length > 1) {
            // 群聊
            Group group = new Group();
            group.setId(groupId);
            group.setGroupName(msg.getGroupName());
            group.setOwner(msg.getFrom());
            group.setUserIds(userList);
            groupMapper.insert(group);
            for (Long userId : USER_MAP.keySet()) {
                if (userList.contains(userId.toString())) {
                    USER_MAP.get(userId).writeAndFlush(new ResponseMessage("服务器", (byte) 0x00, msg.getFrom(), "成功加入" + group.getGroupName() + "群", (byte) 0x01));
                }
            }
        } else {
            ctx.writeAndFlush("群聊必须大于两人!");
        }
    }
}
