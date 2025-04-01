package com.seasugar.netty.message;

import lombok.Data;

@Data
public abstract class AbstractResponseMessage extends Message {
    // 处理结果 0 失败 1 成功
    private byte result;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(Byte protocol, Long from, Long to, String msg, byte result) {
        super(from, to, msg, protocol);
        this.result = result;
    }


}
