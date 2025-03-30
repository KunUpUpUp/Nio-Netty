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
    private String from;
    private String msg;

    public static Map<Byte, Class<? extends Message>> MESSAGE_CLAZZ = new HashMap<>();

    public abstract byte getType();

    final static byte LOGIN = 0x00;
    final static byte OTHER = 0x01;

    static {
        MESSAGE_CLAZZ.put(LOGIN, LoginMessage.class);
        MESSAGE_CLAZZ.put(OTHER, OtherMessage.class);
    }
}
