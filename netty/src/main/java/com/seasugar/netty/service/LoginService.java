package com.seasugar.netty.service;

import com.seasugar.netty.entity.User;

public interface LoginService {
    User login(String username, String password);
}
