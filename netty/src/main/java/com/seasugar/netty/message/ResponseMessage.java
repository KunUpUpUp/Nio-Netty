package com.seasugar.netty.message;

public class ResponseMessage extends AbstractResponseMessage{

    public ResponseMessage(byte type, String from, String msg, byte result) {
        super(type, from, msg, result);
    }

    @Override
    public byte getType() {
        return 0;
    }
}
