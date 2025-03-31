package com.seasugar.netty.message;

public class ResponseMessage extends AbstractResponseMessage {

    public ResponseMessage(byte protocol, String from, String msg, byte result) {
        super(protocol, from, msg, result);

    }

    @Override
    public byte getType() {
        return RESPONSE;
    }
}
