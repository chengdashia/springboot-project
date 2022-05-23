package com.oneKeyRecycling.pojo;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 成大事
 * @since 2022-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ChatMsg对象", description="")
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    private String chatId;

    @ApiModelProperty(value = "发送者id")
    private String sendUserId;

    @ApiModelProperty(value = "接收者id")
    private String reciveUserId;

    @ApiModelProperty(value = "发送内容")
    private String sendText;

    @ApiModelProperty(value = "发送时间")
    private Date sendTime;

    @ApiModelProperty(value = "消息类型")
    private String msgType;


}
