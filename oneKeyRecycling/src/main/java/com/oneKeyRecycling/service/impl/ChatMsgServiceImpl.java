package com.oneKeyRecycling.service.impl;

import com.oneKeyRecycling.pojo.ChatMsg;
import com.oneKeyRecycling.mapper.ChatMsgMapper;
import com.oneKeyRecycling.service.ChatMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-17
 */
@Service
public class ChatMsgServiceImpl extends ServiceImpl<ChatMsgMapper, ChatMsg> implements ChatMsgService {

    @Resource
    private ChatMsgMapper chatMsgMapper;

    @Override
    public void addMsg(ChatMsg chatMsg) {
        chatMsgMapper.insert(chatMsg);
    }
}
