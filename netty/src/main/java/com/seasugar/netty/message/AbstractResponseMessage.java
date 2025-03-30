package com.seasugar.netty.message;

import lombok.Data;

@Data
public abstract class AbstractResponseMessage extends Message {
    // 处理结果
    private byte result;

    public AbstractResponseMessage(byte type, String from, String msg, byte result) {
        super(from, msg);
        this.result = result;
    }

}
