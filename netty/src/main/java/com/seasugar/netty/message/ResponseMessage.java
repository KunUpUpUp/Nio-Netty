package com.seasugar.netty.message;

public class ResponseMessage extends AbstractResponseMessage {

    private String fromUser;

    public ResponseMessage(byte protocol, String from, String msg, byte result) {
        super(protocol, from, msg, result);
    }

    @Override
    public byte getType() {
        return RESPONSE;
    }
}
