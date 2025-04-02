package com.seasugar.netty.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_msg")
public class Msg {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String groupName;
    private String content;
    private Date createTime;
    private Date updateTime;
}
