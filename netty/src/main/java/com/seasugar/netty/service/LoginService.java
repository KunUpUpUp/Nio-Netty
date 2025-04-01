package com.seasugar.netty.service;

import com.seasugar.netty.entity.tUser;

public interface LoginService {
    tUser login(String username, String password);
}
