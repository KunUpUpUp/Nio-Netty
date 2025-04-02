// 文件路径建议：netty/src/main/java/com/seasugar/netty/dao/LoginMapper.java
package com.seasugar.netty.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seasugar.netty.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承BaseMapper后已包含基础CRUD方法
    // 如需自定义SQL查询可在此添加方法
}