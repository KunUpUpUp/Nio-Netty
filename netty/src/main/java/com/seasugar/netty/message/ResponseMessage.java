package com.seasugar.netty.message;

import lombok.Data;

@Data
public class ResponseMessage extends AbstractResponseMessage {

    // 发送者名称
    private String sender;


    public ResponseMessage() {
        super();

    }

    public ResponseMessage(String sender, byte protocol, Long from, String msg, byte result) {
        super(protocol, from, 0L, msg, result);
        this.sender = sender;
    }

    @Override
    public byte getType() {
        return RESPONSE;
    }
}
