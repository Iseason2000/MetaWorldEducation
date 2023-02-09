package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("goods_record")
@ApiModel("商品购买记录")
public class GoodsRecord implements Serializable {
    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty("购买者ID")
    private Integer playerId;

    @ApiModelProperty("商品ID")
    private Integer goodsId;

    @ApiModelProperty("购买数量")
    private Integer amount;

    @ApiModelProperty(value = "购买时间", example = "2023-02-17 12:22:51")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
}
