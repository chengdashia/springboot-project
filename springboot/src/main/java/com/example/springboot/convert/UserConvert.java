package com.example.springboot.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author 成大事
 * @since 2022/5/29 21:19
 */
//添加 @Mapper 注解，声明它是一个 MapStruct Mapper 映射器。
@Mapper
public interface UserConvert {

    //通过调用 Mappers 的 #getMapper(Class<T> clazz) 方法，获得 MapStruct 帮我们自动生成的 UserConvert 实现类的对象。
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    @Mapping(source = "pwd",target = "password")
    @Mapping(target = "id",ignore = true)
    UserBO convert(UserDO userDO);

}
