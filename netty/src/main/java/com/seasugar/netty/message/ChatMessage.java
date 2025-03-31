package com.seasugar.netty.message;

public class ChatMessage extends Message{

    @Override
    public byte getType() {
        return CHAT;
    }
}
