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
@TableName("goods")
@ApiModel("商品")
public class Goods implements Serializable {
    @ApiModelProperty("商品ID")
    @TableId(type = IdType.AUTO)
    private Integer goodsId;

    @ApiModelProperty("商品类型ID")
    private Integer typeId;

    @ApiModelProperty(value = "商品名称", example = "40米大刀")
    private String name;

    @ApiModelProperty(value = "商品价格", example = "8.99")
    private Double price;

    @ApiModelProperty(value = "商品投放数量", example = "25")
    private Integer amount;

    @ApiModelProperty(value = "商品库存数量", example = "60")
    private Integer stock;

    @ApiModelProperty(value = "上架时间", example = "2023-02-17 17:12:26")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
}
