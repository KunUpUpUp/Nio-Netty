package com.seasugar.netty.entity;

import lombok.Data;

import java.util.Date;

@Data
public class tMsg {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Date createTime;
    private Date updateTime;
}
