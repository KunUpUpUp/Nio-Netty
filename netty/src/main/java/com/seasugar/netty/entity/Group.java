package com.seasugar.netty.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_group")
public class Group {
    private Long id;
    private Long owner;
    private String groupName;
    private String userIds;
    private Date createTime;
    private Date updateTime;
    private boolean isDeleted;
}

