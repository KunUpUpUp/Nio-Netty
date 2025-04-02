package com.seasugar.netty.message;

import lombok.Data;

@Data
public class ChatMessage extends Message{
    // 私聊还是群聊 0 私聊 1 群聊
    private Byte messageType;
    // 群聊用户ids
    private String receiverIds;

    @Override
    public byte getType() {
        return CHAT;
    }
}
