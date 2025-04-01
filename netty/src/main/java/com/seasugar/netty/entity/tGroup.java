package com.seasugar.netty.entity;

import lombok.Data;

import java.util.Date;

@Data
public class tGroup {
    private Long id;
    private Long owner;
    private String groupName;
    private String userIds;
    private Date createTime;
    private Date updateTime;
    private boolean isDeleted;
}

