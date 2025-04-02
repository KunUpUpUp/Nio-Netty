package com.seasugar.netty.message;

public class GroupResponseMessage extends ResponseMessage {
    private String groupName;

    public GroupResponseMessage(String sender, byte protocol, Long from, String msg, byte result, String groupName) {
        super(sender, protocol, from, msg, result);
        this.groupName = groupName;
    }
}
