package com.cloudDisk.service;

import com.cloudDisk.pojo.FileLabel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-14
 */
@Transactional
public interface FileLabelService extends IService<FileLabel> {

}
