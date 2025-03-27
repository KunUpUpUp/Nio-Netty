package com.seasugar.netty.message;

import lombok.Data;

@Data
public class SendMsg {
    private String msg;
    private String from;
}
