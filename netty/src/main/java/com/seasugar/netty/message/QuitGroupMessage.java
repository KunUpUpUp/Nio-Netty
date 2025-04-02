package com.seasugar.netty.message;

import lombok.Data;

@Data
public class QuitGroupMessage extends Message{
    private Long groupId;
    private Long userId;

    @Override
    public byte getType() {
        return QUIT_GROUP;
    }
}
