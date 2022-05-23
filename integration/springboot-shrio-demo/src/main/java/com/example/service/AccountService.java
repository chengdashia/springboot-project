package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Account;

public interface AccountService extends IService<Account> {
    public Account findByUsername(String username);
}
