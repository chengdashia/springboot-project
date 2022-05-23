package com.nongXingGang.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.nongXingGang.pojo.Demand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Repository
public interface DemandMapper extends MPJBaseMapper<Demand> {

    @Select("select * from demand limit #{start},#{end}")
    public Demand[] selectByPage(@Param("start") int start, @Param("end") int end);

}
