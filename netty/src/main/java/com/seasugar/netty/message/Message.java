package com.seasugar.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message {
    // 发送者id
    private Long from;
    // 接收者id
    private Long to;
    // 消息
    private String msg;
    // 消息协议 0 json
    private Byte protocol;

    public static Map<Byte, Class<? extends Message>> MESSAGE_CLAZZ = new HashMap<>();

    public abstract byte getType();

    final static byte LOGIN = 0x00;
    final static byte LOGOUT = 0x01;
    final static byte CHAT = 0x02;
    final static byte RESPONSE = 0x03;
    final static byte GROUP = 0x04;
    final static byte QUIT_GROUP = 0x05;
    final static byte OTHER = 0x7f;

    static {
        MESSAGE_CLAZZ.put(LOGIN, LoginMessage.class);
        MESSAGE_CLAZZ.put(LOGOUT, LogOutMessage.class);
        MESSAGE_CLAZZ.put(CHAT, ChatMessage.class);
        MESSAGE_CLAZZ.put(GROUP, GroupMessage.class);
        MESSAGE_CLAZZ.put(QUIT_GROUP, QuitGroupMessage.class);
        MESSAGE_CLAZZ.put(RESPONSE, ResponseMessage.class);
    }
}
