package com.seasugar.netty.message;

import lombok.Data;

@Data
public abstract class AbstractResponseMessage extends Message {
    // 处理结果 0 失败 1 成功
    private byte result;

    public AbstractResponseMessage(Byte protocol, String userName, String msg, byte result) {
        super(userName, "", msg, protocol);
        this.result = result;
    }


}
