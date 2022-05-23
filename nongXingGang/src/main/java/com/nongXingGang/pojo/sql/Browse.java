package com.nongXingGang.pojo.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author 成大事
 * @since 2022/5/11 20:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Browse {

    private String brUUId;

    private String thingUUId;

    private String imgUrl;

    private String name;

    private int status;

    private Date  createTime;
}
