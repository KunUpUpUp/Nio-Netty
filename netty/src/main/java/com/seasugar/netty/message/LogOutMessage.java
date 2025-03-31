package com.seasugar.netty.message;

public class LogOutMessage extends Message {
    @Override
    public byte getType() {
        return LOGOUT;
    }
}
