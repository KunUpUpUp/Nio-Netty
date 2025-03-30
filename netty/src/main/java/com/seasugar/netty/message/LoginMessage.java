package com.seasugar.netty.message;

import lombok.Data;

@Data
public class LoginMessage extends Message {
    private String username;
    private String password;

    @Override
    public byte getType() {
        return LOGIN;
    }
}
