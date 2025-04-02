package com.seasugar.netty.entity;

import lombok.Data;

import java.util.Date;

@Data
public class tUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Date birthday;
    private Date registerTime;
    private Date lastLoginTime;
    private Date createTime;
    private Date updateTime;
    private boolean isOnline;
    private boolean isDeleted;
}
