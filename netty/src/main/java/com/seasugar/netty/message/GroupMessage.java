package com.seasugar.netty.message;

import lombok.Data;

@Data
public class GroupMessage extends Message{
    private String groupName;
    private String userIds;

    @Override
    public byte getType() {
        return GROUP;
    }
}
