package com.seasugar.netty.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
public class User {
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
