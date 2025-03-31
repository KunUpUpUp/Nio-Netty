package com.seasugar.netty.message;

import lombok.Data;

@Data
public abstract class AbstractResponseMessage extends Message {
    // 处理结果 0 失败 1 成功
    private byte result;

    public AbstractResponseMessage(Byte protocol, String from, String msg, byte result) {
        super(from, "", msg, protocol);
        this.result = result;
    }


}
