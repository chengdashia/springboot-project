package com.cloudDisk.mapper;

import com.cloudDisk.pojo.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 成大事
 * @since 2022-04-10
 */
@Repository
public interface FileInfoMapper extends MPJBaseMapper<FileInfo> {


    @Select("select file_path from file_info where file_id = #{fileId} and file_del = 0")
    FileInfo getFilePathByFileId(String fileId);


    @Delete("delete from file_info where file_id = #{fileId}")
    int delFileInfoByFileId(String fileId);

}
