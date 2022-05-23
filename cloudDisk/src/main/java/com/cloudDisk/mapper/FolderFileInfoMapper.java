package com.cloudDisk.mapper;

import com.cloudDisk.pojo.FolderFileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 成大事
 * @since 2022-04-15
 */
@Transactional
public interface FolderFileInfoMapper extends MPJBaseMapper<FolderFileInfo> {

}
