package com.oneKeyRecycling.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TShop对象", description="")
public class TShop implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id，主键")
      @TableId(value = "shop_id", type = IdType.ID_WORKER)
    private String shopId;

    @ApiModelProperty(value = "外键，商家id")
    private String uId;

    @ApiModelProperty(value = "店铺名字")
    private String shopName;

    @ApiModelProperty(value = "店铺标题")
    private String shopTitle;

    @ApiModelProperty(value = "店铺评论")
    private String shopComment;

    @ApiModelProperty(value = "店铺好评度，1， 2， 3....")
    private Integer shopRank;

    @ApiModelProperty(value = "访问数")
    private Integer visitsNumber;

    @ApiModelProperty(value = "店铺封面")
    private String shopCover;

    @ApiModelProperty(value = "店铺图片")
    private String shopImgO;

    @ApiModelProperty(value = "店铺图片")
    private String shopImgTw;

    @ApiModelProperty(value = "店铺图片")
    private String shopImgTh;


}
