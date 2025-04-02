package com.seasugar.netty.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.seasugar.netty.dao.UserMapper;
import com.seasugar.netty.entity.User;
import com.seasugar.netty.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        try {
            // 参数校验
            if (StringUtils.isBlank(username)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            if (StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("密码不能为空");
            }

            // 构建查询条件（使用Lambda表达式防止字段名拼写错误）
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, username)
                    .last("LIMIT 1"));  // 明确限制只查一条记录

            // 用户不存在检查
            if (user == null) {
                throw new AuthenticationException("该用户不存在");
            }

            // 密码验证（建议使用加密验证）
            if (!password.equals(user.getPassword())) {
                throw new AuthenticationException("用户名或密码错误");
            }

            // 用户登录成功——更新数据库
            new Thread(() -> {
//                 更新最后登录时间
                user.setLastLoginTime(new Date());
                user.setUpdateTime(new Date());
                user.setOnline(true);
                userMapper.updateById(user);
            }).start();
            return user;  // 返回用户信息供后续使用
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

}
