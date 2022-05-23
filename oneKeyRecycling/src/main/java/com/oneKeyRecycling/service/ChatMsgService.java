package com.oneKeyRecycling.service;

import com.oneKeyRecycling.pojo.ChatMsg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-17
 */
public interface ChatMsgService extends IService<ChatMsg> {

    //添加聊天记录
    void addMsg(ChatMsg chatMsg);
}
