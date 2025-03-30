package com.seasugar.netty.message;

public class OtherMessage extends Message {
    @Override
    public byte getType() {
        return OTHER;
    }
}
