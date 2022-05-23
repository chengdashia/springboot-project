package com.onlineExam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlineExam.pojo.AnswerInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-19
 */
@Transactional
public interface AnswerInfoService extends IService<AnswerInfo> {

    //答案保存
    int testPaperSave(String testUUId);
}
