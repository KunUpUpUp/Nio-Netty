package com.seasugar.netty.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seasugar.netty.entity.Msg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MsgMapper extends BaseMapper<Msg> {

}
