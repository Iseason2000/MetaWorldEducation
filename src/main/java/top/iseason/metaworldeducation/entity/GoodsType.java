package top.iseason.metaworldeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("goods_types")
@ApiModel("商品类型")
public class GoodsType implements Serializable {
    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer typeId;

    @ApiModelProperty(value = "名称", example = "帽子")
    private String name;
}
