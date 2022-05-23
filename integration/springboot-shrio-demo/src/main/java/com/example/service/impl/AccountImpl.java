package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.AccountMapper;
import com.example.pojo.Account;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountImpl extends ServiceImpl<AccountMapper, Account > implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account findByUsername(String username) {

        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return accountMapper.selectOne(wrapper);
    }
}
